package org.opensixen.omvc.console.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensixen.dev.omvc.model.Developer;
import org.opensixen.omvc.console.Application;
import org.opensixen.omvc.console.ConfigUtil;
import org.opensixen.riena.client.proxy.RienaServerProxy;

public class ConfigDialog extends AbstractDialog {

	private ConfigUtil config;
	private Text fHost, fUser, fPassword, fPort;
	
	public ConfigDialog(Shell parentShell, ConfigUtil config) {
		super(parentShell);
		this.config = config;
	}
	
	@Override
	public void create() {
		super.create();
		setTitle("Configuracion");
		setMessage("Edite sus opciones de configuracion.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
		Label l = new Label(parent, SWT.NONE);
		l.setText("Host");				
		fHost = new Text(parent, SWT.BORDER);
		if (config.getHost() != null)	{
			fHost.setText(config.getHost());
		}
		l = new Label (parent, SWT.NONE);
		l.setText("Puerto");
		fPort = new Text(parent, SWT.BORDER);
		if (config.getPort() != null)	{
			fPort.setText(config.getPort());
		}
		else {
			fPort.setText("8080");
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Usuario");
		fUser = new Text(parent, SWT.BORDER);
		if (config.getUser() != null)	{
			fUser.setText(config.getUser());
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Password");
		fPassword = new Text(parent, SWT.BORDER);
		fPassword.setEchoChar('*');
		if (config.getPassword() != null)	{
			fPassword.setText(config.getPassword());
		}
		
		
		
		return parent;
	}

	
	@Override
	protected boolean isValidInput() {
		
		if (fHost.getText() != null && fPort.getText() != null && fUser.getText() != null && fPassword.getText() != null)	{
			return true;	
		}
		return false;
		
	}

	@Override
	protected void saveInput() {
		config.setHost(fHost.getText());
		config.setPort(fPort.getText());
		config.setUser(fUser.getText());
		config.setPassword(fPassword.getText());
		config.saveConf();
		RienaServerProxy.restartConnection();
	}

}
