package com.example.easydesign.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class Userinfo {
	public static boolean saveuserinfo(String username, String password) {
		try {
			
			File file = new File("/data/data/com.example.easydesign/info.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write((username +"##" + password).getBytes());
			fos.close();

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static Map<String, String> getuserinfo() {
		
		try {
			File file = new File("/data/data/com.example.easydesign/info.txt");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str = br.readLine();
			br.close();
			fis.close();
			String[] infos = str.split("##");
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", infos[0]);
			map.put("password", infos[1]);

			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map<String, String> map = new HashMap<String, String>();
			map.put("password", "null");
			return map;
		}
	}

}