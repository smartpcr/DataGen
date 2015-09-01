package org.finra.test.datagen.util;

/**
 * Created on 9/1/2015.
 */
public class DbConnection {
	private String driverClassName;
	public String getDriverClassName() {
	    return this.driverClassName;
	}

	private String host;
	public String getHost() {
	    return this.host;
	}

	private String user;
	public String getUser() {
	    return this.user;
	}

	private String pwd;
	public String getPwd() {
	    return this.pwd;
	}

	public DbConnection(String driverClassName, String host, String user, String pwd) {
		this.driverClassName = driverClassName;
		this.host = host;
		this.user = user;
		this.pwd = pwd;
	}

}
