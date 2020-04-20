package com.example.loginschoolpool;

public class StudentMember {

    private String username;
    private  String homeAddress;
    private String objectMapperObservabilityDateAndTime;
    private String ImageName;
    private int thumbnail;
    private boolean isDriveFromHome;
    private boolean isDrive;
    private String kmFromMember;

    public String getKmFromMember() {
        return kmFromMember;
    }

    public void setKmFromMember(String kmFromMember) {
        this.kmFromMember = kmFromMember;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public boolean isDriveFromHome() {
        return isDriveFromHome;
    }

    public void setDriveFromHome(boolean driveFromHome) {
        isDriveFromHome = driveFromHome;
    }

    public boolean isDrive() {
        return isDrive;
    }

    public void setDrive(boolean drive) {
        isDrive = drive;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public StudentMember() {

    }

    public String getObjectMapperObservabilityDateAndTime() {
        return objectMapperObservabilityDateAndTime;
    }

    public void setObjectMapperObservabilityDateAndTime(String objectMapperObservabilityDateAndTime) {
        this.objectMapperObservabilityDateAndTime = objectMapperObservabilityDateAndTime;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }




    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
