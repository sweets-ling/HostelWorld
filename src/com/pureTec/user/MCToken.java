package com.pureTec.user;

import com.jfinal.plugin.activerecord.Model;

/**
 * 不能Token 和系统类名称重复
 * 
 * @author kk
 *
 */
public class MCToken extends Model<MCToken> {

	public static final MCToken me = new MCToken();



}