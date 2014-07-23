/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.wizard.myapplication.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class RoutineManager implements Serializable
{
    private int Year;
    private int Month;
    private int Day;
    private ArrayList<NoteRow> Data
            = new ArrayList<NoteRow>();
    private Context Context;

    public void SetContext(Context context)
    {
        Context = context;
    }

    public int Size()
    {
        return Data.size();
    }
    
    /*public static void Import(String filename, boolean append)
           throws ClassNotFoundException, IOException
    {
        SQLiteDatabase db = new DatabaseHelper(Context).getReadableDatabase();

        ArrayList<NoteRow> data = new ArrayList<NoteRow>();
        if(append)
            data.addAll(LoadAll());
        else
        {
            String sql = "DELETE FROM note";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        
        StreamReader sr = new StreamReader(filename);
        String[] lines = sr.ReadToEnd().split(System.getProperty("line.separator"));
        sr.close();
        
        for(String line : lines)
        {
            String[] tmp = line.split(" ");
            if(tmp.length < 7) continue;
            NoteRow note = new NoteRow();
            note.SetDate(Integer.parseInt(tmp[0]));
            note.SetStartTime(Integer.parseInt(tmp[1]));
            note.SetEndTime(Integer.parseInt(tmp[2]));
            note.SetTitle(Base64Utility.Base64Deco(tmp[3]));
            note.SetComment(Base64Utility.Base64Deco(tmp[4]));
            note.SetAlertType(Integer.parseInt(tmp[5]));
            note.SetAlertTime(Integer.parseInt(tmp[6]));
            if(data.indexOf(note) == -1)
            {
                String sql = "INSERT INTO note (date, starttime, endtime, title, " +
                             "comment, alerttype, alerttime) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, note.GetDate());
                stmt.setInt(2, note.GetStartTime());
                stmt.setInt(3, note.GetEndTime());
                stmt.setString(4, note.GetTitle());
                stmt.setString(5, note.GetComment());
                stmt.setInt(6, note.GetAlertType());
                stmt.setInt(7, note.GetAlertTime());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                note.SetID(rs.getInt(1));
                data.add(note);
            }
        }
        
        conn.close();
    }
    
    private static ArrayList<NoteRow> LoadAll()
            throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "SELECT * FROM note";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<NoteRow> data = new ArrayList<NoteRow>();
        while(rs.next())
        {
            NoteRow row = new NoteRow();
            row.SetID(rs.getInt(1));
            row.SetDate(rs.getInt(2));
            row.SetStartTime(rs.getInt(3));
            row.SetEndTime(rs.getInt(4));
            row.SetTitle(rs.getString(5));
            row.SetComment(rs.getString(6));
            row.SetAlertType(rs.getInt(7));
            row.SetAlertTime(rs.getInt(8));
            data.add(row);
        }
        conn.close();
        return data;
    }*/
    
    public void Load(int year, int month, int day)
    {
        if(Year == year && Month == month && Day == day)
            return;
        SQLiteDatabase db = new DatabaseHelper(Context).getReadableDatabase();
        String sql = "SELECT * FROM note WHERE date=?";
        int date = year * 10000 + month * 100 + day;
        Cursor cur = db.rawQuery(sql, new String[]{String.valueOf(date)});
        Data.clear();
        while(cur.moveToNext())
        {
            NoteRow row = new NoteRow();
            row.SetID(cur.getInt(1));
            row.SetDate(cur.getInt(2));
            row.SetStartTime(cur.getInt(3));
            row.SetEndTime(cur.getInt(4));
            row.SetTitle(cur.getString(5));
            row.SetComment(cur.getString(6));
            row.SetAlertType(cur.getInt(7));
            row.SetAlertTime(cur.getInt(8));
            Data.add(row);
        }
        cur.close();
        Year = year;
        Month = month;
        Day = day;
    }
    
    public NoteRow Get(int index)
    {
        return Data.get(index);
    }
    
    public static String TimeConvert(int time)
    {
        int min = time % 100;
        int hr = time / 100;
        return String.format("%02d:%02d", hr, min);
    }
    
    public void UpdateAt(int index, NoteRow note)
           throws Exception
    {
        NoteRow oldnote = Data.get(index);
        if(!oldnote.equals(note) && Data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        SQLiteDatabase db = new DatabaseHelper(Context).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("starttime", note.GetStartTime());
        values.put("endtime", note.GetEndTime());
        values.put("title", note.GetTitle());
        values.put("comment", note.GetComment());
        values.put("alerttype", note.GetAlertType());
        values.put("alerttime", note.GetAlertTime());
        db.update("note", values, "id=?", new String[]{String.valueOf(oldnote.GetID())});
        Data.set(index, note);
    }
  
    public void RemoveAt(int index)
           throws Exception
    {
        NoteRow note = Data.get(index);
        SQLiteDatabase db = new DatabaseHelper(Context).getReadableDatabase();
        db.delete("note", "id=?", new String[]{String.valueOf(note.GetID())});
        Data.remove(note);
    }
    
    public void Add(NoteRow note)
           throws Exception
    {
        if(Data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        SQLiteDatabase db = new DatabaseHelper(Context).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", note.GetDate());
        values.put("starttime", note.GetStartTime());
        values.put("endtime", note.GetEndTime());
        values.put("title", note.GetTitle());
        values.put("comment", note.GetComment());
        values.put("alerttype", note.GetAlertType());
        values.put("alerttime", note.GetAlertTime());
        long rowid = db.insert("note", null, values);
        note.SetID((int)rowid);
        Data.add(note);
    }
    
    /*public static void Export(String filename)
           throws ClassNotFoundException, SQLException,
                  FileNotFoundException, UnsupportedEncodingException, IOException
    {
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        //id date starttime endtime title comment alerttype alerttime
        String sql = "SELECT * FROM note";
        Statement stmt= conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        StreamWriter sw = new StreamWriter(filename, false);
        while(rs.next())
        {
            int date = rs.getInt(2);
            int starttime = rs.getInt(3);
            int endtime = rs.getInt(4);
            String title = Base64Utility.Base64Enco(rs.getString(5));
            String comment = Base64Utility.Base64Enco(rs.getString(6));
            comment = comment.equals("")? "-": comment;
            int alerttype = rs.getInt(7);
            int alerttime = rs.getInt(8);
            String line = String.format("%d %d %d %s %s %d %d",
                          date, starttime, endtime, title, comment,
                          alerttype, alerttime);
            sw.WriteLine(line);
        }
        sw.close();
        conn.close();
    }*/
    
    public void Clear()
    {
        Year = Month = Day = 0;
    }
}
