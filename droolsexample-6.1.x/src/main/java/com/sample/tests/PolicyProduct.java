package com.sample.tests;

import java.util.ArrayList;
import java.util.List;
 
public class PolicyProduct {
 
    private List<String> states = new ArrayList<String>();
 
    {
        states.add("VA");
        states.add("WI");
        states.add("NY");
    }
 
    public List<String> getStates() {
        return states;
    }
 
    public void setStates(List<String> states) {
        this.states = states;
    }
 
}

