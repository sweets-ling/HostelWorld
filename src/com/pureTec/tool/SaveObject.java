package com.pureTec.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public class SaveObject {

	public static Object reStore(Type classOfT,String filePath) {

//		File file = new File("d://person3.dat");
		File file = new File(filePath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int length = 0;
		String gosnStr = null;
		try {
			length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
//			gosnStr = new String(buffer, "UTF-8");
			gosnStr = new String(buffer);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gson gson = new Gson();
		Object objectStr = gson.fromJson(gosnStr, classOfT);

		// Object res = EncodingUtils.getString(buffer, "UTF-8");

		return objectStr;

	}

	public static void saveObject(Object object,String filePath) {
		

		Gson gson = new Gson();
		String objectStr = gson.toJson(object); // 这个也是字符也是对的
//		try {
//			objectStr =new String(objectStr.getBytes(),"UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		File file = new File("d://person3.dat");
		File file = new File(filePath);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = null;
		try {
			bytes = objectStr.getBytes("UTF-8"); // 这个字符格式要注意一下 
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
