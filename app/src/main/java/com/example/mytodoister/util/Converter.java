package com.example.mytodoister.util;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.mytodoister.model.Priority;

import java.util.Date;

public class Converter {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null? null: date.getTime();
    }
    @TypeConverter
    public static String fromPriority(Priority priority){
        return priority == null ? null : priority.name();
    }
    @TypeConverter
    public static Priority toPriority(String priority){
        return priority == null ? null :Priority.valueOf(priority);
    }

}
