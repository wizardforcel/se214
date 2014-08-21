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

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class RoutineManager implements Serializable
{
    private int year;
    private int month;
    private int day;
    private ArrayList<NoteRow> data
            = new ArrayList<NoteRow>();

    public int size()
    {
        return data.size();
    }
    
    /*public static void importData(String filename, boolean append)
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
        String[] lines = sr.readToEnd().split(System.getProperty("line.separator"));
        sr.close();
        
        for(String line : lines)
        {
            String[] tmp = line.split(" ");
            if(tmp.length < 7) continue;
            NoteRow note = new NoteRow();
            note.setDate(Integer.parseInt(tmp[0]));
            note.setStartTime(Integer.parseInt(tmp[1]));
            note.setEndTime(Integer.parseInt(tmp[2]));
            note.setTitle(Base64Utility.base64Deco(tmp[3]));
            note.setComment(Base64Utility.base64Deco(tmp[4]));
            note.setAlertType(Integer.parseInt(tmp[5]));
            note.setAlertTime(Integer.parseInt(tmp[6]));
            if(data.indexOf(note) == -1)
            {
                String sql = "INSERT INTO note (date, starttime, endtime, title, " +
                             "comment, alerttype, alerttime) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, note.getDate());
                stmt.setInt(2, note.getStartTime());
                stmt.setInt(3, note.getEndTime());
                stmt.setString(4, note.getTitle());
                stmt.setString(5, note.getComment());
                stmt.setInt(6, note.getAlertType());
                stmt.setInt(7, note.getAlertTime());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                note.setId(rs.getInt(1));
                data.add(note);
            }
        }
        
        conn.close();
    }
    
    private static ArrayList<NoteRow> loadAll()
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
            row.setId(rs.getInt(1));
            row.setDate(rs.getInt(2));
            row.setStartTime(rs.getInt(3));
            row.setEndTime(rs.getInt(4));
            row.setTitle(rs.getString(5));
            row.setComment(rs.getString(6));
            row.setAlertType(rs.getInt(7));
            row.setAlertTime(rs.getInt(8));
            data.add(row);
        }
        conn.close();
        return data;
    }*/
    
    public void load(Context context, int year, int month, int day)
    {
        if(this.year == year && this.month == month && this.day == day)
            return;
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String sql = "SELECT * FROM note WHERE date=?";
        int date = year * 10000 + month * 100 + day;
        Cursor cur = db.rawQuery(sql, new String[]{String.valueOf(date)});
        data.clear();
        while(cur.moveToNext())
        {
            NoteRow row = new NoteRow();
            row.setId(cur.getInt(cur.getColumnIndex("id")));
            row.setDate(cur.getInt(cur.getColumnIndex("date")));
            row.setStartTime(cur.getInt(cur.getColumnIndex("starttime")));
            row.setEndTime(cur.getInt(cur.getColumnIndex("endtime")));
            row.setTitle(cur.getString(cur.getColumnIndex("title")));
            row.setComment(cur.getString(cur.getColumnIndex("comment")));
            row.setAlertType(cur.getInt(cur.getColumnIndex("alerttype")));
            row.setAlertTime(cur.getInt(cur.getColumnIndex("alerttime")));
            data.add(row);
        }
        cur.close();
        db.close();
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    public NoteRow get(int index)
    {
        return data.get(index);
    }
    
    public static String timeConvert(int time)
    {
        int min = time % 100;
        int hr = time / 100;
        return String.format("%02d:%02d", hr, min);
    }
    
    public void updateAt(Context context, int index, NoteRow note)
           throws Exception
    {
        NoteRow oldnote = data.get(index);
        if(!oldnote.equals(note) && data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("starttime", note.getStartTime());
        values.put("endtime", note.getEndTime());
        values.put("title", note.getTitle());
        values.put("comment", note.getComment());
        values.put("alerttype", note.getAlertType());
        values.put("alerttime", note.getAlertTime());
        db.update("note", values, "id=?", new String[]{String.valueOf(oldnote.getId())});
        db.close();
        data.set(index, note);
    }
  
    public void removeAt(Context context, int index)
           throws Exception
    {
        NoteRow note = data.get(index);
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        db.delete("note", "id=?", new String[]{String.valueOf(note.getId())});
        db.close();
        data.remove(note);
    }
    
    public void add(Context context, NoteRow note)
           throws Exception
    {
        if(data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", note.getDate());
        values.put("starttime", note.getStartTime());
        values.put("endtime", note.getEndTime());
        values.put("title", note.getTitle());
        values.put("comment", note.getComment());
        values.put("alerttype", note.getAlertType());
        values.put("alerttime", note.getAlertTime());
        long rowid = db.insert("note", null, values);
        db.close();
        note.setId((int) rowid);
        data.add(note);
    }
    
    /*public static void exportData(String filename)
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
            String title = Base64Utility.base64Enco(rs.getString(5));
            String comment = Base64Utility.base64Enco(rs.getString(6));
            comment = comment.equals("")? "-": comment;
            int alerttype = rs.getInt(7);
            int alerttime = rs.getInt(8);
            String line = String.format("%d %d %d %s %s %d %d",
                          date, starttime, endtime, title, comment,
                          alerttype, alerttime);
            sw.writeln(line);
        }
        sw.close();
        conn.close();
    }*/
    
    public void clear()
    {
        year = month = day = 0;
    }
}
