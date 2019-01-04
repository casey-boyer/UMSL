package edu.umsl.site.entity;

import java.util.LinkedHashMap;

public class TimeZoneMap {
    //LinkedHashMap preserves the order of the inserted elements.
    private LinkedHashMap<String, String> timezoneMap;

    public TimeZoneMap() {
        timezoneMap = new LinkedHashMap<>();

        //These are all of the time zones observed in the United States. The key
        //is the name of the time zone, and the value is the abbreviation of the time zone
        //for standard and daylight time. These values are based on the time zones used in the
        //United States, listed on:
        //https://en.wikipedia.org/wiki/Time_in_the_United_States#United_States_time_zones
        timezoneMap.put("Atlantic Time Zone", "AST, ADT");
        timezoneMap.put("Eastern Time Zone", "EST, EDT");
        timezoneMap.put("Central Time Zone", "CST, CDT");
        timezoneMap.put("Mountain Time Zone", "MST, MDT");
        timezoneMap.put("Pacific Time Zone", "PST, PDT");
        timezoneMap.put("Alaska Time Zone", "AKST, AKDT");
        timezoneMap.put("Hawaii–Aleutian Time Zone", "HST, HDT");
        timezoneMap.put("Samoa Time Zone", "SST");
        timezoneMap.put("Chamorro Time Zone", "ChST");
    }

    public LinkedHashMap<String, String> getTimezoneMap() {
        return timezoneMap;
    }

    public void setTimezoneMap(LinkedHashMap<String, String> timezoneMap) {
        this.timezoneMap = timezoneMap;
    }
}
