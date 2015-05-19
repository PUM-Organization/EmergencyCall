package com.pomorganization.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 5/14/2015.
 */
public class ShiftRegisterList<T> extends ArrayList<T> implements List<T> {
    private int maxSize;

    public ShiftRegisterList(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T o)
    {
        Log.d("PUMMMMMMMMMMMMMMM","ROZMIAR : "+this.size());
        if(super.size() < maxSize)
        {
            super.add(o);
        }
        else
        {
            super.remove(0);
            super.add(o);
        }
        return true;
    }
}
