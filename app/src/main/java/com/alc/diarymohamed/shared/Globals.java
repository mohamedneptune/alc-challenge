package com.alc.diarymohamed.shared;

import android.content.Context;

import com.alc.diarymohamed.data.model.TimeModel;


/**
 * Created by mohamed on 25/06/18.
 */


public class Globals {

    private static Context applicationContext;


    private Globals() {
    }

    public static void setApplicationContext(Context applicationContext) {
        Globals.applicationContext = applicationContext;
    }

    public static TimeModel setTimeFormat(int hour, int minute) {
        TimeModel timeModel = new TimeModel();
        int hourModif = hour;
        int minModif = minute;

        if(minute >59){
            minModif = minute % 60;
            hourModif = hour + (minute / 60);
        }

        if(hourModif > 23){
            hourModif = hourModif - 24;
        }

        if(hourModif < 0){
            hourModif = hourModif + 24;
        }

        if (hourModif < 10) {
            timeModel.setHour("0" + hourModif);
        } else {
            timeModel.setHour("" + hourModif);
        }

        if (minModif < 10) {
            timeModel.setMinute("0" + minModif);
        } else {
            timeModel.setMinute("" + minModif);
        }
        return timeModel;
    }

    public static String getMonth(int intMonth){

        switch (intMonth){
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April ";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default:return "";
        }
    }

}
