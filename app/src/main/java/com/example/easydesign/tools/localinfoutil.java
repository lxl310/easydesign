package com.example.easydesign.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class localinfoutil {
	public static boolean saveinfo(String type,String imgnames){
		try {
			File file=new File("/data/data/com.example.easydesign/"+type+".txt");
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(imgnames.getBytes());
			fos.close();
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
		
	public static String getinfo(String type) {
			File file=new File("/data/data/com.example.easydesign/"+type+".txt");
			try {
				FileInputStream fis=new FileInputStream(file);
				String str = istostr(fis);
				return str;
			} catch (Exception e) {
				e.printStackTrace();
				return "dontexit";
			}
		}

	//////////////
	public static String istostr(InputStream in)
	{

		String str = "";
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			StringBuffer sb = new StringBuffer();

			while ((str = reader.readLine()) != null)
			{
				sb.append(str).append("\n");
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return str;
	}

}
