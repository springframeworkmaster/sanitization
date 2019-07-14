package com.tasks.validation.sanitization.domain;

import java.io.Serializable;


public class Person implements Serializable {
    private static final long serialVersionUID = -3934137806078558433L;

    private String first;

    private String middle;

    private String last;
 
    private String preferredFirst;

    private String prefix;

    private String[] professionalSuffixes;

    private String generationalSuffix;



    public Person() {
    }

    public String getFirst() {
        return this.first;
    }

    public String getMiddle() {
        return this.middle;
    }

    public String getLast() {
        return this.last;
    }

    public String getPreferredFirst() {
        return this.preferredFirst;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String[] getProfessionalSuffixes() {
        return this.professionalSuffixes;
    }

    public String getGenerationalSuffix() {
        return this.generationalSuffix;
    }


    public void setFirst(String first) {
        this.first = first;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setPreferredFirst(String preferredFirst) {
        this.preferredFirst = preferredFirst;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setProfessionalSuffixes(String[] professionalSuffixes) {
        this.professionalSuffixes = professionalSuffixes;
    }

    public void setGenerationalSuffix(String generationalSuffix) {
        this.generationalSuffix = generationalSuffix;
    }
}
