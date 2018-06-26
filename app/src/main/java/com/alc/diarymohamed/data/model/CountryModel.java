package com.alc.diarymohamed.data.model;

import io.realm.RealmObject;

/**
 * Created by mbak on 16/02/18.
 */

public class CountryModel extends RealmObject {

    private String Code;
    private String Name;
    private String Prefix;
    private String Currency;
    private String Capital;
    private String TimeZone;
    private String Dst;
    private String DstBegin;
    private String DstEnd;

    public CountryModel copy(){
        CountryModel copy = new CountryModel();
        copy.Code = Code;
        copy.Name = Name;
        copy.Prefix = Prefix;
        copy.Currency = Currency;
        copy.Capital = Capital;
        copy.TimeZone = TimeZone;
        copy.Dst = Dst;
        copy.DstBegin = DstBegin;
        copy.DstEnd = DstEnd;
        return copy;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }


    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }


    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCapital() {
        return Capital;
    }

    public void setCapital(String capital) {
        Capital = capital;
    }

    public String getDst() {
        return Dst;
    }

    public void setDst(String dst) {
        Dst = dst;
    }

    public String getDstBegin() {
        return DstBegin;
    }

    public void setDstBegin(String dstBegin) {
        DstBegin = dstBegin;
    }

    public String getDstEnd() {
        return DstEnd;
    }

    public void setDstEnd(String dstEnd) {
        DstEnd = dstEnd;
    }
}
