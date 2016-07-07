package com.rahulfakir.theboldcircle.StoreData;

public class StoreObject {
    private String ID, address, name, email, phoneNumber;
    private double hours[][] = {{-1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1,}};
    public StoreObject(){

    }

    public StoreObject(String ID,  String name, String phoneNumber, String email, String address){
        this.ID = ID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHours(int day, int boundary, double time){
        hours[boundary][day] = time;
    }

    public String getStringHours(int day, int boundary){
        double time = hours[boundary][day];
        int hours = (int) time;
        int minutes = (int) (time * 60) % 60;
        String sHours = String.valueOf(hours);
        String sMin = String.valueOf(minutes);
        if (String.valueOf(sHours).length() == 1) {
            sHours = "0" + sHours;
        }
        if (String.valueOf(sMin).length() == 1) {
            sMin =  "0" + sMin;
        }

        return sHours + ":" + sMin;
    }


}
