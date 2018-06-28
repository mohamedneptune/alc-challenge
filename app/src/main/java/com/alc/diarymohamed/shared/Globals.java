package com.alc.diarymohamed.shared;

import android.content.Context;

import com.alc.diarymohamed.data.model.TimeModel;

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
            case 1: return "Janvier";
            case 2: return "Février";
            case 3: return "Mars";
            case 4: return "Avril";
            case 5: return "Mai";
            case 6: return "Juin";
            case 7: return "Juillet";
            case 8: return "Août";
            case 9: return "Septembre";
            case 10: return "Octobre";
            case 11: return "Novembre";
            case 12: return "Décembre";
            default:return "";
        }
    }

}
