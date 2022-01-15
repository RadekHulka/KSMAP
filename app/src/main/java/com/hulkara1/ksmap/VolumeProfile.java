package com.hulkara1.ksmap;

public class VolumeProfile {
    private int id;
    private String name;
    private int media, alarm, system, notification, call, ringing;
    private double latitude, longitude;
    private int radius;
    private int fromMin, toMin;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private boolean inUse;

    public VolumeProfile(int id, String name, int media, int alarm, int system, int notification, int call, int ringing, double latitude, double longitude, int radius, int fromMin, int toMin, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, boolean inUse) {
        this.id = id;
        this.name = name;
        this.media = media;
        this.alarm = alarm;
        this.system = system;
        this.notification = notification;
        this.call = call;
        this.ringing = ringing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.fromMin = fromMin;
        this.toMin = toMin;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return "VolumeProfile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", media=" + media +
                ", alarm=" + alarm +
                ", system=" + system +
                ", notification=" + notification +
                ", call=" + call +
                ", ringing=" + ringing +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", fromMin=" + fromMin +
                ", toMin=" + toMin +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", inUse=" + inUse +
                '}';
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getRinging() {
        return ringing;
    }

    public void setRinging(int ringing) {
        this.ringing = ringing;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFromMin() {
        return fromMin;
    }

    public void setFromMin(int fromMin) {
        this.fromMin = fromMin;
    }

    public int getToMin() {
        return toMin;
    }

    public void setToMin(int toMin) {
        this.toMin = toMin;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }
}
