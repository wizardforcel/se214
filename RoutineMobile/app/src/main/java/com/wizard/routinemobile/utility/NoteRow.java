/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wizard.routinemobile.utility;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Wizard
 */
public class NoteRow implements Serializable
{   
    private int id;
    private int date;
    private int startTime;
    private int endTime;
    private String title = "";
    private String comment = "";
    private int alertType;
    private int alertTime;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStartTime() { return startTime; }
    public void setStartTime(int starttime) { startTime = starttime; }
    public int getDate() { return date; }
    public void setDate(int date) { this.date = date; }
    public int getEndTime() { return endTime; }
    public void setEndTime(int endtime) { endTime = endtime; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getAlertType() { return alertType; }
    public void setAlertType(int alerttype) { alertType = alerttype; }
    public int getAlertTime() { return alertTime; }
    public void setAlertTime(int alerttime) { alertTime = alerttime; }
    
    @Override
    public NoteRow clone()
    {
        NoteRow newnote = new NoteRow();
        newnote.setId(id);
        newnote.setDate(date);
        newnote.setStartTime(startTime);
        newnote.setEndTime(endTime);
        newnote.setTitle(title);
        newnote.setComment(comment);
        newnote.setAlertTime(alertTime);
        newnote.setAlertType(alertType);
        return newnote;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof NoteRow))
            return false;
        NoteRow that = (NoteRow)o;
        return (this.getDate() == that.getDate() &&
                this.getStartTime() == that.getStartTime() &&
                this.getEndTime() == that.getEndTime() &&
                this.getTitle().equals(that.getTitle()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.date;
        hash = 41 * hash + this.startTime;
        hash = 41 * hash + this.endTime;
        hash = 41 * hash + Objects.hashCode(this.title);
        return hash;
    }
}
