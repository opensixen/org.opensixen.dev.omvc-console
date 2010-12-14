 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.omvc.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.opensixen.dev.omvc.model.RienaTools;
import org.opensixen.riena.client.proxy.ServiceConnection;
import org.opensixen.riena.interfaces.IConnectionChangeListener;
import org.opensixen.riena.interfaces.IServiceConnectionHandler;

/**
 * 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class ConfigUtil implements IServiceConnectionHandler {

	private static final String fileName = "console.properties";
	
	private static final String KEY_HOST = "host";
	private static final String KEY_USER = "user";
	private static final String KEY_PORT = "port";
	private static final String KEY_SERVICE = "service";
	
	private static final String KEY_PASSWORD = "password";
	
	private ArrayList<IConnectionChangeListener> connectionChangeListeners = new ArrayList<IConnectionChangeListener>();
	
	private String host;
	
	private String port;
	
	private String user;
	
	private String service;
	
	private String password;

	private static ConfigUtil instance;
	
	public static ConfigUtil getInstance()	{
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
	
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}


	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
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
		setService(prop.getProperty(KEY_SERVICE));
				
		return true;
	}
	
	public boolean saveConf()	{
		Properties prop = new Properties();
		
		prop.setProperty(KEY_HOST, getHost());
		prop.setProperty(KEY_PORT, getPort());
		prop.setProperty(KEY_USER, getUser());
		prop.setProperty(KEY_PASSWORD, getPassword());
		prop.setProperty(KEY_SERVICE, getService());
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
		fireConnectionChange();
		return true;
	}
		
	public String getHostUrl(String webService)	{	
		return RienaTools.getURL(getHost(), getPort(), getService(), webService);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.interfaces.IServiceConnectionHandler#getServiceConnection()
	 */
	@Override
	public ServiceConnection getServiceConnection() {
		ServiceConnection connection = new ServiceConnection();
		connection.setHost(getHost());
		connection.setPort(getPort());
		connection.setService(getService());
		return connection;
	}


	/* (non-Javadoc)
	 * @see org.opensixen.riena.interfaces.IServiceConnectionHandler#addConnectionChangeListener(org.opensixen.riena.interfaces.IConnectionChangeListener)
	 */
	@Override
	public void addConnectionChangeListener(IConnectionChangeListener listener) {
		connectionChangeListeners.add(listener);
		
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.interfaces.IServiceConnectionHandler#removeConnectionChangeListener(org.opensixen.riena.interfaces.IConnectionChangeListener)
	 */
	@Override
	public void removeConnectionChangeListener(IConnectionChangeListener listener) {
		if (connectionChangeListeners.contains(listener))	{
			connectionChangeListeners.remove(listener);
		}		
	}	
	
	private void fireConnectionChange()	{
		for (IConnectionChangeListener listener : connectionChangeListeners)	{
			listener.fireConnectionChange();
		}
	}
	
}
