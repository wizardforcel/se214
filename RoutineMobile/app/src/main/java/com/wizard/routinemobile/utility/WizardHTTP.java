/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wizard.routinemobile.utility;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class WizardHTTP 
{
    private HashMap<String, String> header
    = new HashMap<String, String>();
    private Map<String, List<String>> retHeader;
    private Proxy proxy;
    private int timeout = 4000;
    private String charset = "GBK";
    
    public String getHeader(String name)
    {
        return header.get(name);
    }
    
    public void setHeader(String name, String value)
    {
        header.put(name, value);
    }
    
    public void delHeader(String name)
    {
        header.remove(name);
    }
    public void clearHeader()
    {
        header.clear();
    }
    public void setDefHeader(boolean mobile)
    {
        header.put("Accept", "*/*");
        header.put("Accept-Language", "zh-cn");
        if(mobile)
            header.put("User-Agent", "Dalvik/1.1.0 (Linux; U; Android 2.1; sdk Build/ERD79)");
        else
            header.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Cache-Control", "no-cache");
    }
    
    public String getRetHeader(String name)
    {
        return retHeader.get(name).toString();
    }
    
    public String getCharset()
    {
        return charset;
    }
    
    public void setCharset(String cs)
    {
        charset = cs;
    }
    
    public Proxy getProxy()
    {
        return proxy;
    }
    
    public void setProxy(Proxy proxy)
    {
        this.proxy = proxy;
    }
    
    private String httpSubmit(String method, String tar, String postdata)
            throws IOException
    {
        URL url = new URL(tar);
        URLConnection conn;
        if(proxy == null)
            conn = url.openConnection();
        else
            conn = url.openConnection(proxy);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        for(String k : header.keySet())
            conn.setRequestProperty(k, header.get(k));
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
        retHeader = conn.getHeaderFields();
        StreamReader sr
          = new StreamReader(conn.getInputStream(), charset);
        String retstr = sr.readToEnd();
        sr.close();
        return retstr;
    }
    
    public String httpGet(String tar)
           throws IOException
    {
        return httpSubmit("GET", tar, "");
    }
    
    public String httpPost(String tar, String data)
           throws IOException
    {
        return httpSubmit("POST", tar, data);
    }
}
