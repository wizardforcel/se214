/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.sql.*;
import javax.swing.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wizard
 */
public class RoutineManager 
{
    private int Year;
    private int Month;
    private int Day;
    private ArrayList<NoteRow> Data
            = new ArrayList<NoteRow>();
    
    public void Load(int year, int month, int day)
           throws SQLException, ClassNotFoundException
    {
        if(Year == year && Month == month && Day == day)
            return;
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "SELECT * FROM note WHERE date=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int date = year * 10000 + month * 100 + day;
        stmt.setInt(1, date);
        ResultSet rs = stmt.executeQuery();
        Data.clear();
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
            Data.add(row);
        }
        conn.close();
    }
    
    public void Show(JTable jtb)
    {
        DefaultTableModel model = (DefaultTableModel)jtb.getModel();
        model.setRowCount(0);
        for(NoteRow row : Data)
        {
            Object[] arr = new Object[6];
            arr[0] = row.GetTitle();
            arr[1] = TimeConvert(row.GetStartTime());
            arr[2] = TimeConvert(row.GetEndTime());
            int itype = row.GetAlertType();
            String strtype;
            if(itype == 1)
                strtype = "当天";
            else if(itype == 2)
                strtype = "提前一天";
            else
                strtype = "无";
            arr[3] = strtype;
            arr[4] =TimeConvert(row.GetAlertTime());
            arr[5] = row.GetComment();
            model.addRow(arr);
        }
    }
    
    public NoteRow Get(int index)
    {
        return Data.get(index);
    }
    
    private static String TimeConvert(int time)
    {
        int sec = time % 100;
        time /= 100;
        int min = time % 100;
        int hr = time / 100;
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }
}
