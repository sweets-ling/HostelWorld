package com.pureTec.tool;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class JavaFileDir {
	public List<File> getAllfileListByDir(String dirPath) {
		// TODO Auto-generated method stub
		List<File> dirList = new ArrayList<File>();
		List<File> fileList = new ArrayList<File>();
		// File dir = new File("D:\\lwyRead\\001\\source\\");
		File dir = new File(dirPath);
		File file[] = dir.listFiles();// �Ȼ�õ�ǰ�ļ����µ����ļ�
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory())
				dirList.add(file[i]);
			else
				// System.out.println(file[i].getAbsolutePath());
				fileList.add(file[i]);
		}
		File tmp;
		while (!dirList.isEmpty()) {
			tmp = dirList.remove(0);
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null)
					continue;
				for (int i = 0; i < file.length; i++) {
					if (file[i].isDirectory()) {
						dirList.add(file[i]);
					} else {
						fileList.add(file[i]);
						// System.out.println(file[i].getAbsolutePath());
					}
				}
			} else {
				fileList.add(tmp);
				// System.out.println(tmp.getAbsolutePath());
			}
		}
		return fileList;
	}

	// ɾ���ļ���
	// param folderPath �ļ���������·��
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ����ļ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ɾ��ָ���ļ����������ļ�
	// param path �ļ���������·��
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ����ļ���
				flag = true;
			}
		}
		return flag;
	}
	
	
	
	
	
	public long getFileSizes(File f) throws Exception{//取得文件大小
	       long s=0;
	       if (f.exists()) {
	           FileInputStream fis = null;
	           fis = new FileInputStream(f);
	          s= fis.available();
	       } else {
	           f.createNewFile();
	           System.out.println("文件不存在");
	       }
	       return s;
	    }
	
	/**
	 * 保留至整数
	 * @param fileS
	 * @return
	 */
	public String formetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public String fileSize(String path){
		File file = new File(path);
		try {
			return formetFileSize(getFileSizes(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "---M";
	}
	
	
	
	
	
	public static void main(String[] args) {
		JavaFileDir JavaFileDir = new JavaFileDir();
		JavaFileDir.delFolder("D:\\lwyRead\\001\\MMsourdsce\\");
	}
}