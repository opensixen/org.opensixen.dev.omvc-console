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

import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import javax.security.auth.Subject;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opensixen.omvc.client.proxy.OMVCAuthServiceProxy;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;
import org.opensixen.omvc.client.proxy.RevisionDownloaderProxy;
import org.opensixen.omvc.console.dialog.ConfigDialog;
import org.opensixen.riena.client.proxy.RienaServerProxy;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private static Shell shell;
	
	private static boolean registered;
	
	private static final String JAAS_CONFIG_FILE = "data/jaas_config.txt"; //$NON-NLS-1$
	
	private static ArrayList<IRemoteServiceRegistration> services = new ArrayList<IRemoteServiceRegistration>();

	private static ILoginContext loginContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context)  throws Exception  {
		
		while (initApp() == false) {
			ConfigUtil config = ConfigUtil.getInstance();
			ConfigDialog configDialog = new ConfigDialog(shell, config);
			if (configDialog.open() == Window.CANCEL) {
				return EXIT_OK;
			}			
		}
				
		// Setup login context
		String configName = Activator.getConfigurationName();
		URL configUrl = Activator.getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		loginContext = LoginContextFactory.createContext(configName, configUrl);
		
		
		Integer result = null;
		final Display display = PlatformUI.createDisplay();
		try {
		
			// Run app in secure context
			result = (Integer) Subject.doAs(loginContext.getSubject(), getRunAction(display));
		} finally {
			display.dispose();
			loginContext.logout();
		}
		// TBD handle javax.security.auth.login.LoginException

		if (result != null && PlatformUI.RETURN_RESTART == result.intValue())
			return EXIT_RESTART;
		return EXIT_OK;
	}

	private PrivilegedAction getRunAction(final Display display) {
		return new PrivilegedAction() {
			@Override
			public Object run() {
				int result = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				return new Integer(result);
			}
		};
	}
	
	private boolean initApp() {
		// Leemos la configuracion
		ConfigUtil config = ConfigUtil.getInstance();

		if (config.loadConf() == false) {
			return false;
		}
		
		try {
		
		// Registramos el Authentication Service			
		OMVCAuthServiceProxy.getInstance().setServiceConnectionHandler(config);
		OMVCAuthServiceProxy.getInstance().register();
				
		// Setup RienaServer connection		
		RienaServerProxy.setServiceConnectionHandler(config);
		
		// Setups configs
		RemoteConsoleProxy.getInstance().setServiceConnectionHandler(config);
		RevisionDownloaderProxy.getInstance().setServiceConnectionHandler(config);
		
		// Add proxy to server
		RienaServerProxy.addService(RemoteConsoleProxy.getInstance());
		RienaServerProxy.addService(RevisionDownloaderProxy.getInstance());
		
		return true;
		}
		catch (Exception e)	{
			
			return false;
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
	public static ILoginContext getLoginContext()	{
		return loginContext;
	}

}
