package com.pureTec.tool;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFormater {

	public static String dateToStr() {
		// TODO Auto-generated method stub
		// SimpleDateFormat sdf = new SimpleDateFormat( " yyyy年MM月dd日 " );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

		return sdf.format(new Date());
	}

	public static Date strToDate(String dateStr) {
		// TODO Auto-generated method stub
		// SimpleDateFormat sdf = new SimpleDateFormat( " yyyy年MM月dd日 " );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static Date strToDateTime(String dateStr) {
		// TODO Auto-generated method stub
		// SimpleDateFormat sdf = new SimpleDateFormat( " yyyy年MM月dd日 " );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String floatFormate(Float number) {
		// TODO Auto-generated method stub
		// SimpleDateFormat sdf = new SimpleDateFormat( " yyyy年MM月dd日 " );
		DecimalFormat fnum = new DecimalFormat("##0.0"); 
		return fnum.format(number); 
	}
	
	
	
	
	
//	public static void main(String[] args) {
//		System.out.println("--------"+strToDateTime("2015-09-03 09:30:16"));
//	}
}
