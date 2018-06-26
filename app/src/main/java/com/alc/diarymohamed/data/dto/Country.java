package com.alc.diarymohamed.data.dto;

import java.lang.annotation.Annotation;

/**
 * Created by mbak on 13/02/18.
 */

@XMLSequence({
        "Code",
        "Name",
        "Prefix",
        "Currency",
        "Capital",
        "TimeZone",
        "Dst",
        "DstBegin",
        "DstEnd",
})

public class Country implements XMLSequence {

    private String Code;
    private String Name;
    private String Prefix;
    private String Currency;
    private String Capital;
    private String TimeZone;
    private String Dst;
    private String DstBegin;
    private String DstEnd;

    @Override
    public String[] value() {
        return new String[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }


    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }


    public void setCurrency(String currency) {
        Currency = currency;
    }

    public void setCapital(String capital) {
        Capital = capital;
    }

    public void setDst(String dst) {
        Dst = dst;
    }

    public void setDstBegin(String dstBegin) {
        DstBegin = dstBegin;
    }

    public void setDstEnd(String dstEnd) {
        DstEnd = dstEnd;
    }
}
