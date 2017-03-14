package com.pureTec.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pureTec.course.CourseData;

/**
 * xls工具类
 * 
 * @author hjn
 * 
 */
public class ExcleUtil {

	/**
	 * 支持 xls xlsx
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static void read(String filePath) throws IOException {
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				System.out.print(cell.getStringCellValue() + "  ");
			}
			System.out.println();
		}
	}

	public static List<CourseData> readCoureData(String filePath) throws IOException {

		List<CourseData> cds = new ArrayList<CourseData>();

		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		int rowIndex = 0;
		for (Row row : sheet1) {
			if (rowIndex == 0) {
				rowIndex++;
				continue; // 第一行是标题栏，不用读取
			}
			CourseData cd = new CourseData();
			int index = 0;
			for (Cell cell : row) {
                //怎么判断这个cell是否为空呢？
//				cell.get      
				switch (index) {
				case 0:
					//这里要判断一下是否为空，id可以为空
					cd.set("id", (long) Double.parseDouble(cell.getNumericCellValue() + "")); // 数字类型转换好麻烦
					break;
				case 1:
					cd.set("order", (int) Double.parseDouble(cell.getNumericCellValue() + ""));
					break;
				case 2:
					cd.set("chapter", cell.getNumericCellValue() + "");
					break;
				case 3:
					cd.set("name", cell.getStringCellValue() + "");
					break;
				case 4:
					cd.set("chinese", cell.getStringCellValue() + "");
					break;

				default:
					break;
				}

				index++;
				// System.out.print(cell.getStringCellValue() + "  ");
			}

			cds.add(cd);
			System.out.println();
		}

		return cds;
	}
	
	
	public static List<CourseData> readCoureDataForSection(String filePath,int chapter) throws IOException {
		
		List<CourseData> cds = new ArrayList<CourseData>();
		
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		int rowIndex = 0;
		for (Row row : sheet1) {
			if (rowIndex == 0) {
				rowIndex++;
				continue; // 第一行是标题栏，不用读取
			}
			CourseData cd = new CourseData();
			int index = 0;
			for (Cell cell : row) {
				//怎么判断这个cell是否为空呢？
//				cell.get      
				cd.set("chapter", chapter);
				switch (index) {
				case 0:
					//这里要判断一下是否为空，id可以为空
					cd.set("id", (long) Double.parseDouble(cell.getNumericCellValue() + "")); // 数字类型转换好麻烦
					break;
				case 1:
					cd.set("order", (int) Double.parseDouble(cell.getNumericCellValue() + ""));
					break;
				case 2:
					cd.set("section", cell.getNumericCellValue() + "");
					break;
				case 3:
					cd.set("name", cell.getStringCellValue() + "");
					break;
				case 4:
					cd.set("chinese", cell.getStringCellValue() + "");
					break;
					
				default:
					break;
				}
				
				index++;
				// System.out.print(cell.getStringCellValue() + "  ");
			}
			
			cds.add(cd);
			System.out.println();
		}
		
		return cds;
	}
	public static List<CourseData> readCoureDataWords(String filePath,int chapter,int section) throws IOException {
		
		List<CourseData> cds = new ArrayList<CourseData>();
		
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		int rowIndex = 0;
		for (Row row : sheet1) {
			if (rowIndex == 0) {
				rowIndex++;
				continue; // 第一行是标题栏，不用读取
			}
			CourseData cd = new CourseData();
			int index = 0;
			for (Cell cell : row) {
				//怎么判断这个cell是否为空呢？ 为空的时候好像并不读取的样子
				cd.set("chapter", chapter);
				cd.set("section", section);
				
//				ID	排序编号	中文	外文	音频编号	图片1	图片2
//				6	1	你好	hello	0001	001.jpg	002.png
//				12	1	大家好	hello everyone	0002	003.png	004.jpg
				
				switch (index) {
				case 0:
					//这里要判断一下是否为空，id可以为空
					cd.set("id", (long) Double.parseDouble(cell.getNumericCellValue() + "")); // 数字类型转换好麻烦
					break;
				case 1:
					cd.set("order", (int) Double.parseDouble(cell.getNumericCellValue() + ""));
					break;
				case 2:
					cd.set("chinese", cell.getStringCellValue() + "");
					break;
				case 3:
					cd.set("chinese_say", cell.getStringCellValue() + "");
					break;
				case 4:
					cd.set("foreign", cell.getStringCellValue() + "");
					break;
				case 5:
					cd.set("audio", cell.getStringCellValue() + "");
					break;
				case 6:
					cd.set("img1", cell.getStringCellValue() + "");
					break;
				case 7:
					cd.set("img2", cell.getStringCellValue() + "");
					break;
				case 8:
					cd.set("describe", cell.getStringCellValue() + "");
					break;
					
				default:
					break;
				}
				
				index++;
				// System.out.print(cell.getStringCellValue() + "  ");
			}
			
			cds.add(cd);
			System.out.println();
		}
		
		return cds;
	}

	
	
	
	
	
	public static boolean write(String outPath) throws Exception {
		String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
		System.out.println(fileType);
		// 创建工作文档对象
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("您的文档格式不正确！");
			return false;
		}
		// 创建sheet对象
		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
		// 循环写入行数据
		for (int i = 0; i < 5; i++) {
			Row row = (Row) sheet1.createRow(i);
			// 循环写入列数据
			for (int j = 0; j < 8; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue("测试" + j);
			}
		}
		// 创建文件流
		OutputStream stream = new FileOutputStream(outPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
		return true;
	}

	public static boolean write(String outPath, String titles[], List<List<Object>> datas) throws Exception {
		String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
		System.out.println(fileType);
		// 创建工作文档对象
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("您的文档格式不正确！");
			return false;
		}
		// 创建sheet对象
		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");

		// 首先写入标题行
		Row titleRow = (Row) sheet1.createRow(0);
		for (int i = 0; i < titles.length; i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
		}

		// 循环写入行数据
		for (int i = 0; i < datas.size(); i++) {
			Row row = (Row) sheet1.createRow(i + 1); // 循环写入列数据
			for (int j = 0; j < datas.get(i).size(); j++) {
				Cell cell = row.createCell(j);
				if (datas.get(i).get(j) != null) {

					if (datas.get(i).get(j).getClass().equals(java.lang.String.class)) {
						cell.setCellValue(datas.get(i).get(j).toString()); // 数据就两种类型，一种string 一种不是string
					} else {
						cell.setCellValue(Long.parseLong(datas.get(i).get(j).toString())); // 数据就两种类型，一种string
																							// 一种不是string

					}
//					System.out.println("--------" + datas.get(i).get(j).getClass().toString());
					// cell.setC
				}
			}
		}

		// 创建文件流
		OutputStream stream = new FileOutputStream(outPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
		return true;
	}

	public boolean writeExcel(String outPath, String titles[], List<List<Object>> datas) throws Exception {
		String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
		System.out.println(fileType);
		// 创建工作文档对象
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("您的文档格式不正确！");
			return false;
		}
		// 创建sheet对象
		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");

		// 首先写入标题行
		Row titleRow = (Row) sheet1.createRow(0);
		for (int i = 0; i < titles.length; i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
		}

		// 循环写入行数据
		for (int i = 0; i < datas.size(); i++) {
			Row row = (Row) sheet1.createRow(i + 1); // 循环写入列数据
			for (int j = 0; j < datas.get(i).size(); j++) {
				Cell cell = row.createCell(j);
				if (datas.get(i).get(j) != null) {
					cell.setCellValue(datas.get(i).get(j).toString());
				}
			}
		}

		// 创建文件流
		OutputStream stream = new FileOutputStream(outPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
		return true;
	}

	public static void main(String[] args) {
		try {
			// ExcleUtil.write("D:" + File.separator + "out.xlsx");
			String titles[] = { "sdf", "sdfs", "sdfsdf" };
			List<List<Object>> datas = new ArrayList<List<Object>>();
			List<Object> wer = new ArrayList<Object>();
			wer.add("234");
			wer.add("234");
			wer.add("234");
			datas.add(wer);
			datas.add(wer);

			ExcleUtil.write("D:" + File.separator + "out.xlsx", titles, datas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ExcleUtil.read("D:" + File.separator + "out.xlsx");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}