package com.mohamed_mosabeh.course_project.objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class CarFine {
    private int fineCode;
    private String issueDate;
    private int ownerId;
    private String ownerName;
    private float fineAmount;
    private String carType;
    private String violation;
    private String description = "";
    private LatLng latLng = new LatLng(24.465135, 54.347118);
    
    /** Yes, we need the default constructor and all the getter and setters
     *  so we could set the values when they are Queried */
    public CarFine() {
    
    }
    
    public CarFine(int fineCode, String issueDate, int ownerId, String ownerName, float fineAmount, String carType, String violation, String description) {
        this.fineCode = fineCode;
        this.issueDate = issueDate;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.fineAmount = fineAmount;
        this.carType = carType;
        this.violation = violation;
        this.description = description;
    }
    
    public int getFineCode() {
        return fineCode;
    }
    
    public void setFineCode(int fineCode) {
        this.fineCode = fineCode;
    }
    
    public String getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    
    public int getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public float getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getCarType() {
        return carType;
    }
    
    public void setCarType(String carType) {
        this.carType = carType;
    }
    
    public String getViolation() {
        return violation;
    }
    
    public void setViolation(String violation) {
        this.violation = violation;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double[] getLatLng() {
        return new Double[]{latLng.latitude, latLng.longitude};
    }
    
    public void setLatLng(Double[] latLng) {
        this.latLng = new LatLng(latLng[0], latLng[1]);
    }
}