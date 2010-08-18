package org.opensixen.omvc.riena;

import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
import org.opensixen.dev.omvc.interfaces.IRevisionUploader;
import org.opensixen.omvc.console.Activator;
import org.osgi.framework.ServiceReference;



public class ServiceFactory {
	
			
	public static IRemoteConsole getConsole()		{
		ServiceReference ref = Activator.getContext().getServiceReference(IRemoteConsole.class.getName());
		IRemoteConsole console = (IRemoteConsole) Activator.getContext().getService(ref);
		
		if (console == null)	{
			throw new RuntimeException("No se puede registrar la consola remota");
		}
		return console;
	}
	
	
	public static IRevisionUploader getUploader()		{
		ServiceReference ref = Activator.getContext().getServiceReference(IRevisionUploader.class.getName());
		IRevisionUploader uploader = (IRevisionUploader) Activator.getContext().getService(ref);
		
		if (uploader == null)	{
			throw new RuntimeException("No se puede registrar el uploader remoto");
		}
		return uploader;
	}
	
	
	public static IRevisionDownloader getDownloader()		{
		ServiceReference ref = Activator.getContext().getServiceReference(IRevisionDownloader.class.getName());
		IRevisionDownloader downloader = (IRevisionDownloader) Activator.getContext().getService(ref);
		
		if (downloader == null)	{
			throw new RuntimeException("No se puede registrar el downloader remoto");
		}
		return downloader;
	}
}
