package org.poo.bank.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class Date {
    private int year;
    private int month;
    private int day;
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
