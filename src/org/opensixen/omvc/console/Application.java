package org.opensixen.omvc.console;

import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import javax.security.auth.Subject;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
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
	public Object start(IApplicationContext context)  throws Exception  {
		
		if (initApp() == false) {
			return null;
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
			public Object run() {
				int result = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				return new Integer(result);
			}
		};
	}
	
	private boolean initApp() {
		// Leemos la configuracion
		ConfigUtil config = ConfigUtil.getConfig();

		if (config.loadConf() == false) {
			ConfigDialog configDialog = new ConfigDialog(shell, config);
			if (configDialog.open() == SWT.CANCEL) {
				return false;
			}
		}
		
		// Registramos el Authentication Service		
		String url = config.getHostUrl(IAuthenticationService.WS_ID);
		services.add(Register.remoteProxy(IAuthenticationService.class).usingUrl(url).withProtocol("hessian").andStart(Activator.getContext()));
				
		// Setup RienaServer connection		
		RienaServerProxy.setServiceConnectionHandler(config);
		
		// Setups configs
		RemoteConsoleProxy.setServiceConnectionHandler(config);
		RevisionDownloaderProxy.setServiceConnectionHandler(config);
		
		// Add proxy to server
		RienaServerProxy.addService(RemoteConsoleProxy.getInstance());
		RienaServerProxy.addService(RevisionDownloaderProxy.getInstance());

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
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
