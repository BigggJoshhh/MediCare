package com.example.medicare;

public class RecyclerItem {
    private String type;
    private String date;
    private String time;
    private String address;

    public RecyclerItem(String type, String date, String time, String address) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }
}
