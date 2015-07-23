package com.sample.tests;

import java.util.ArrayList;
import java.util.List;
 
public class Policy {
 
    private List<Coverage> coverages = new ArrayList<Coverage>();
 
    private List<String> agents = new ArrayList<String>();
    private String issueState = null;
    private List<String> sample = new ArrayList<String>();
 
    public List<Coverage> getCoverages() {
        return coverages;
    }
 
    public void setCoverages(List<Coverage> coverages) {
        this.coverages = coverages;
    }
 
    public void addCoverage(Coverage coverage) {
        this.coverages.add(coverage);
    }
 
    public List<String> getAgents() {
        return agents;
    }
 
    public void setAgents(List<String> agents) {
        this.agents = agents;
    }
 
    public void addAgent(String agentName) {
        this.agents.add(agentName);
    }
 
    public List<String> getSample() {
        return sample;
    }
 
    public void setSample(List<String> sample) {
        this.sample = sample;
    }
 
    public String getIssueState() {
        return issueState;
    }
 
    public void setIssueState(String issueState) {
        this.issueState = issueState;
    }
 
}