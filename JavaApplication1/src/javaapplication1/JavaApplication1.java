/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import javax.swing.*;

/**
 *
 * @author Wizard
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
          String style = UIManager.getSystemLookAndFeelClassName();
          UIManager.setLookAndFeel(style);
          MainForm mf = new MainForm();
          mf.setVisible(true);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
}
