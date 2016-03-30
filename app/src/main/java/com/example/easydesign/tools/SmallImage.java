package com.example.easydesign.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

public class SmallImage {
	public String smallimagepath;
	public Bitmap bitmap;
	
	
	public SmallImage(String imagepath) {
		try {
			
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;// 不读入内存，至获取长宽
			Bitmap bm = BitmapFactory.decodeFile(imagepath, option);
			int oldwidth = option.outWidth;
			int oldheight = option.outHeight;
			option.inJustDecodeBounds = false;// 以下decode读入内存中
			option.inSampleSize = 1;// 分辨率设为1/n

			if (oldwidth > 700 || oldheight > 700) {
				if (oldwidth >= oldheight)

					option.inSampleSize = (int) (oldwidth / 700);
				else
					option.inSampleSize = (int) (oldheight / 700);
			}
			bm = BitmapFactory.decodeFile(imagepath, option);
			//
			FileOutputStream out = new FileOutputStream(new File("/data/data/com.example.easydesign/1.jpg"));
			bm.compress(Bitmap.CompressFormat.JPEG, 40, out);
			out.flush();
			out.close();
		
			//////////////////////////////////////////////////////////////////
			this.smallimagepath = "/data/data/com.example.easydesign/1.jpg";
			
			this.bitmap = bm;
		} catch (Exception e) {
			this.smallimagepath = "";
		}
	}


}
