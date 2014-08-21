/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package routine.baiduapi;

import routine.utility.WizardHTTP;
import java.net.*;
import java.io.*;
import net.sf.json.JSONObject;

/**
 *
 * @author Wizard
 */
public class BaiduAPI 
{
    public static final String API_KEY = "D76279e349e952599c0ee9f283a85a57";
    
    public static LocResult getLoc(WizardHTTP wc, String ip)
           throws MalformedURLException, IOException
    {
        String retstr = wc.httpGet("http://api.map.baidu.com/location/ip" + 
                                   "?ak=" + BaiduAPI.API_KEY + "&ip=" + ip);
        JSONObject json = JSONObject.fromObject(retstr);
        int errno = json.getInt("status");
        if(errno != 0)
            return new LocResult(errno, json.getString("message"), "");
        String city = json.getJSONObject("content").getString("address");
        return new LocResult(0, "", city);
    }
    
    public static WeatherResult getWeater(WizardHTTP wc, String city)
           throws IOException
    {
        wc.setCharset("utf-8");
        String retstr = wc.httpGet("http://api.map.baidu.com/telematics/v3/weather" + 
                                   "?location=" + URLEncoder.encode(city, "utf-8") + 
                                   "&output=json&ak="+ BaiduAPI.API_KEY);
        JSONObject json = JSONObject.fromObject(retstr);
        int errno = json.getInt("error");
        if(errno != 0)
            return new WeatherResult(errno, json.getString("status"), "", "", "");
        JSONObject weatherdata = json.getJSONArray("results").getJSONObject(0)
                                     .getJSONArray("weather_data")
                                     .getJSONObject(0);
        String weather = weatherdata.getString("weather");
        String wind = weatherdata.getString("wind");
        String temperature = weatherdata.getString("temperature");
        return new WeatherResult(0, "", weather, wind, temperature);
    }
    
    
}
