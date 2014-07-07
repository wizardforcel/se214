/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.util.*;
import javax.swing.*;
import java.sql.*;

/**
 *
 * @author Wizard
 */
public class Program 
{
    public static final String FILE_PATH = "C:/data.db";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
          String style = UIManager.getSystemLookAndFeelClassName();
          UIManager.setLookAndFeel(style);

          CreateDB();
          
          MainForm mf = new MainForm();
          mf.setVisible(true);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private static void CreateDB() 
            throws SQLException, ClassNotFoundException
    {
        //String path = System.getProperty("user.dir");
        Class.forName("org.sqlite.JDBC");
        Connection conn 
           = DriverManager.getConnection("jdbc:sqlite:" + FILE_PATH);
        String sql = "CREATE TABLE IF NOT EXISTS note (" + 
                     "id INTEGER PRIMARY KEY," + 
                     "date INT," + 
                     "starttime INT," + 
                     "endtime INT," + 
                     "title TEXT," + 
                     "comment TEXT," + 
                     "alerttype INT," + 
                     "alerttime INT )";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        conn.close();
    }
    
}
