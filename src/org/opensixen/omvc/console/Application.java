package org.opensixen.omvc.console;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opensixen.dev.omvc.interfaces.IRemoteCentralizedIDGenerator;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionUploader;
import org.opensixen.omvc.riena.ServiceFactory;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		initApp();
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}
	
	private void initApp()	{
		// Registramos la consola remota
		String url = "http://localhost:8080/hessian/RemoteConsoleWS";
		Register.remoteProxy(IRemoteConsole.class).usingUrl(url).withProtocol("hessian").andStart(Activator.getContext());
		
		// Registramos el uploader de revisiones
		url = "http://localhost:8080/hessian/RevisionUploaderWS";
		Register.remoteProxy(IRevisionUploader.class).usingUrl(url).withProtocol("hessian").andStart(Activator.getContext());
		
	}
	

	/* (non-Javadoc)
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
}
