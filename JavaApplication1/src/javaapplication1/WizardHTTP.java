/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

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
        URLConnection  conn = url.openConnection(Proxy);
        conn.setConnectTimeout(Timeout);
        conn.setReadTimeout(Timeout);
        for(String k : Header.keySet())
            conn.setRequestProperty(k, Header.get(k));
        if(method.equals("POST"))
        {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStreamWriter out
              = new OutputStreamWriter(conn.getOutputStream());
            out.write(postdata);
            out.close();
        }
        conn.connect();
        RetHeader = conn.getHeaderFields();
        BufferedReader in
          = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), Charset));
        StringBuffer sb = new StringBuffer();
        while(true)
        {
            String line = in.readLine();
            if(line == null) break;
            sb.append(line).append("\n");
        }
        in.close();
        return sb.toString();
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
