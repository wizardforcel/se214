/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.wizard.myapplication.utility;

import java.io.*;

/**
 *
 * @author Wizard
 */
public class StreamWriter extends BufferedWriter
{
    //Writer 构造
    public StreamWriter(Writer out) 
    {
        super(out);
    }
    
    //Stream 构造
    public StreamWriter(OutputStream out, String charsetName)
           throws UnsupportedEncodingException 
    {
        super(new OutputStreamWriter(out, charsetName));
    }
    public StreamWriter(OutputStream out)
    {
        super(new OutputStreamWriter(out));
    }
    
    // Filename 构造
    public StreamWriter(String fileName, boolean append, String charsetName)
           throws UnsupportedEncodingException, FileNotFoundException 
    {
        this(new FileOutputStream(fileName, append), charsetName);
    }
    public StreamWriter(String fileName, boolean append)
           throws FileNotFoundException
    {
        this(new FileOutputStream(fileName, append));
    }
    public StreamWriter(String fileName)
           throws FileNotFoundException
    {
        this(fileName, false);
    }
    
    //File 构造
    public StreamWriter(File file, boolean append, String charsetName)
           throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileOutputStream(file, append), charsetName);
    }
    public StreamWriter(File file, boolean append)
           throws FileNotFoundException
    {
        this(new FileOutputStream(file, append));
    }
    public StreamWriter(File file)
           throws FileNotFoundException
    {
        this(file, false);
    }
    
    //WriteLine
    public void WriteLine(String str)
           throws IOException
    {
        this.write(str);
        this.newLine();
    }
}
