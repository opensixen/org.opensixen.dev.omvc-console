package org.opensixen.omvc.console;

import java.util.ArrayList;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opensixen.dev.omvc.interfaces.IRemoteCentralizedIDGenerator;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionUploader;
import org.opensixen.omvc.console.dialog.ConfigDialog;
import org.opensixen.omvc.riena.ServiceFactory;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private static Shell shell;
	
	private static boolean registered;
	private static ArrayList<IRemoteServiceRegistration> services = new ArrayList<IRemoteServiceRegistration>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		this.shell = display.getActiveShell();
		if (initApp() == false) {
			return null;
		}
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	private boolean initApp() {

		return register();
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

	public static boolean register() {
		if (registered)	{
			return true;
		}
		
		// Leemos la configuracion
		ConfigUtil config = new ConfigUtil();

		if (config.loadConf() == false) {
			ConfigDialog configDialog = new ConfigDialog(shell, config);
			if (configDialog.open() == SWT.CANCEL) {
				return false;
			}
		}

		// Registramos la consola remota
		String url = config.getHostUrl(IRemoteConsole.path);
		services.add(Register.remoteProxy(IRemoteConsole.class).usingUrl(url)
				.withProtocol("hessian").andStart(Activator.getContext()));

		// Registramos el uploader de revisiones
		url = config.getHostUrl(IRevisionUploader.path);
		services.add(Register.remoteProxy(IRevisionUploader.class)
				.usingUrl(url).withProtocol("hessian")
				.andStart(Activator.getContext()));

		registered = true;
		return true;
	}

	public static void unregister() {
		if (!registered)	{
			return;
		}
		for (IRemoteServiceRegistration service : services) {
			service.unregister();
		}
	}

}
