package com.example.mytodoister.util;

import android.graphics.Color;

import com.example.mytodoister.model.Priority;
import com.example.mytodoister.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");
        return simpleDateFormat.format(date);
    }
    public static int priorityColor(Task task){
        int color;
        if (task.getPriority() == Priority.HIGH){
            color = Color.argb(200,201,21,23);
        }else if (task.getPriority()==Priority.MEDIUM){
            color = Color.argb(200,155,179,0);
        }else {
            color = Color.argb(200,51,181,129);
        }
        return color;
    }
}
