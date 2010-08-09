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
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Developer;
import org.opensixen.omvc.riena.ServiceFactory;

public class UserEditorDialog extends AbstractDialog {

	private Developer developer;
	private Text fUser, fPassword1, fPassword2;
	
	public UserEditorDialog(Shell parentShell, Developer developer) {
		super(parentShell);
		this.developer = developer;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Usuario");
		setMessage("Gestiona los proyectos usuarios.", IMessageProvider.INFORMATION);
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
		l.setText("Nombre");		
		
		fUser = new Text(parent, SWT.BORDER);
		// Si tiene usuario, no se puede editar
		if (developer.getUserName() != null)	{
			fUser.setText(developer.getUserName());
			fUser.setEditable(false);
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Password");
		fPassword1 = new Text(parent, SWT.BORDER);
		fPassword1.setEchoChar('*');
		l = new Label(parent, SWT.NONE);
		l.setText("Repita password");
		fPassword2 = new Text(parent, SWT.BORDER);
		fPassword2.setEchoChar('*');
		
		return parent;
	}

	
	
	@Override
	protected boolean isValidInput() {
		if (fUser.getText() == null || fUser.getText().length() <= 0)	{
			return false;
		}
		if (fPassword1.getText() == null || fPassword1.getText().length() <= 0)	{
			return false;
		}
		
		if (!fPassword1.getText().equals(fPassword2.getText()))	{
			return false;
		}
		return true;
	}

	@Override
	protected void saveInput() {
		developer.setUserName(fUser.getText());
		developer.setPassword(fPassword1.getText());
		IRemoteConsole console = ServiceFactory.getConsole();
		console.save(developer);
	}

}
