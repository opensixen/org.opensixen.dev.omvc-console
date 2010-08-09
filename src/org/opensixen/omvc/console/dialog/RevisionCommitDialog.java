package org.opensixen.omvc.console.dialog;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionUploader;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.riena.ServiceFactory;

public class RevisionCommitDialog extends TitleAreaDialog{

	private IRemoteConsole console;
	
	public RevisionCommitDialog(Shell parentShell) {
		super(parentShell);
		this.console = ServiceFactory.getConsole();
	}

	private Text fDescription, fOraFile, fPgFile;
	private Combo project;
	private ArrayList<Project> projects;
	
	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Nueva Revision");
		// Set the message
		setMessage("Envia una nueva revision al sistema.", IMessageProvider.INFORMATION);		
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Guardar", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancelar", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {		
		if (fDescription.getText() == null || fDescription.getText().length() <=0 )	{
			return false;
		}
		
		if (fPgFile.getText() == null || fPgFile.getText().length() <=0 )	{
			return false;
		}
		
		if (fOraFile.getText() == null || fOraFile.getText().length() <=0 )	{
			return false;
		}
		File f = new File(fPgFile.getText());
		if (!f.exists())	{
			return false;			
		}
		
		f = new File (fOraFile.getText());
		if (!f.exists())	{
			return false;			
		}
		
		return true;
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
		l.setText("Descripcion");

		fDescription = new Text(parent, SWT.MULTI|SWT.BORDER);
		fDescription.setLayoutData(gridData);
		fDescription.setBounds(0,0,200,180);
		fDescription.setSize(200, 180);

		
		l = new Label(parent, SWT.NONE);
		l.setText("Proyecto");
		project = new Combo (parent, SWT.SIMPLE);
		// Cargamos los proyectos
		projects = console.getAll(Project.class);
		for (Project p:projects)	
			project.add(p.getName());
		project.setSize (200, 200);

		// Ficheros de cambios
		l = new Label(parent, SWT.NONE);
		l.setText("Fichero PostgreSQL");
		Composite pgComp = new Composite(parent, SWT.NONE);
		FillLayout pgLayout = new FillLayout();
		pgComp.setLayout(pgLayout);
		fPgFile = new Text(pgComp, SWT.BORDER);
		Button pgBttn = new Button(pgComp, SWT.PUSH);
		pgBttn.setText("Buscar");
		
		
		l = new Label(parent, SWT.NONE);
		l.setText("Fichero Oracle");
		Composite orComp = new Composite(parent, SWT.NONE);
		FillLayout orLayout = new FillLayout();
		orComp.setLayout(orLayout);
		fOraFile = new Text(orComp, SWT.BORDER);
		Button orBttn = new Button(orComp, SWT.PUSH);
		orBttn.setText("Buscar");
		
		return parent;
	}

	private boolean commitRevision()	{
		
		Revision revision = new Revision();
		revision.setDescription(fDescription.getText());
		revision.setProject(projects.get(project.getSelectionIndex()));
		
		ArrayList<Script> scripts = new ArrayList<Script>();
		Script pgScript = new Script();
		pgScript.setEngine(Script.ENGINE_POSTGRESQL);
		if (pgScript.loadFile(fPgFile.getText()) == false)	{
			return false;
		}
		scripts.add(pgScript);
		
		Script orScript = new Script();
		orScript.setEngine(Script.ENGINE_ORACLE);
		if (orScript.loadFile(fOraFile.getText()) == false)	{
			return false;
		}
		scripts.add(orScript);
		
		revision.setScripts(scripts);
		
		IRevisionUploader uploader = ServiceFactory.getUploader();
		return uploader.uploadRevison(revision);		
	}

	@Override
	protected void okPressed() {
		if (commitRevision())	{
			super.okPressed();
		}
	}
	
	
}
