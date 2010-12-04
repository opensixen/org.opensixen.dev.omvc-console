/**
 * 
 */
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
 * @author harlock
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