/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

/**
 *
 * @author Wizard
 */
public class WeatherResult 
{
    public int errno;
    public String errmsg = "";
    public String weather = "";
    public String wind = "";
    public String temprature = "";
    
    public WeatherResult() { }
    public WeatherResult(int errno, String errmsg, String weather, 
                           String wind, String temprature)
    {
        this.errno = errno;
        this.errmsg = errmsg;
        this.weather = weather;
        this.wind = wind;
        this.temprature = temprature;
    }
}
