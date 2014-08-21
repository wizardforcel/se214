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
public class StreamReader extends BufferedReader
{
    //Reader 构造
    public StreamReader(Reader in)
    {
        super(in);
    }
    
    //Stream 构造
    public StreamReader(InputStream in, String charsetName)
           throws UnsupportedEncodingException
    {
        super(new InputStreamReader(in, charsetName));
    }
    public StreamReader(InputStream in)
    {
        super(new InputStreamReader(in));
    }
    
    //Filename 构造
    public StreamReader(String fileName, String charset)
           throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileInputStream(fileName), charset);
    }
    public StreamReader(String fileName)
           throws FileNotFoundException
    {
        this(new FileInputStream(fileName));
    }
    
    //File 构造
    public StreamReader(File file, String charset)
           throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileInputStream(file), charset);
    }
    public StreamReader(File file)
           throws FileNotFoundException
    {
        this(new FileInputStream(file));
    }
    
    //读到头
    public String readToEnd()
           throws IOException
    {
        StringBuilder sb = new StringBuilder();
        while(true)
        {
            String line = this.readLine();
            if(line == null) break;
            sb.append(line).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
