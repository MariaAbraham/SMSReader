package com.manam.andorid.mobeeverification.smsreader;

import android.support.annotation.NonNull;

/**
 * Created by Maria Abraham on 21/01/19.
 */
public class SMSModel {
    private String text;
    private double date;
    private String sender;
    private String fullText;
    private int type;

    public SMSModel(String fullText) {
        this.fullText = fullText;
    }

    public SMSModel(String fullText, double date) {
        this.fullText = fullText;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}