/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.util.Objects;

/**
 *
 * @author Wizard
 */
public class NoteRow 
{   
    private int ID;
    private int Date;
    private int StartTime;
    private int EndTime;
    private String Title = "";
    private String Comment = "";
    private int AlertType;
    private int AlertTime;
    
    public int GetID() { return ID; }
    public void SetID(int id) { ID = id; }
    public int GetStartTime() { return StartTime; }
    public void SetStartTime(int starttime) { StartTime = starttime; }
    public int GetDate() { return Date; }
    public void SetDate(int date) { Date = date; }
    public int GetEndTime() { return EndTime; }
    public void SetEndTime(int endtime) { EndTime = endtime; }
    public String GetTitle() { return Title; }
    public void SetTitle(String title) { Title = title; }
    public String GetComment() { return Comment; }
    public void SetComment(String comment) { Comment = comment; }
    public int GetAlertType() { return AlertType; }
    public void SetAlertType(int alerttype) { AlertType = alerttype; }
    public int GetAlertTime() { return AlertTime; }
    public void SetAlertTime(int alerttime) { AlertTime = alerttime; }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof NoteRow))
            return false;
        NoteRow that = (NoteRow)o;
        return (this.GetDate() == that.GetDate() &&
                this.GetStartTime() == that.GetStartTime() &&
                this.GetEndTime() == that.GetEndTime() &&
                this.GetTitle().equals(that.GetTitle()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.Date;
        hash = 41 * hash + this.StartTime;
        hash = 41 * hash + this.EndTime;
        hash = 41 * hash + Objects.hashCode(this.Title);
        return hash;
    }
}
