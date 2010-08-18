package org.opensixen.omvc.console.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.omvc.riena.ServiceFactory;


public class ProjectEditorDialog extends AbstractDialog {

	private Project project;	
	private Text fName;	
			
	public ProjectEditorDialog(Shell parentShell, Project project) {
		super(parentShell);
		this.project = project;
	}
			
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Proyecto");
		setMessage("Gestiona los proyectos activos.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout(2,false);
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
		Label nameLabel = new Label(parent, SWT.NONE);
		nameLabel.setText("Nombre");		
		
		fName = new Text(parent, SWT.BORDER);
		fName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		if (project.getName() != null)	{
			fName.setText(project.getName());
		}
		
		return parent;
	}


	
	protected boolean isValidInput() {
		if (fName.getText() != null || fName.getText().length() > 0)	{
			return true;
		}
		return false;
	}

	
	protected void saveInput() {
		project.setName(fName.getText());
		IRemoteConsole console = ServiceFactory.getConsole();
		console.save(project);
	}
	
}
