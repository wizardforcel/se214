/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.wizard.myapplication.utility;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class WizardHTTP 
{
    private HashMap<String, String> Header
    = new HashMap<String, String>();
    private Map<String, List<String>> RetHeader;
    private Proxy Proxy;
    private int Timeout = 4000;
    private String Charset = "GBK";
    
    public String GetHeader(String name)
    {
        return Header.get(name);
    }
    
    public void SetHeader(String name, String value)
    {
        Header.put(name, value);
    }
    
    public void DelHeader(String name)
    {
        Header.remove(name);
    }
    public void ClearHeader()
    {
        Header.clear();
    }
    public void SetDefHeader(boolean mobile)
    {
        Header.put("Accept", "*/*");
        Header.put("Accept-Language", "zh-cn");
        if(mobile)
            Header.put("User-Agent", "Dalvik/1.1.0 (Linux; U; Android 2.1; sdk Build/ERD79)");
        else
            Header.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        Header.put("Content-Type", "application/x-www-form-urlencoded");
        Header.put("Cache-Control", "no-cache");
    }
    
    public String GetRetHeader(String name)
    {
        return RetHeader.get(name).toString();
    }
    
    public String GetCharset()
    {
        return Charset;
    }
    
    public void SetCharset(String cs)
    {
        Charset = cs;
    }
    
    public Proxy GetProxy()
    {
        return Proxy;
    }
    
    public void SetProxy(Proxy proxy)
    {
        Proxy = proxy;
    }
    
    private String HTTPSubmit(String method, String tar, String postdata)
            throws IOException
    {
        URL url = new URL(tar);
        URLConnection conn;
        if(Proxy == null)
            conn = url.openConnection();
        else
            conn = url.openConnection(Proxy);
        conn.setConnectTimeout(Timeout);
        conn.setReadTimeout(Timeout);
        for(String k : Header.keySet())
            conn.setRequestProperty(k, Header.get(k));
        if(method.equals("POST"))
        {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            StreamWriter sw
              = new StreamWriter(conn.getOutputStream());
            sw.write(postdata);
            sw.close();
        }
        conn.connect();
        RetHeader = conn.getHeaderFields();
        StreamReader sr
          = new StreamReader(conn.getInputStream(), Charset);
        String retstr = sr.ReadToEnd();
        sr.close();
        return retstr;
    }
    
    public String HTTPGet(String tar)
           throws IOException
    {
        return HTTPSubmit("GET", tar, "");
    }
    
    public String HTTPPost(String tar, String data)
           throws IOException
    {
        return HTTPSubmit("POST", tar, data);
    }
}
