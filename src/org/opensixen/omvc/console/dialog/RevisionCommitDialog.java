 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.omvc.console.dialog;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;

/**
 * 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class RevisionCommitDialog extends AbstractDialog implements SelectionListener {

	private RemoteConsoleProxy console;
	
	public RevisionCommitDialog(Shell parentShell) {
		super(parentShell);
		this.console = RemoteConsoleProxy.getInstance();
	}

	private Text fDescription, fOraFile, fPgFile;
	private Combo project, cType;
	private ArrayList<Project> projects;
	private Button pgBttn;
	private Button orBttn;
	
	private Button isMultiFile;
	
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

	@Override
	protected boolean isValidInput() {		
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
		//GridData gridData = new GridData();
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.horizontalAlignment = GridData.FILL;

		Label l = new Label(parent, SWT.NONE);
		l.setText("Descripcion");

		fDescription = new Text(parent, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
		fDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		fDescription.setBounds(0,0,200,200);
		fDescription.setSize(200, 200);

		
		l = new Label(parent, SWT.NONE);
		l.setText("Proyecto");
		project = new Combo (parent, SWT.SIMPLE);
		// Cargamos los proyectos
		projects = console.getAll(Project.class);
		for (Project p:projects)	
			project.add(p.getName());
		project.setSize (200, 200);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Tipo de Script");
		cType = new Combo(parent, SWT.SIMPLE);
		cType.add(Script.TYPE_SQL);
		cType.add(Script.TYPE_OSX);
		
		l = new Label(parent, SWT.NONE);
		l.setText("Multiples Ficheros");
		isMultiFile = new Button(parent, SWT.CHECK);
		
		// Ficheros de cambios
		l = new Label(parent, SWT.NONE);
		l.setText("Fichero PostgreSQL");
		Composite pgComp = new Composite(parent, SWT.NONE);
		
		GridLayout pgLayout = new GridLayout(2, false);
		pgComp.setLayout(pgLayout);
		pgComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		fPgFile = new Text(pgComp, SWT.BORDER);
		fPgFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		pgBttn = new Button(pgComp, SWT.PUSH);
		pgBttn.setText("Buscar");
		pgBttn.addSelectionListener(this);
		
		
		l = new Label(parent, SWT.NONE);
		l.setText("Fichero Oracle");
		Composite orComp = new Composite(parent, SWT.NONE);
		GridLayout orLayout = new GridLayout(2, false);
		orComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		orComp.setLayout(orLayout);
		fOraFile = new Text(orComp, SWT.BORDER);
		fOraFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		orBttn = new Button(orComp, SWT.PUSH);
		orBttn.setText("Buscar");
		orBttn.addSelectionListener(this);
		
		return parent;
	}

	private boolean commitRevision()	{
		
		Revision revision = new Revision();
		revision.setDescription(fDescription.getText());
		revision.setProject(projects.get(project.getSelectionIndex()));
				
		ArrayList<Script> scripts = new ArrayList<Script>();
		if (isMultiFile.getSelection())	{
			scripts.addAll(loadMultiFileScripts());
		}
		else {
			scripts.addAll(loadScripts());
		}		
				
		revision.setScripts(scripts);
		
		RemoteConsoleProxy console = RemoteConsoleProxy.getInstance();
		if (console.uploadRevison(revision) == -1)	{
			return false;
		}
		return true;		
	}

		
	private ArrayList<Script> loadScripts()	{
		ArrayList<Script> scripts = new ArrayList<Script>();
		
		// Postgres
		Script pgScript = Script.getScript(Script.ENGINE_POSTGRESQL, cType.getText(), fPgFile.getText());				
		scripts.add(pgScript);
		
		// Oracle
		Script orScript = Script.getScript(Script.ENGINE_ORACLE, cType.getText(), fOraFile.getText());		
		scripts.add(orScript);
		return scripts;
	}
	
	private ArrayList<Script> loadMultiFileScripts()	{
		ArrayList<Script> scripts = new ArrayList<Script>();
		// Postgres
		File pgDir = new File(fPgFile.getText());
		for (String fileName: pgDir.list())	{
			Script script = Script.getScript(Script.ENGINE_POSTGRESQL, cType.getText(), pgDir + "/" + fileName);
			scripts.add(script);
		}
		
		// Oracle
		File oraDir = new File(fOraFile.getText());
		for (String fileName: oraDir.list())	{
			Script script = Script.getScript(Script.ENGINE_ORACLE, cType.getText(), oraDir + "/" + fileName);
			scripts.add(script);
		}
		
		return scripts;
	}
		
		

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.dialog.AbstractDialog#saveInput()
	 */
	@Override
	protected void saveInput() {
		commitRevision();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		String[] filterExt = { "*.sql;*.SQL;.txt;*.TXT" };
		
		
		if (e.getSource().equals(orBttn))	{
			 FileDialog fd = new FileDialog(getParentShell(), SWT.OPEN);
		     fd.setText("Open");
		      
		      fOraFile.setText(fd.open());
		}
		
		if (e.getSource().equals(pgBttn))	{
			 FileDialog fd = new FileDialog(getParentShell(), SWT.OPEN);
		     fd.setText("Open");
		      //String[] filterExt = { "*.sql", "*.SQL", ".txt", "*.TXT" };
		     if (!isMultiFile.getSelection())	{	    	  
		    	  fd.setFilterExtensions(filterExt);		    	  
		      }
		      fPgFile.setText(fd.open());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
