package com.sample.tests;

public class Coverage {
	 
    private boolean eligible = false;
 
    private Insured insured = null;
 
    private int faceAmount = 0;
 
    public boolean isEligible() {
        return eligible;
    }
 
    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
 
    public Insured getInsured() {
        return insured;
    }
 
    public void setInsured(Insured insured) {
        this.insured = insured;
    }
 
    public int getFaceAmount() {
        return faceAmount;
    }
 
    public void setFaceAmount(int faceAmount) {
        this.faceAmount = faceAmount;
    }
 
}