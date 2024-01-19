package com.example.medicare;

public class RecyclerItem {
    private final String service;
    private final String formattedDate;
    private final String formattedTime;
    private final String address;

    public RecyclerItem(String service, String formattedDate, String formattedTime, String address) {
        this.service = service;
        this.formattedDate = formattedDate;
        this.formattedTime = formattedTime;
        this.address = address;
    }

    public String getService() {
        return service;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public String getAddress() {
        return address;
    }
}
