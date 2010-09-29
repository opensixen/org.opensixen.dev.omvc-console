package org.opensixen.omvc.console;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static BundleContext context;
	
	
	/* stuff for the login configuration */
	private static final String CONFIG_PREF = "loginConfiguration"; //$NON-NLS-1$
	private static final String CONFIG_DEFAULT = "omvc"; //$NON-NLS-1$
	
	public static BundleContext getContext()	{
		return context;
	}
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.opensixen.dev.omvc-console"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static String getConfigurationName() {
		return new DefaultScope().getNode(PLUGIN_ID).get(CONFIG_PREF, CONFIG_DEFAULT);
	}
}
