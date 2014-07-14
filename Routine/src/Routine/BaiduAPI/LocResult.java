/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Routine.BaiduAPI;

/**
 *
 * @author Wizard
 */
public class LocResult 
{
    public int errno;
    public String errmsg = "";
    public String province = "";
    public String city = "";
    
    public LocResult() { }
    public LocResult(int errno, String errmsg, String province, String city)
    {
        this.errno = errno;
        this.errmsg = errmsg;
        this.province = province;
        this.city = city;
    }
}
