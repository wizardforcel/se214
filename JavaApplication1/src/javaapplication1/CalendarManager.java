/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author Wizard
 */
public class CalendarManager 
{
    private int[][] table;
    
    public CalendarManager()
    {
        Clear();
    }
    
    private void Clear()
    {
        table = new int[][]
        {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
        };
    }
    
    public void SetDate(int year, int month)
    {
        Clear();
        Calendar dt = Calendar.getInstance();
        dt.set(year, month - 1, 1);
        int dayofweek = dt.get(Calendar.DAY_OF_WEEK);
        int offset = (dayofweek == 1)? 6: dayofweek - 2;
        int total = dt.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 1; i <= total; i++)
        {
            int row = offset / 7,
                col = offset % 7;
            table[row][col] = i;
            offset++;
        }
    }
    
    public void Show(JTable jtb)
    {
        for(int row = 0; row < 6; row++)
        {
           for(int col = 0; col < 7; col++)   
           {
              if(table[row][col] == 0)
                  jtb.setValueAt(null, row, col);
              else
                  jtb.setValueAt(table[row][col], row, col);
           }
        }
    }
}
