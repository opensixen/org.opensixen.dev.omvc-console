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
