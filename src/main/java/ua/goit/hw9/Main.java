package ua.goit.hw9;

import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        TimezoneValidateFilter filter = new TimezoneValidateFilter();
        System.out.println(filter.isValidTimezone("UTS-4"));
        System.out.println(TimeZone.getTimeZone(""));
    }
}