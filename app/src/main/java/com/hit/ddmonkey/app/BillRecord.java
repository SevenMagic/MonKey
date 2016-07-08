package com.hit.ddmonkey.app;


public class BillRecord {
    private String IO;
    private String category;
    private String note;
    private String date;
    private double money;
    private String dateId;

    public BillRecord(String IO,String category,String note,String date,double money){
        this.IO = IO;
        this.category = category;
        this.note = note;
        this.date = date;
        this.money = money;
    }

    public  BillRecord(){}

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }
    public String getIO() {
        return IO;
    }

    public void setIO(String IO) {
        this.IO = IO;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
