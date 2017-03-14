package com.pureTec.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩方法 见main里面的测试
 * 需要的引入ant.jar 
 * @author kk
 *
 */
public class ZipUtil {
	public ZipUtil() {
	}

	public float doneNum;// 压缩完成的比例

	/**
	 * * inputFileName 输入一个文件夹 zipFileName 输出一个压缩文件夹
	 * 
	 * @param inputFileName
	 * @param outPutFileName
	 * @param basePath
	 *            //一般为null 如果想压缩要指定的路径 加 001/source/ 压缩深度增加
	 * @throws Exception
	 */
	public void zip(String inputFileName, String outPutFileName, String basePath) throws Exception {
		// String zipFileName = "E:\\test.zip"; //打包后文件名字
		String zipFileName = outPutFileName + ".lwyr"; // 打包后文件名字
		System.out.println(zipFileName);
		zip(zipFileName, new File(inputFileName),basePath);
	}

	private void zip(String zipFileName, File inputFile, String basePath) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		zip2(out, inputFile, "",basePath);
		System.out.println("zip done");
		out.close();
	}

	/**
	 * 压缩的递归方法
	 * 
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	/**
	 * 压缩的非递归方法
	 * 
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private void zip2(ZipOutputStream out, File f, String base, String basePath) throws Exception {
		doneNum = 0;
		List<File> fileList;

		if (f.isDirectory()) {
			JavaFileDir javaFileDir = new JavaFileDir();
			fileList = javaFileDir.getAllfileListByDir(f.getAbsolutePath());
		} else {
			fileList = new ArrayList<File>();
			fileList.add(f);
		}

		for (int i = 0; i < fileList.size(); i++) {
			base = fileList.get(i).getAbsolutePath().substring(f.getAbsolutePath().length(), fileList.get(i).getAbsolutePath().length());
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(basePath + base));
			FileInputStream in = new FileInputStream(fileList.get(i));
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}

			in.close();

			doneNum = (float) (i + 1) / (float) fileList.size();// 已近完成的比例
			// System.out.println("xx---doneNum-----"+doneNum);
		}
	}

	/**
	 * 解压zip文件 这个解压算法是有缺陷的。 -需要重新改良
	 * 
	 * @param zipFilePath
	 *            zip文件绝对路径
	 * @param unzipDirectory
	 *            解压到的目录
	 * @throws Exception
	 */
	public static void unzip(String zipFilePath, String unzipDirectory) throws Exception {
		// 定义输入输出流对象
		InputStream input = null;
		OutputStream output = null;
		try {
			// 创建文件对象
			File file = new File(zipFilePath);
			// 创建zip文件对象
			ZipFile zipFile = new ZipFile(file);
			// 创建本zip文件解压目录
			String name = file.getName().substring(0, file.getName().lastIndexOf("."));

			/* 这里修改原文件名 */
			// File unzipFile = new File(unzipDirectory + "/" + name);
			File unzipFile = new File(unzipDirectory);// 不保留原文件名

			File destdir = new File(unzipDirectory);
			if (!destdir.exists()) {
				destdir.mkdir();
			}// ，目录不存在则新建

			if (unzipFile.exists())
				unzipFile.delete();// 删除已经存在的文件 ，删除这个同名的文件
			unzipFile.mkdir();
			// 得到zip文件条目枚举对象
			Enumeration zipEnum = zipFile.getEntries();
			// 定义对象
			ZipEntry entry = null;
			String entryName = null, path = null;
			String names[] = null;
			int length;
			// 循环读取条目
			while (zipEnum.hasMoreElements()) {
				// 得到当前条目
				entry = (ZipEntry) zipEnum.nextElement();
				entryName = new String(entry.getName());// entryName是相对路径
				System.out.println("entryName--------" + entryName);
				// 用/分隔条目名称
				// names = entryName.split("\\/");//表示分隔符为/ 写法错误
				names = entryName.split("\\\\");// 表示分隔符为\
				length = names.length;
				path = unzipFile.getAbsolutePath();
				for (int v = 0; v < length; v++) {
					System.out.println("xx----names----" + names[v]);
				}
				for (int v = 0; v < length; v++) {
					if (v < length - 1) { // 最后一个目录之前的目录 这样的做法是错误的
						java.io.File tempfile123 = new java.io.File(path += "/" + names[v] + "/");
						tempfile123.mkdir();
						// FileUtil.createDirectory(path += "/" + names[v] + "/");

					} else { // 最后一个
						if (entryName.endsWith("/")) { // 为目录,则创建文件夹
							// FileUtil.createDirectory(unzipFile.getAbsolutePath() + "/" + entryName);
							java.io.File tempfile123 = new java.io.File(unzipFile.getAbsolutePath() + "/" + entryName);
							tempfile123.mkdir();
						} else { // 为文件,则输出到文件
							input = zipFile.getInputStream(entry);
							output = new FileOutputStream(new File(unzipFile.getAbsolutePath() + "/" + entryName));
							byte[] buffer = new byte[1024 * 8];
							int readLen = 0;
							while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
								output.write(buffer, 0, readLen);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (input != null)
				input.close();
			if (output != null) {
				output.flush();
				output.close();
			}
		}
	}

	public static void main(String[] temp) {
		ZipUtil book = new ZipUtil();
		
//		G:\software\apache-tomcat-7.0.69\webapps\MinLanWeb\file_souce\B001\source
		
		try {
			// book.zip("./record","./record");//你要压缩的文件夹
			book.zip("G:\\zipTest\\", "G:\\test",null);// 你要压缩的文件夹
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		//
		//
		// try {
		// // book.zip("./record","./record");//你要压缩的文件夹
		// book.unzip("D:\\lwyRead\\001\\source001.lwyr", "D:\\lwyRead\\001\\UnzipMMsource\\");// 你要压缩的文件夹
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
	}
}