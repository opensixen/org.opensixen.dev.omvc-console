package org.opensixen.omvc.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.opensixen.dev.omvc.model.RienaTools;
import org.opensixen.riena.client.proxy.ServiceConnection;
import org.opensixen.riena.interfaces.IServiceConnectionHandler;


public class ConfigUtil implements IServiceConnectionHandler {

	private static final String fileName = "console.properties";
	
	private static final String KEY_HOST = "host";
	private static final String KEY_USER = "user";
	private static final String KEY_PORT = "port";
	
	private static final String KEY_PASSWORD = "password";
	
	private String host;
	
	private String port;
	
	private String user;
	
	private String password;

	private static ConfigUtil instance;
	
	public static ConfigUtil getConfig()	{
		if (instance == null)	{
			instance = new ConfigUtil();
		}
		
		return instance;
	}
	
	
	private ConfigUtil() {
		super();
		// TODO Auto-generated constructor stub
	}	

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	public boolean loadConf()	{
		String confPath = Activator.getDefault().getStateLocation().toOSString();
		File f = new File(confPath + "/" + fileName);
		if (!f.exists())	{
			return false;
		}
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(f));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		setHost(prop.getProperty(KEY_HOST));
		setUser(prop.getProperty(KEY_USER));
		setPassword(prop.getProperty(KEY_PASSWORD));
		setPort(prop.getProperty(KEY_PORT));
				
		return true;
	}
	
	public boolean saveConf()	{
		Properties prop = new Properties();
		
		prop.setProperty(KEY_HOST, getHost());
		prop.setProperty(KEY_PORT, getPort());
		prop.setProperty(KEY_USER, getUser());
		prop.setProperty(KEY_PASSWORD, getPassword());
		String fname = Activator.getDefault().getStateLocation().toOSString() + "/" + fileName;
		try {
			FileOutputStream stream = new FileOutputStream(fname, false);
			prop.store(stream, "Configuration for org.opensixen.dev.omvc-console plugin");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public String getHostUrl(String webService)	{	
		return RienaTools.getURL(getHost(), getPort(), webService);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.interfaces.IServiceConnectionHandler#getServiceConnection()
	 */
	@Override
	public ServiceConnection getServiceConnection() {
		ServiceConnection connection = new ServiceConnection();
		connection.setHost(getHost());
		connection.setPort(getPort());
		return connection;
	}
	
}
