package com.winston.utils.shiro;


import com.winston.entity.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Date;

public class PasswordHelper {
	//private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;
	private String salt = "lkjLKSJ887J";

	/**
	 * 加密加盐
	 * @param user
	 */
	public void encryptPassword(User user) {
		//String salt=randomNumberGenerator.nextBytes().toHex();
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),  ByteSource.Util.bytes(user.getUsername()), hashIterations).toHex();
		//String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
		user.setPassword(newPassword);
	}

	/**
	 * 加密加盐
	 * @param user
	 */
	public void encryptPasswordWx(User user) {
		//String salt=randomNumberGenerator.nextBytes().toHex();
		String newOpenId = new SimpleHash(algorithmName, user.getOpenidHex(),  ByteSource.Util.bytes(user.getOpenId()), hashIterations).toHex();
		//String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
		user.setOpenidHex(newOpenId);

	}

	public String encryptString(String arg){
		return new SimpleHash(algorithmName, arg,  ByteSource.Util.bytes(salt), hashIterations).toHex();
	}

	public static void main(String[] args) {
		PasswordHelper passwordHelper = new PasswordHelper();
		User user = new User();
		long nowTime = new Date().getTime();
		user.setUsername("admin");
		user.setPassword("123456");
		user.setOpenId("oWYB6ww7hzeJ-IsBT106ob7WgaDE");
		user.setCreateTime(nowTime);
		user.setUpdateTime(nowTime);
		user.setCreateOpr("admin");
		user.setUpdateOpr("admin");
		user.setEnable(1);
		user.setState("1");
		passwordHelper.encryptPassword(user);

		System.out.println(user.getPassword());
	}
}
