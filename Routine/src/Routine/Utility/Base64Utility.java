/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Routine.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import sun.misc.*;

/**
 *
 * @author Wizard
 */
public class Base64Utility 
{
    public static String Base64Enco(String text) 
           throws UnsupportedEncodingException
    {
        return new BASE64Encoder().encode(text.getBytes("UTF-8"));
        //return Convert.ToBase64String(Encoding.UTF8.GetBytes(text));
    }
    public static String Base64Deco(String text) 
           throws UnsupportedEncodingException, IOException
    {
        return new String(new BASE64Decoder().decodeBuffer(text), "UTF-8");
        //return Encoding.UTF8.GetString(Convert.FromBase64String(text));
    }
}
