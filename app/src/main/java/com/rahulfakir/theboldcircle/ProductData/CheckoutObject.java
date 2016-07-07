package com.rahulfakir.theboldcircle.ProductData;

/**
 * Created by rahulfakir on 6/25/16.
 */

public class CheckoutObject {
    private String name, sku, storeID, employeeName, employeeID, appointmentStoreName, getAppointmentStoreId, appointmentDate, fullAppointmentDate, fullAppointmentTime;
    private Double price, appointmentTime;
    private int type;
    private Boolean appointmentStatus = false;
    int skillRequired, sessionsRequired, day, month, year;

    public CheckoutObject(String name, String sku, Double price, int type) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSkillRequired() {
        return skillRequired;
    }

    public void setSkillRequired(int skillRequired) {
        this.skillRequired = skillRequired;
    }

    public int getSessionsRequired() {
        return sessionsRequired;
    }

    public void setSessionsRequired(int sessionsRequired) {
        this.sessionsRequired = sessionsRequired;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public boolean getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(boolean appointmentSet) {
        this.appointmentStatus = appointmentSet;
    }


    public String getAppointmentStoreName() {
        return appointmentStoreName;
    }

    public void setAppointmentStoreName(String appointmentStoreName) {
        this.appointmentStoreName = appointmentStoreName;
    }

    public String getGetAppointmentStoreId() {
        return getAppointmentStoreId;
    }

    public void setAppointmentStoreId(String getAppointmentStoreId) {
        this.getAppointmentStoreId = getAppointmentStoreId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Double getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Double appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getFullAppointmentDate() {
        return fullAppointmentDate;
    }

    public void setFullAppointmentDate(String fullAppointmentDate) {
        this.fullAppointmentDate = fullAppointmentDate;
    }

    public String getFullAppointmentTime() {
        return fullAppointmentTime;
    }

    public void setFullAppointmentTime(String fullAppointmentTime) {
        this.fullAppointmentTime = fullAppointmentTime;
    }
}
