/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.db;


import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class ShiroMD5PasswordEncoder {
	  private String algorithmName = "md5";

	  private int hashIterations = 2;
 
	  private String salt = "easytnt";
	  
	  public ShiroMD5PasswordEncoder() {
		  
	  }

	  public String encode(String owId, String password) {
	    this.salt = owId;
	    return new SimpleHash(this.algorithmName, password, ByteSource.Util.bytes(this.salt), this.hashIterations).toHex();
	  }

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
}
