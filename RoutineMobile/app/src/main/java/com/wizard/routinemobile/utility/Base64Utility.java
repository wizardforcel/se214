/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wizard.routinemobile.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import android.util.Base64;

/**
 *
 * @author Wizard
 */
public class Base64Utility 
{
    public static String base64Enco(String text)
           throws UnsupportedEncodingException
    {
        return Base64.encodeToString(text.getBytes("UTF-8"), Base64.DEFAULT);
        //return Convert.ToBase64String(Encoding.UTF8.GetBytes(text));
    }
    public static String base64Deco(String text)
           throws IOException
    {
        return new String(Base64.decode(text, Base64.DEFAULT), "UTF-8");
        //return Encoding.UTF8.GetString(Convert.FromBase64String(text));
    }
}
