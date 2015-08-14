/**
 * Copyright (c) 2005, by:      Angelo Marletta <marlonx80@hotmail.com>
 *
 * This file may be used subject to the terms and conditions of the
 * GNU Library General Public License Version 2, or any later version
 * at your option, as published by the Free Software Foundation.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 **********************************************************************
 *
 * Simple program to limit the cpu usage of a process
 * If you modify this code, send me a copy please
 *
 * Author:  Angelo Marletta
 * Date:    26/06/2005
 * Version: 1.1
 * Last version at: http://marlon80.interfree.it/cpulimit/index.html
 *
 * Changelog:
 *  - Fixed a segmentation fault if controlled process exited in particular circumstances
 *  - Better CPU usage estimate
 *  - Fixed a <0 %CPU usage reporting in rare cases
 *  - Replaced MAX_PATH_SIZE with PATH_MAX already defined in <limits.h>
 *  - Command line arguments now available
 *  - Now is possible to specify target process by pid
 */


#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include <sys/resource.h>
#include <string.h>
#include <dirent.h>
#include <errno.h>
#include <string.h>

//kernel time resolution (inverse of one jiffy interval) in Hertz
//i don't know how to detect it, then define to the default (not very clean!)
#define HZ 100

//some useful macro
#define min(a,b) (a<b?a:b)
#define max(a,b) (a>b?a:b)

//pid of the controlled process
int pid=0;
//executable file name
//nazwa programu
char *program_name;
//verbose mode
int verbose=0;
//lazy mode
int lazy=0;

//reverse byte search
void *memrchr(const void *s, int c, size_t n); // scan memomy for char

//return ta-tb in microseconds (no overflow checks!)
inline long timediff(const struct timespec *ta,const struct timespec *tb) {
    unsigned long us = (ta->tv_sec-tb->tv_sec)*1000000 + (ta->tv_nsec/1000 - tb->tv_nsec/1000);
    return us;
}

int waitforpid(int pid) {
	//switch to low priority
	//wywołuje funckcje zmniejszająca priorytet procesu programu jeżeli nie udało się zmniejszyć priorytetu to wyswietl warning
	if (setpriority(PRIO_PROCESS,getpid(),19)!=0) {
		printf("Warning: cannot renice\n");
	}

	int i=0;

	while(1) {
		//handler na katalog (katalog jest plikiem jakby co)
		DIR *dip;
		//zawartość pól struktury dirent
		//	struct dirent
		//	{
    	//		ino_t          d_ino;       /* inode number */
    	//		off_t          d_off;       /* offset to the next dirent */
    	//		unsigned short d_reclen;    /* length of this record */
    	//		unsigned char  d_type;      /* type of file; not supported
        //	                           by all file system types */
    	//		char           d_name[256]; /* filename */
		//	};
		struct dirent *dit;

		//open a directory stream to /proc directory
		//otwiera plik /proc jeśli nie udało się otworzyć pliku 
		if ((dip = opendir("/proc")) == NULL) {
			perror("opendir");
			return -1;
		}
		
		//read in from /proc and seek for process dirs

		//jesli się udało jednak otworzyć plik to czytaj wszystko co jest w katalogu /proc
		while ((dit = readdir(dip)) != NULL) {
			//get pid
			//jesli uda
			//dit->d_name odwolanie sie do pola struktury typu dirent
			if (pid==atoi(dit->d_name)) {
				//pid detected
				//kill wysyla sygnal do procesu o pidzie =pid, SIGSTOP(sygnal zatrzymania procesu(nie jego zamknięcia!!)) SIGCONT (sygnal wznowienia procesu po jego zatrzymaniu) 
				//jesli udalo się te 2 operacje wykonac (proces istnieje) masz nad nim kontrole możesz go zatrzymac wystartowac spowrotem
				if (kill(pid,SIGSTOP)==0 &&  kill(pid,SIGCONT)==0) {
					//process is ok!
					//skocz to etykiety done
					goto done;
				}
				//nie masz kontroli nad dostepnym procesem
				else {
					
					fprintf(stderr,"Error: Process %d detected, but you don't have permission to control it\n",pid);
				}
			}
		}

		//close the dir stream and check for errors
		//próbuje zamknąć uchwyt do pliku //proc
		if (closedir(dip) == -1) {
			//jesli się nie udało to wydrukuje na standardowe wyjscie bledów info w nawiasie
			perror("closedir");
			return -1;
		}
		//jesli i jest równe 0 
		//no suitable target found
		if (i++==0) {
			//sprawdz czy program zostal odpalony w trybie lazy czyli z parametrem -z 
			if (lazy) {
				//nie ma procesu
				fprintf(stderr,"No process found\n");
				//wylacz cpulimiter
				exit(2);
			}
			//w innym wypadku czekaj na proces o danym pidzie
			else {
				printf("Warning: no target process found. Waiting for it...\n");
			}
		}

		//sleep for a while
		sleep(2);
	}
//etykieta done
done:
	printf("Process %d detected\n",pid);
	//now set high priority, if possible
	if (setpriority(PRIO_PROCESS,getpid(),-20)!=0) {
		printf("Warning: cannot renice.\nTo work better you should run this program as root.\n");
	}
	return 0;

}

//this function periodically scans process list and looks for executable path names
//it should be executed in a low priority context, since precise timing does not matter
//if a process is found then its pid is returned
//process: the name of the wanted process, can be an absolute path name to the executable file
//         or simply its name
//return: pid of the found process





/*

funkcja zwraca pid procesu

*/
int getpidof(const char *process) {

	//set low priority
	if (setpriority(PRIO_PROCESS,getpid(),19)!=0) {
		printf("Warning: cannot renice\n");
	}

	char exelink[20];
	char exepath[PATH_MAX+1];
	int pid=0;
	int i=0;

	while(1) {

		DIR *dip;
		struct dirent *dit;

		//open a directory stream to /proc directory
		if ((dip = opendir("/proc")) == NULL) {
			perror("opendir");
			return -1;
		}

		//read in from /proc and seek for process dirs
		while ((dit = readdir(dip)) != NULL) {
			//get pid
			pid=atoi(dit->d_name);
			if (pid>0) {
				sprintf(exelink,"/proc/%d/exe",pid);
				int size=readlink(exelink,exepath,sizeof(exepath));
				if (size>0) {
					int found=0;
					if (process[0]=='/' && strncmp(exepath,process,size)==0 && size==strlen(process)) {
						//process starts with / then it's an absolute path
						found=1;
					}
					else {
						//process is the name of the executable file
						if (strncmp(exepath+size-strlen(process),process,strlen(process))==0) {
							found=1;
						}
					}
					if (found==1) {
						if (kill(pid,SIGSTOP)==0 &&  kill(pid,SIGCONT)==0) {
							//process is ok!
							goto done;
						}
						else {
							fprintf(stderr,"Error: Process %d detected, but you don't have permission to control it\n",pid);
						}
					}
				}
			}
		}

		//close the dir stream and check for errors
		if (closedir(dip) == -1) {
			perror("closedir");
			return -1;
		}

		//no suitable target found
		if (i++==0) {
			if (lazy) {
				fprintf(stderr,"No process found\n");
				exit(2);
			}
			else {
				printf("Warning: no target process found. Waiting for it...\n");
			}
		}

		//sleep for a while
		sleep(2);
	}

done:
	printf("Process %d detected\n",pid);
	//now set high priority, if possible
	if (setpriority(PRIO_PROCESS,getpid(),-20)!=0) {
		printf("Warning: cannot renice.\nTo work better you should run this program as root.\n");
	}
	return pid;

}

//SIGINT and SIGTERM signal handler
void quit(int sig) {
	//let the process continue if it's stopped
	kill(pid,SIGCONT);
	printf("Exiting...\n");
	exit(0);
}

//get jiffies count from /proc filesystem
int getjiffies(int pid) {
	static char stat[20];
	static char buffer[1024];
	sprintf(stat,"/proc/%d/stat",pid);
	FILE *f=fopen(stat,"r");
	if (f==NULL) return -1;
	fgets(buffer,sizeof(buffer),f);
	fclose(f);
	char *p=buffer;
	p=memchr(p+1,')',sizeof(buffer)-(p-buffer));
	int sp=12;
	while (sp--)
		p=memchr(p+1,' ',sizeof(buffer)-(p-buffer));
	//user mode jiffies
	int utime=atoi(p+1);
	p=memchr(p+1,' ',sizeof(buffer)-(p-buffer));
	//kernel mode jiffies
	int ktime=atoi(p+1);
	return utime+ktime;
}

//process instant photo
struct process_screenshot {
	struct timespec when;	//timestamp
	int jiffies;	//jiffies count of the process
	int cputime;	//microseconds of work from previous screenshot to current
};

//extracted process statistics
struct cpu_usage {
	float pcpu;
	float workingrate;
};

//this function is an autonomous dynamic system
//it works with static variables (state variables of the system), that keep memory of recent past
//its aim is to estimate the cpu usage of the process
//to work properly it should be called in a fixed periodic way
//perhaps i will put it in a separate thread...

//oblicza uzycie procesora przez dany proces o danym pidzie 
//funkcja zbiera pewna ilosć cykli procesora(jiffies) jak nazbiera chyba 10 ale nie jestem pewien to różniczkuje po czasie 
int compute_cpu_usage(int pid,int last_working_quantum,struct cpu_usage *pusage) {
	#define MEM_ORDER 10
	//circular buffer containing last MEM_ORDER process screenshots
	static struct process_screenshot ps[MEM_ORDER];
	//the last screenshot recorded in the buffer
	static int front=-1;
	//the oldest screenshot recorded in the buffer
	static int tail=0;
	//jesli struktura pusage jest pusta
	if (pusage==NULL) {
		//reinit static variables
		front=-1;
		tail=0;
		return 0;
	}

	//let's advance front index and save the screenshot
	front=(front+1)%MEM_ORDER;
	int j=getjiffies(pid);
	if (j>=0) ps[front].jiffies=j;
	else return -1;	//error: pid does not exist
	clock_gettime(CLOCK_REALTIME,&(ps[front].when));
	ps[front].cputime=last_working_quantum;

	//buffer actual size is: (front-tail+MEM_ORDER)%MEM_ORDER+1
	int size=(front-tail+MEM_ORDER)%MEM_ORDER+1;

	if (size==1) {
		//not enough samples taken (it's the first one!), return -1
		pusage->pcpu=-1;
		pusage->workingrate=1;
		return 0;
	}
	else {
		//now we can calculate cpu usage, interval dt and dtwork are expressed in microseconds
		long dt=timediff(&(ps[front].when),&(ps[tail].when));
		long dtwork=0;
		int i=(tail+1)%MEM_ORDER;
		int max=(front+1)%MEM_ORDER;
		do {
			dtwork+=ps[i].cputime;
			i=(i+1)%MEM_ORDER;
		} while (i!=max);
		int used=ps[front].jiffies-ps[tail].jiffies;
		float usage=(used*1000000.0/HZ)/dtwork;
		pusage->workingrate=1.0*dtwork/dt;
		pusage->pcpu=usage*pusage->workingrate;
		if (size==MEM_ORDER)
			tail=(tail+1)%MEM_ORDER;
		return 0;
	}
	#undef MEM_ORDER
}

void print_caption() {
	printf("\n%%CPU\twork quantum\tsleep quantum\tactive rate\n");
}
//funkcja wyswietlaca do strumienia stream informacje o tym jak użyć program
void print_usage(FILE *stream,int exit_code) {
	fprintf(stream, "Usage: %s TARGET [OPTIONS...]\n",program_name);
	fprintf(stream, "   TARGET must be exactly one of these:\n");
	fprintf(stream, "      -p, --pid=N        pid of the process\n");
	fprintf(stream, "      -e, --exe=FILE     name of the executable program file\n");
	fprintf(stream, "      -P, --path=PATH    absolute path name of the executable program file\n");
	fprintf(stream, "   OPTIONS\n");
	fprintf(stream, "      -l, --limit=N      percentage of cpu allowed from 0 to 100 (mandatory)\n");
	fprintf(stream, "      -v, --verbose      show control statistics\n");
	fprintf(stream, "      -z, --lazy         exit if there is no suitable target process, or if it dies\n");
	fprintf(stream, "      -h, --help         display this help and exit\n");
	exit(exit_code);
}

//cpulimiter -p -h
//argc liczba parametrów przekazywanych podczas uruchamiania programu

//**argv = *argv[] wskaznik na tablicê z parametrami
int main(int argc, char **argv) {



	//get program name
	//wyszukueje ostatni znak '/' i zwraca jego wskaźnik(przesówa wskaźnik na ostatni znak '/')
	//1 argument memchr to wskaźnik na miejsce w pamięci (np. początek tekstu) 
	//2 argument memchr to wartosc w pamięci jakiej poszukujemy ( w tym wypadku szukamy znaku '/' więc trzeba go zrzutować na int)
	//3 argument memchr rozmiar pamięci jaki przeszukujemy(w tym wypadku ilosc znaków w ciągu tekstowym)
	char *p=(char*)memrchr(argv[0],(unsigned int)'/',strlen(argv[0]));

	//zapisanie nazwy programu do zmiennej 
	//program_name = p ==NULL?argv[0]:(p+1)
	//można zapisać 
	//if(p==NULL)
	//{
	//		program_name= argv[0]
	//}
	//else{
	//		program_name=(p+1)
	//}

	program_name = p==NULL?argv[0]:(p+1);
	//parse arguments
	int next_option;
	/* A string listing valid short options letters. */
	const char* short_options="p:e:P:l:vzh";
	/* An array describing valid long options. */
	const struct option long_options[] = {
		{ "pid", 0, NULL, 'p' },
		{ "exe", 1, NULL, 'e' },
		{ "path", 0, NULL, 'P' },
		{ "limit", 0, NULL, 'l' },
		{ "verbose", 0, NULL, 'v' },
		{ "lazy", 0, NULL, 'z' },
		{ "help", 0, NULL, 'h' },
		{ NULL, 0, NULL, 0 }
	};
	//argument variables
	const char *exe=NULL;
	const char *path=NULL;
	int perclimit=0;
	int pid_ok=0;
	int process_ok=0;
	int limit_ok=0;

	do {
		next_option = getopt_long (argc, argv, short_options,long_options, NULL);
		switch(next_option) {
			case 'p':
`				// -p 1234 lub --pid=1234 wtedy optarg = "1234" trzeba zamienić tekst na liczbe
				//ascii to int ( zamienia 
				pid=atoi(optarg);
				//stwierdza że poprawnie odczytano pid programu który chcemy limitować
				pid_ok=1;
				break;
			case 'e':
				//do exe przypisywana jest nazwa programu który chcemy limitowac  
				exe=optarg;
				process_ok=1;
				break;
			case 'P':
				//do path przypisywana jest pelna sciezka do programu
				path=optarg;
				process_ok=1;
				break;
			case 'l':
				//do zmiennej perclimit przypisywana jest wartośc w procentach do której mamy ograniczyć programowi zuzycie cpu zamieniona z tekstu na liczbe
				perclimit=atoi(optarg);
				limit_ok=1;
				break;
			case 'v':
				//wlacza opcje pokazywania statystyk podczas dzialania programu
				verbose=1;
				break;
			case 'z':
				
				lazy=1;
				break;
			case 'h':
				print_usage (stdout, 1);
				break;
			case '?':
				print_usage (stderr, 1);
				break;
			//gdy koniec parametróœ
			case -1:
				break;
			default:
				abort();
		}
	} while(next_option != -1);
	//jeżeli nie udało się pobrać pidu i nazwy procesu do limitowania
	if (!process_ok && !pid_ok) {
		//wyświetl bląd
		fprintf(stderr,"Error: You must specify a target process\n");
		//wyswietla na standardowe wyjscie bledów (terminal w tym wypadku)  info o blędzie i jego kod bledu --- 1
		print_usage (stderr, 1);
		//zakończ program z blędem 
		exit(1);
	}
	//kolejne zabezpieczenie przed niepodaniem danych
	if ((exe!=NULL && path!=NULL) || (pid_ok && (exe!=NULL || path!=NULL))) {
		fprintf(stderr,"Error: You must specify exactly one target process\n");
		print_usage (stderr, 1);
		exit(1);
	}
	//jeśli nie podano wartosci limitu w procentach
	if (!limit_ok) {
		fprintf(stderr,"Error: You must specify a cpu limit\n");
		print_usage (stderr, 1);
		exit(1);
	}
	//zamienia procenty na wartość zmienno przecinkową z zakresu 0 ... 1
	float limit=perclimit/100.0;
	//sprawdza czy nie wyszedles poza zakres 
	if (limit<0 || limit >1) {
		fprintf(stderr,"Error: limit must be in the range 0-100\n");
		print_usage (stderr, 1);
		exit(1);
	}
	//parameters are all ok!
	//mówi jaka funkcja ma być wywołana po pojawieniu się sygnału SIGINT
	signal(SIGINT,quit);
	//mówi  jaka funkcja ma być wywołana po pojawieniu się sygnału SIGTERM
	signal(SIGTERM,quit);

	//time quantum in microseconds. it's splitted in a working period and a sleeping one
	int period=100000;
	struct timespec twork,tsleep;   //working and sleeping intervals
	//rezerwuje pamięć dla struktur twork,tsleep typu timespec i ustawia wartość 0 do pamięci 
	memset(&twork,0,sizeof(struct timespec));
	memset(&tsleep,0,sizeof(struct timespec));
//etykieta dla goto
wait_for_process:

	//look for the target process..or wait for it
	//jeżeli nazwa programu jest różna od null ( nie jest pusta czyli została podana z parametrem -e)
	if (exe!=NULL)
		//zczytaj jej pid
		pid=getpidof(exe);
	else if (path!=NULL)
		//jeżeli podana została pełna sciezka do programu to zczytaj jej pid
		pid=getpidof(path);
	else {
		//wywołaj funkcję waitforpid która czeka na odpalenie programu ktory chcemy ograniczać lub zamyka cpulimiter(w trybie lazy) jeśli nie znalazł procesu w katalogu /proc
		waitforpid(pid);
	}
	//process detected...let's play

	//init compute_cpu_usage internal stuff
	//funkcja obliczajaca uzycie procesora
	compute_cpu_usage(0,0,NULL);
	//main loop counter
	int i=0;

	struct timespec startwork,endwork;
	long workingtime=0;		//last working time in microseconds

	if (verbose) print_caption();

	float pcpu_avg=0;

	//here we should already have high priority, for time precision
	while(1) {

		//estimate how much the controlled process is using the cpu in its working interval
		struct cpu_usage cu;
	//jesli nie udało się obliczyć zuzycia procesora to znaczy ze proces prawdopodobnie już nie żyje
		if (compute_cpu_usage(pid,workingtime,&cu)==-1) {
			fprintf(stderr,"Process %d dead!\n",pid);
			//jesli tryb lazy to wylacz cpulimiter
			if (lazy) exit(2);
			//wait until our process appears
			//jesli nie to poszukaj procesu w katalogu /proc
			goto wait_for_process;
		}

		//cpu actual usage of process (range 0-1)
		//zapisuje do zmiennej typu float odczytane zuzycie procka przez dany proces ze struktury w której są zapisane dane z funkcji compute_cpu_usage 
		float pcpu=cu.pcpu;
		//rate at which we are keeping active the process (range 0-1)
		float workingrate=cu.workingrate;

		//adjust work and sleep time slices (reguluje czasem pracy i uspania procesu)
		//jesli zuzycie procka wyzsze od 0 
		if (pcpu>0) {
			twork.tv_nsec=min(period*limit*1000/pcpu*workingrate,period*1000);
		}
		//jesli uzywasz cpu w 0 procentach dostajesz pełen okres pracy
		else if (pcpu==0) {
			twork.tv_nsec=period*1000;
		}
		else if (pcpu==-1) {
			//nieprawidlowa wartosc zuzycia cpu
			//not yet a valid idea of cpu usage
			pcpu=limit;
			workingrate=limit;
			twork.tv_nsec=min(period*limit*1000,period*1000);
		}
		//czas spania = okres - czas pracy 
		tsleep.tv_nsec=period*1000-twork.tv_nsec;

		//update average usage
		pcpu_avg=(pcpu_avg*i+pcpu)/(i+1);
		//jesli wlaczono tryb verbose i jest to co 10 wywolanie petli while i nie jest to jej pierwszy obieg petli
		if (verbose && i%10==0 && i>0) {
			printf("%0.2f%%\t%6ld us\t%6ld us\t%0.2f%%\n",pcpu*100,twork.tv_nsec/1000,tsleep.tv_nsec/1000,workingrate*100);
		}
		//jesli limit jest ustalony na prawidlowy od 0 .. 1
		if (limit<1 && limit>0) {
			//resume process
			//wyslij sygnal wznowienia procesu
			if (kill(pid,SIGCONT)!=0) {
				//jesli sie nie udalo wyslac sygnalu znaczy proces umarł wyswietl o tym info na standardowe wyjscie bledow
				fprintf(stderr,"Process %d dead!\n",pid);
				//jesli tryb lazy wylacz program cpulimiter
				if (lazy) exit(2);
				//wait until our process appears
				//jesli nie czekaj na pojawienie się procesu
				goto wait_for_process;
			}
		}
//
		clock_gettime(CLOCK_REALTIME,&startwork);
		nanosleep(&twork,NULL);		//now process is working zawiesza watek (pewnie programu cpulimiter) program limitowany dziala(śpi tylko cpulimiter a proces o danym pidzie dalej działa)
		clock_gettime(CLOCK_REALTIME,&endwork);
		workingtime=timediff(&endwork,&startwork); 

		if (limit<1) {
			//stop process, it has worked enough
			if (kill(pid,SIGSTOP)!=0) {
				fprintf(stderr,"Process %d dead!\n",pid);
				if (lazy) exit(2);
				//wait until our process appears
				goto wait_for_process;
			}
			nanosleep(&tsleep,NULL);	//now process is sleeping usypia cpulimiter na czas spania procesu który limitujemy (2 naraz śpią)
		}
		i++;
	}

}