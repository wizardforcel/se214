/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package routine.utility;

import routine.Program;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class RoutineManager 
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
    
    public static void importData(String filename, boolean append)
           throws SQLException, ClassNotFoundException, IOException
    {
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        
        ArrayList<NoteRow> data = new ArrayList<NoteRow>();
        if(append)
            data.addAll(loadAll());
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
    }
    
    public void load(int year, int month, int day)
           throws SQLException, ClassNotFoundException
    {
        if(this.year == year && this.month == month && this.day == day)
            return;
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "SELECT * FROM note WHERE date=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int date = year * 10000 + month * 100 + day;
        stmt.setInt(1, date);
        ResultSet rs = stmt.executeQuery();
        data.clear();
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
    
    public void updateAt(int index, NoteRow note)
           throws ClassNotFoundException, SQLException, Exception
    {
        NoteRow oldnote = data.get(index);
        if(!oldnote.equals(note) && data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "UPDATE note SET starttime=?, " +
                     "endtime=?, title=?, comment=?, alerttype=?, " + 
                     "alerttime=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, note.getStartTime());
        stmt.setInt(2, note.getEndTime());
        stmt.setString(3, note.getTitle());
        stmt.setString(4, note.getComment());
        stmt.setInt(5, note.getAlertType());
        stmt.setInt(6, note.getAlertTime());
        stmt.setInt(7, oldnote.getId());
        stmt.executeUpdate();
        data.set(index, note);
    }
  
    public void removeAt(int index)
           throws ClassNotFoundException, SQLException, Exception
    {
        NoteRow note = data.get(index);
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "DELETE FROM note WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, note.getId());
        stmt.executeUpdate();
        data.remove(note);
    }
    
    public void add(NoteRow note)
           throws ClassNotFoundException, SQLException, Exception
    {
        if(data.indexOf(note) != -1)
            throw new Exception("该计划已存在。");
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
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
    
    public static void exportData(String filename) 
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
    }
    
    public void clear()
    {
        year = month = day = 0;
    }
}
