package com.pureTec.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AppService {

	private static String Url="http://localhost:8080/MinLanApp/user/deletetoken?id=";
	
	public static void deletetoken (int id) {  
        try {  
            URL urlObject = new URL(Url+id);  
            URLConnection uc = urlObject.openConnection();  
            uc.getInputStream();  


        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
    }  

}
