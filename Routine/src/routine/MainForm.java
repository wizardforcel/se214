/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package routine;

import routine.utility.CalendarManager;
import routine.utility.WizardHTTP;
import routine.utility.RoutineManager;
import routine.baiduapi.BaiduAPI;
import routine.baiduapi.LocResult;
import routine.baiduapi.WeatherResult;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author Wizard
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm()
           throws AWTException, Exception
    {
        initComponents();
        this.setLocationRelativeTo(null);
        
        calendarTable.getTableHeader().setReorderingAllowed(false);
        calendarTable.getTableHeader().setResizingAllowed(false);
        
        routineForm = new RoutineForm();
        
        initTrayIcon();
    }

    private void initTrayIcon()
            throws AWTException, Exception
    {
        if(!SystemTray.isSupported()) 
            throw new Exception("对不起，该系统不支持托盘。");
        trayIcon = new TrayIcon(
                   new ImageIcon(MainForm.class.getResource("/rsrc/icon.jpg"))
                                         .getImage());
        trayIcon.setToolTip("日程管理");
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseListener() 
        {       
            @Override
            public void mouseClicked(MouseEvent e) 
            { trayIcon_Click(e); }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        SystemTray.getSystemTray().add(trayIcon);
    }
    
    private void showCalendar()
    {
        int year = (Integer)this.yearSpinner.getValue();
        int month = (Integer)this.monthSpinner.getValue();
        CalendarManager cm = new CalendarManager();
        cm.setDate(year, month);
        for(int row = 0; row < CalendarManager.HEIGHT; row++)
        {
           for(int col = 0; col < CalendarManager.WIDTH; col++)   
           {
              int val = cm.at(row, col);
              if(val == 0)
                  calendarTable.setValueAt(null, row, col);
              else
                  calendarTable.setValueAt(val, row, col);
           }
        }
    }
    
    private void trayIcon_Click(MouseEvent e)
    {
        if(e.getClickCount() != 2) return;
        setVisible(true);
    }
    
    private void doThread()
    {
        try {
        int lastmin = -1;
        Class.forName("org.sqlite.JDBC");
        Connection conn 
          = DriverManager.getConnection("jdbc:sqlite:" + Program.FILE_PATH);
        String sql = "SELECT title FROM note " + 
                     "WHERE ((alerttype=2 AND date=?) OR " +
                     "(alerttype=1 AND date=?)) AND alerttime=?";
        while(true)
        {
            try {
            System.out.println("Thread is running.");
            Calendar cal =  Calendar.getInstance();
            int min = cal.get(Calendar.MINUTE);
            if(min == lastmin)
            {
                Thread.sleep(10 * 1000);
                continue;
            }
            lastmin = min;
            PreparedStatement stmt = conn.prepareStatement(sql);
            int date = cal.get(Calendar.YEAR) * 10000 +
                       (cal.get(Calendar.MONTH) + 1) * 100 +
                       cal.get(Calendar.DATE);
            int time = cal.get(Calendar.HOUR_OF_DAY) * 100 + min;
            stmt.setInt(1, date);
            stmt.setInt(2, date + 1);
            stmt.setInt(3, time);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Date: " + String.valueOf(date) + 
                               "\nTime: " + String.valueOf(time));
            int rowcount = 0;
            while(rs.next())
            {
                rowcount++;
                String title = rs.getString(1);
                trayIcon.displayMessage("信息提示：", title + " 即将开始",
                                        java.awt.TrayIcon.MessageType.INFO);
            }
            System.out.println("Rowcount: " + String.valueOf(rowcount));
            Thread.sleep(10 * 1000);
            } catch(Exception ex) 
            {
                trayIcon.displayMessage("信息提示：", ex.getMessage(),
                                        java.awt.TrayIcon.MessageType.ERROR);
            }
        }
        } catch(Exception ex)
        { JOptionPane.showMessageDialog(null, ex.getMessage()); }
    }
    
    private void getWeather()
    {
        try
        {
            WizardHTTP wc = new WizardHTTP();
            wc.setDefHeader(false);
            LocResult lr = BaiduAPI.getLoc(wc, "");
            if(lr.errno != 0)
                throw new Exception(lr.errmsg);
            String city = lr.city;
            WeatherResult wr = BaiduAPI.getWeater(wc, city);
            if(wr.errno != 0)
                throw new Exception(wr.errmsg);
            String weather = wr.weather;
            String wind = wr.wind;
            String temperature = wr.temprature;
            this.weatherLabel.setText(city + " " + temperature + " " + 
                                      weather + " " + wind);
        }
        catch(Exception ex)
        {
            this.weatherLabel.setText("天气获取失败！");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        calendarScrollPane = new javax.swing.JScrollPane();
        calendarTable = new javax.swing.JTable();
        yearSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        monthSpinner = new javax.swing.JSpinner();
        exportButton = new javax.swing.JButton();
        weatherLabel = new javax.swing.JLabel();
        importButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("日程管理");
        setIconImage(new ImageIcon(MainForm.class.getResource("/rsrc/icon.jpg")).getImage());
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                form_Open(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                form_StateChange(evt);
            }
        });

        calendarTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        calendarTable.setRowSelectionAllowed(false);
        calendarTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                calendarTable_Click(evt);
            }
        });
        calendarScrollPane.setViewportView(calendarTable);

        yearSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yearSpinner_StateChange(evt);
            }
        });

        jLabel1.setText("年份");

        jLabel2.setText("月份");

        monthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                monthSpinner_StateChange(evt);
            }
        });

        exportButton.setText("导出");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButton_Click(evt);
            }
        });

        weatherLabel.setText("          ");

        importButton.setText("导入");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButton_Click(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(importButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportButton))
                    .addComponent(calendarScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(weatherLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(monthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(exportButton)
                        .addComponent(importButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calendarScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(weatherLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yearSpinner_StateChange(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yearSpinner_StateChange
        showCalendar();
    }//GEN-LAST:event_yearSpinner_StateChange

    private void monthSpinner_StateChange(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_monthSpinner_StateChange
        showCalendar();
    }//GEN-LAST:event_monthSpinner_StateChange

    private void calendarTable_Click(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendarTable_Click
        try
        {
            if(evt.getClickCount() == 1) return;
            int year = (Integer)this.yearSpinner.getValue();
            int month = (Integer)this.monthSpinner.getValue();
            int irow = calendarTable.getSelectedRow();
            int icol = calendarTable.getSelectedColumn();
            Integer day = (Integer)calendarTable.getValueAt(irow, icol);
            if(day != null)
            {
              routineForm.setDate(year, month, day);
              routineForm.setVisible(true);
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_calendarTable_Click

    private void form_StateChange(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_form_StateChange
        if(evt.getNewState() == JFrame.ICONIFIED)
        {
            this.setExtendedState(JFrame.NORMAL);
            this.setVisible(false);
            trayIcon.displayMessage("我在这里...", "双击我可以显示窗口。",
                                    java.awt.TrayIcon.MessageType.INFO);
        }
    }//GEN-LAST:event_form_StateChange

    private void form_Open(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_form_Open
        Calendar dt = Calendar.getInstance();
        int year = dt.get(Calendar.YEAR);
        int month = dt.get(Calendar.MONTH) + 1;
        yearSpinner.setModel(new SpinnerNumberModel(year, 1970, 2999, 1));
        monthSpinner.setModel(new SpinnerNumberModel(month, 1, 12, 1));
        showCalendar();
        
        new Thread(new Runnable()
        {
            @Override
            public void run() { doThread(); }
        }).start();
        
        new Thread(new Runnable()
        {
            @Override
            public void run() { getWeather(); }
        }).start();
    }//GEN-LAST:event_form_Open

    private void exportButton_Click(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButton_Click
        try
        {
            JFileChooser dlg = new JFileChooser();
            dlg.setFileFilter(new FileNameExtensionFilter("数据文件(*.dat)", "dat"));
            if(dlg.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
                return;
            String filename = dlg.getSelectedFile().getPath();
            RoutineManager.exportData(filename);
            JOptionPane.showMessageDialog(null, "导出成功！");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_exportButton_Click

    private void importButton_Click(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButton_Click
       try
       {
            JFileChooser dlg = new JFileChooser();
            dlg.setFileFilter(new FileNameExtensionFilter("数据文件(*.dat)", "dat"));
            if(dlg.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                return;
            String filename = dlg.getSelectedFile().getPath();
            boolean append = (JOptionPane.showConfirmDialog(null, "要删除之前的内容吗？",
                             "", JOptionPane.OK_CANCEL_OPTION)
                             == JOptionPane.CANCEL_OPTION);
            RoutineManager.importData(filename, append);
            routineForm.clear();
            JOptionPane.showMessageDialog(null, "导入成功！");
       }
       catch(Exception ex)
       {
           JOptionPane.showMessageDialog(null, ex.getMessage());
       }
    }//GEN-LAST:event_importButton_Click

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane calendarScrollPane;
    private javax.swing.JTable calendarTable;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSpinner monthSpinner;
    private javax.swing.JLabel weatherLabel;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables

    private RoutineForm routineForm;
    private TrayIcon trayIcon;
}