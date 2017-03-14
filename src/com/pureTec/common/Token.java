package com.pureTec.common;

import java.security.MessageDigest;
import java.util.HashMap;

import com.jfinal.plugin.ehcache.CacheKit;

public class Token {

	public static String generateToken(long mid) {

		String value = Long.toString(mid + System.currentTimeMillis()); // mid +
																		// String

		return string2MD5(value);

	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}

		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 身份验证
	 * @param mid
	 * @param srctoken
	 * @return 返回0 验证通过,
	 */
	public static int tokenValidation(long mid, String srctoken) {

		// 第一步进行token验证

		HashMap<Long, String> tokenList = CacheKit.get("member", "tokenList");

		// 注册成功也要生成一个新的token
		if (tokenList != null) {

			String token = tokenList.get(mid);
			if (token == null) { // 缓冲中没有登录记录 
				
				return -1;

			} else { // 有记录需要进行更新
				if (token.equals(srctoken)) { // 验证成功

					return 0;
				} else {//token错误
					return -2;

					// resultData.setCode(-1);
					// resultData.setError("Token匹配错误");
				}
			}
		} else {
			return -1;
			// resultData.setCode(-1);
			// resultData.setError("系统错误");
		}

	}
	
	
	
	// public static void main(String[] args) {
	// System.out.println(string2MD5("123"));
	// }
	//

}