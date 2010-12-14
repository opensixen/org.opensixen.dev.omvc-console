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

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
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
public class RevisionEditDialog extends AbstractDialog implements SelectionListener  {

	private Revision revision;
	private RemoteConsoleProxy console;
	private Table table;
	private String[] titles = {"ID", "Nombre", "Engine", "Tipo", "SQL"};
	private Text fName;
	private Combo cEngine;
	private Combo cType;
	private Text editor;
	private Script selected;

	/**
	 * @param parentShell
	 */
	public RevisionEditDialog(Shell parentShell, Revision revision) {
		super(parentShell);
		this.revision = revision;
		this.console = RemoteConsoleProxy.getInstance();
		revision.setScripts(console.getScripts(revision));
	}

	@Override
	public void create() {
		super.create();
		setTitle("Revision");
		setMessage("Edita la informacion de la revision.", IMessageProvider.INFORMATION);
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		parent.setLayout(new GridLayout());
	
		table = new Table (parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableData.heightHint = 100;
		table.setLayoutData(tableData);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		table.addSelectionListener(this);
		
		
		// Obtenemos los titulos de la ventana
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		updateScriptsTable();

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack();
		}	
		
		// Composite with Revision data
		Composite infoComposite = new Composite(parent, SWT.NONE);
		infoComposite.setLayout(new GridLayout(2, false));
		GridData infoData = new GridData(SWT.FILL, SWT.FILL, true, true);
		infoComposite.setLayoutData(infoData);
		
		Label label = new Label(infoComposite, SWT.NONE);
		label.setText("Nombre");
		label.setLayoutData(new GridData(SWT.FILL));
		fName = createText(infoComposite);
		label = new Label(infoComposite, SWT.NONE);
		label.setText("Engine");
		cEngine = new Combo (infoComposite, SWT.SIMPLE);
		cEngine.add(Script.ENGINE_POSTGRESQL);
		cEngine.add(Script.ENGINE_ORACLE);
		
		label = new Label(infoComposite, SWT.NONE);
		label.setText("Tipo de Script");
		cType = new Combo(infoComposite, SWT.SIMPLE);
		cType.add(Script.TYPE_SQL);
		cType.add(Script.TYPE_OSX);
		
		// Text Editor
		editor = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData editorData = new GridData(SWT.FILL, SWT.FILL, true, true);
		editorData.heightHint = 100;
		editor.setLayoutData(editorData);
		
		return parent;
	}

	private void updateScriptsTable()	{
		int previewSize = 60;
		table.removeAll();
		for (Script script: revision.getScripts())	{
			int i=0;
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(i++, script.getScript_ID()+"");
			item.setText(i++, script.getEngine());
			item.setText(i++, script.getType());
			item.setText(i++, script.getName()+"");
			String s = script.getScript();
			s = s.replaceAll("\n", " ");
			if (s == null)	{
				s = "";
			}
			else if (s.length() > previewSize +1)	{
				s = s.substring(0, previewSize) + "...";
			}
			item.setText(i++, s);
		}				
	}
	
	private void loadScriptData()	{
		if (selected.getName() != null)	{
			fName.setText(selected.getName());
		}
		else {
			fName.setText("");
		}
		
		if (selected.getScript() != null)	{
			editor.setText(selected.getScript());
		}
		else {
			editor.setText("");
		}
		
		cEngine.setText(selected.getEngine());
		cType.setText(selected.getType());				
	}
	
	
	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.dialog.AbstractDialog#isValidInput()
	 */
	@Override
	protected boolean isValidInput() {
		if (selected == null)	{
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.dialog.AbstractDialog#saveInput()
	 */
	@Override
	protected void saveInput() {
		selected.setEngine(cEngine.getText());
		selected.setType(cType.getText());
		selected.setName(fName.getText());
		selected.setScript(editor.getText());
		console.save(selected);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(table))	{
			int index = table.getSelectionIndex();
			selected = revision.getScripts().get(index);
			loadScriptData();
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