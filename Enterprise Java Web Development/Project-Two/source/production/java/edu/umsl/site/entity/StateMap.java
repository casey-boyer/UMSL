package edu.umsl.site.entity;

import java.util.LinkedHashMap;

/*Used for a select drop-down menu on the register form and edit details form*/
public class StateMap {
    //A LinkedHashMap will preserve the order of the inserted items. This will map a state name
    //to the state's abbreviation.
    private LinkedHashMap<String, String> stateMap;

    public StateMap() {
        stateMap = new LinkedHashMap<>();

        //Populate the LinkedHashMap with each state in the United States. The key is the name of
        //the state, which yields an abbreviation of the state as a value.
        stateMap.put("Alabama", "AL");
        stateMap.put("Alaska", "AK");
        stateMap.put("Arizona", "AZ");
        stateMap.put("Arkansas", "AK");
        stateMap.put("California", "CA");
        stateMap.put("Colorado", "CO");
        stateMap.put("Connecticut", "CT");
        stateMap.put("Delaware","DE");
        stateMap.put("District Of Columbia", "DC");
        stateMap.put("Florida", "FL");
        stateMap.put("Georgia", "GA");
        stateMap.put("Hawaii", "HI");
        stateMap.put("Idaho", "ID");
        stateMap.put("Illinois", "IL");
        stateMap.put("Indiana", "IN");
        stateMap.put("Iowa", "IA");
        stateMap.put("Kansas", "KS");
        stateMap.put("Kentucky", "KY");
        stateMap.put("Louisiana", "LA");
        stateMap.put("Maine", "ME");
        stateMap.put("Maryland", "MD");
        stateMap.put("Massachusetts", "MA");
        stateMap.put("Michigan", "MI");
        stateMap.put("Minnesota", "MN");
        stateMap.put("Mississippi", "MS");
        stateMap.put("Missouri", "MO");
        stateMap.put("Montana", "MT");
        stateMap.put("Nebraska", "NE");
        stateMap.put("Nevada", "NV");
        stateMap.put("New Hampshire", "NH");
        stateMap.put("New Jersey", "NJ");
        stateMap.put("New Mexico", "NM");
        stateMap.put("New York", "NY");
        stateMap.put("North Carolina", "NC");
        stateMap.put("North Dakota", "ND");
        stateMap.put("Ohio", "OH");
        stateMap.put("Oklahoma", "OK");
        stateMap.put("Oregon", "OR");
        stateMap.put("Pennsylvania", "PA");
        stateMap.put("Rhode Island", "RI");
        stateMap.put("South Carolina", "SC");
        stateMap.put("South Dakota", "SD");
        stateMap.put("Tennessee", "TN");
        stateMap.put("Texas", "TX");
        stateMap.put("Utah", "UT");
        stateMap.put("Vermont", "VT");
        stateMap.put("Virginia", "VA");
        stateMap.put("Washington", "WA");
        stateMap.put("West Virginia", "WV");
        stateMap.put("Wisconsin", "WI");
        stateMap.put("Wyoming", "WY");
    }

    public LinkedHashMap<String, String> getStateMap() {
        return stateMap;
    }

    public void setStateMap(LinkedHashMap<String, String> stateMap) {
        this.stateMap = stateMap;
    }
}
