package com.pureTec.tool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

public class EncryptionNew {

	public float doneNum;// 已经完成的百分比

	public static void main(String[] args) {
		/*********** 加密 **************************************/
//		 try {
//		 encrypt("D:\\0002.mp3","key");
//		 } catch (Exception e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
//		 
		 
//		try {
//			encrypt("D:\\lwyRead\\0004.dat",  "D:\\lwyRead\\0005.dat", "abcd");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		/*********** 解密 **************************************/
		 try {
		 decrypt("D:\\lwyRead\\0004.dat", "D:\\lwyRead\\0003.dat", 4);
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }


	}

	/**
	 * 文件file进行加密
	 * 
	 * @param fileUrl
	 *            文件路径
	 * @param key
	 *            密码
	 * @throws Exception
	 */
	public void encrypt(String fileUrl, String key) throws Exception {
		File file = new File(fileUrl);
		String path = file.getPath();
		if (!file.exists()) {
			return;
		}
		int index = path.lastIndexOf("File.separator");
		String destFile = path.substring(0, index) + "File.separator" + "abc";
		File dest = new File(destFile);
		InputStream in = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buffer = new byte[1024];
		int r;
		byte[] buffer2 = new byte[1024];
		while ((r = in.read(buffer)) > 0) {
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 255 ? 0 : ++b;
			}
			out.write(buffer2, 0, r);
			out.flush();
		}
		in.close();
		out.close();
		file.delete();
		dest.renameTo(new File(fileUrl));
		appendMethodA(fileUrl, key);
		System.out.println("加密成功");

		// 执行过程，创建然后新的加密文件，然后删除源文件，最后修改新加密文件的名称为源文件
	}

	/**
	 * 文件file进行加密
	 * 
	 * @param fileSourceUrl
	 *            文件路径
	 * @param key
	 *            密码
	 * @throws Exception
	 */
	public static void  encrypt(String fileSourceUrl, String destFile, String key) throws Exception {
		File file = new File(fileSourceUrl);
		String path = file.getPath();
		if (!file.exists()) {
			return;
		}
		int index = destFile.lastIndexOf(".");
		destFile = destFile.substring(0, index) + ".dat";
		String destFiledir = destFile.substring(0, destFile.lastIndexOf(File.separator));
		System.out.println("destFiledir-------" + destFiledir);

		File dest = new File(destFile);
		if (dest.exists()) {
			dest.delete();
		}// 已有文件则删除

		File destdir = new File(destFiledir);
		if (!destdir.exists()) {
			destdir.mkdir();
		}// ，目录不存在则新建

		InputStream in = new FileInputStream(fileSourceUrl);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buffer = new byte[1024];
		int r;
		byte[] buffer2 = new byte[1024];
		while ((r = in.read(buffer)) > 0) {
			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
//				buffer2[i] = b == 255 ? 0 : ++b;
				
				if( b == 255){
					buffer2[i] = 1;
				}else
				if( b == 254){
					buffer2[i] = 0;
				}else{
					++b;
					++b;
					buffer2[i]= b;
				}
				
				
			}
			out.write(buffer2, 0, r);
			out.flush();
		}
		in.close();
		out.close();
//		 file.delete();
		// dest.renameTo(new File(destFile));
		// dest.
		appendMethodA(destFile, key);
		System.out.println("加密成功");

		// 执行过程，创建然后新的加密文件，然后删除源文件，最后修改新加密文件的名称为源文件
	}

	public int encryptDir(String fileSourceUrl, String key) {
		doneNum=0;
		JavaFileDir javaFileDir = new JavaFileDir();
		List<File> fileList = javaFileDir.getAllfileListByDir(fileSourceUrl);
		for (int i = 0; i < fileList.size(); i++) {
			// System.out.println("xx--------" + filetemp.getAbsolutePath());
			try {
				encrypt(fileList.get(i).getAbsolutePath(), fileList.get(i).getAbsolutePath().replace(File.separator+"source"+File.separator, File.separator+"MMsource"+File.separator), key);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			doneNum =(float) (i + 1) / (float)fileList.size();// 已近完成的比例
		}
		return fileList.size(); // 返回总数量 显示加密进度

	}

	/**
	 * 
	 * @param fileName
	 * @param content
	 *            密钥 在文件的最后加入秘钥信息？ 这个做法是在是奇怪。 按照上述的算法，是在最后删除掉秘钥信息。 不靠谱，但是简单的加密，容易被被破解。 我还是想用无法解密的算法。
	 */
	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解密
	 * 
	 * @param fileUrl
	 *            源文件
	 * @param tempUrl
	 *            临时文件
	 * @param ketLength
	 *            密码长度 可以解密到零时文件 为什么解密不需要key 只需要key的长度
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String fileUrl, String tempUrl, int keyLength) throws Exception {
		File file = new File(fileUrl);
		if (!file.exists()) {
			return null;
		}
		File dest = new File(tempUrl);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}

		InputStream is = new FileInputStream(fileUrl);
		OutputStream out = new FileOutputStream(tempUrl);

		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = file.length() - keyLength;
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1);
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}

			for (int i = 0; i < r; i++) {
				byte b = buffer[i];
				buffer2[i] = b == 0 ? bMax : --b;
			}
			out.write(buffer2, 0, r);
			k++;
		}
		out.close();
		is.close();
		return tempUrl;
	}

	/**
	 * 解密 返回文件流 直接使用
	 * 
	 * @param fileUrl
	 *            源文件
	 * @param tempUrl
	 *            临时文件
	 * @param ketLength
	 *            密码长度 可以解密到零时文件 为什么解密不需要key 只需要key的长度
	 * @return
	 * @throws Exception
	 */
	public static InputStream decrypt(String fileUrl, int keyLength) throws Exception {

		// 可以使用ByteArrayInputStream/ByteArrayOutputStream 字节流的实现
		StringBuffer strBuffer = new StringBuffer();

		File file = new File(fileUrl);
		if (!file.exists()) {
			return null;
		}

		// byte[] bufferIn = new byte[file.length()];

		LinkedList bufferIn = new LinkedList<Object>();

		// File dest = new File(tempUrl);
		// if (!dest.getParentFile().exists()) {
		// dest.getParentFile().mkdirs();
		// }

		InputStream is = new FileInputStream(fileUrl);
		// OutputStream out = new FileOutputStream(tempUrl);

		byte[] buffer = new byte[1024];
		byte[] buffer2 = new byte[1024];
		byte bMax = (byte) 255;
		long size = file.length() - keyLength;
		int mod = (int) (size % 1024);
		int div = (int) (size >> 10);
		int count = mod == 0 ? div : (div + 1); // 对文件的分段操作 实在的写法.
		int k = 1, r;
		while ((k <= count && (r = is.read(buffer)) > 0)) {
			if (mod != 0 && k == count) {
				r = mod;
			}

			for (int i = 0; i < r; i++) {
				byte b = buffer[i];

				buffer2[i] = (b == 0 ? bMax : --b);
				strBuffer.append(buffer2[i]);
				bufferIn.add(buffer2[i]);
			}
			// out.write(buffer2, 0, r);

			k++;
		}

		// out.close();
		is.close();

		InputStream isUnEncript = new ByteArrayInputStream(strBuffer.toString().getBytes("utf-8"));

		for (Object object : bufferIn) {
			// System.out.println((char)(String.valueOf(object).getBytes()[0]));
			System.out.println(object);

		}

		return isUnEncript;
	}

	/**
	 * 判断文件是否加密
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileLastByte(String fileName, int keyLength) {
		File file = new File(fileName);
		if (!file.exists())
			return null;
		StringBuffer str = new StringBuffer();
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			for (int i = keyLength; i >= 1; i--) {
				randomFile.seek(fileLength - i);
				str.append((char) randomFile.read());
			}
			randomFile.close();
			return str.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}