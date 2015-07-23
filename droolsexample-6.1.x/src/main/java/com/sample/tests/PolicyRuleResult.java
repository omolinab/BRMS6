package com.sample.tests;

import java.util.ArrayList;
import java.util.List;
 
public class PolicyRuleResult {
 
    private boolean eligible = false;
    private boolean allCoveragesAreEligible = false;
    private boolean agentInformationValid = false;
    private boolean approvedState = false;
    private List<String> soundMessages = new ArrayList<String>();
    private boolean soundexWorksCollect = false;
    private boolean retIdentifier = false;
    private Integer totalFaceAmount = null;
    private Integer totalFaceAmountSimp = null;
    private Double averageFaceAmount = null;
 
    public boolean isAllCoveragesAreEligible() {
        return allCoveragesAreEligible;
    }
 
    public void setAllCoveragesAreEligible(boolean allCoveragesAreEligible) {
        this.allCoveragesAreEligible = allCoveragesAreEligible;
    }
 
    public boolean isEligible() {
        return eligible;
    }
 
    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
 
    public boolean isAgentInformationValid() {
        return agentInformationValid;
    }
 
    public void setAgentInformationValid(boolean agentInformationValid) {
        this.agentInformationValid = agentInformationValid;
    }
 
    public boolean isApprovedState() {
        return approvedState;
    }
 
    public void setApprovedState(boolean approvedState) {
        this.approvedState = approvedState;
    }
 
    public void addSoundMessage(String msg) {
        this.soundMessages.add(msg);
    }
 
    public List<String> getSoundMessages() {
        return this.soundMessages;
    }
 
    public boolean isSoundexWorksCollect() {
        return soundexWorksCollect;
    }
 
    public void setSoundexWorksCollect(boolean soundexWorksCollect) {
        this.soundexWorksCollect = soundexWorksCollect;
    }
 
    public boolean isRetIdentifier() {
        return retIdentifier;
    }
 
    public void setRetIdentifier(boolean retIdentifier) {
        this.retIdentifier = retIdentifier;
    }
 
    public Integer getTotalFaceAmount() {
        return totalFaceAmount;
    }
 
    public void setTotalFaceAmount(Integer totalFaceAmount) {
        this.totalFaceAmount = totalFaceAmount;
    }
 
    public Integer getTotalFaceAmountSimp() {
        return totalFaceAmountSimp;
    }
 
    public void setTotalFaceAmountSimp(Integer totalFaceAmountSimp) {
        this.totalFaceAmountSimp = totalFaceAmountSimp;
    }
 
    public Double getAverageFaceAmount() {
        return averageFaceAmount;
    }
 
    public void setAverageFaceAmount(Double averageFaceAmount) {
        this.averageFaceAmount = averageFaceAmount;
    }
 
}