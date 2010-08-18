/**
 * 
 */
package org.opensixen.omvc.console.dialog;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.riena.ServiceFactory;

/**
 * @author harlock
 *
 */
public class RevisionEditDialog extends TitleAreaDialog  {

	private Revision revision;
	private Text fDescription;
	private IRemoteConsole console;
	private ArrayList<ScriptWrapper> scriptEditors;

	/**
	 * @param parentShell
	 */
	public RevisionEditDialog(Shell parentShell, Revision revision) {
		super(parentShell);
		this.revision = revision;
		this.console = ServiceFactory.getConsole();
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
		
		Composite main = new Composite(parent, SWT.NONE);	
		main.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1);
		main.setLayoutData(gridData);		
		
		Composite editorComposite = new Composite(main, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		editorComposite.setLayout(layout);
		editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label l = new Label(editorComposite, SWT.NONE);
		l.setText("Descripcion");		
		
		fDescription = AbstractDialog.createText(editorComposite);
		fDescription.setText(revision.getDescription());
				
		Composite scriptComposite = new Composite(main, SWT.NONE);
		scriptComposite.setLayout(new GridLayout());
		scriptComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
//		FillLayout fillLayout = new FillLayout();
//		fillLayout.type = SWT.VERTICAL;
//		scriptComposite.setLayout(fillLayout);

		
		scriptEditors = new ArrayList<ScriptWrapper>();
		CTabFolder folder = new CTabFolder(scriptComposite, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Creamos controles para los scripts
		for (Script script:revision.getScripts())	{
			CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText(script.getEngine() + " " + script.getType());

			Composite tab = new Composite(folder, SWT.NONE);
			tab.setLayout(new GridLayout());
			tab.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true));
			tabItem.setControl(tab);
			Text editor = new Text(tab, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			editor.setText(script.getScript());
			editor.setSize(150, 40);
			editor.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true));
			scriptEditors.add(new ScriptWrapper(editor, script));
		}
		
		return parent;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		ArrayList<Script> scripts = new ArrayList<Script>();
		for (ScriptWrapper wrapper: scriptEditors)	{
			Script script = wrapper.getScript();
			Text editor = wrapper.getEditor();
			script.setScript(editor.getText());
			scripts.add(script);
		}
		
		revision.setScripts(scripts);
		if (console.uploadRevison(revision) != -1)		{
			setReturnCode(OK);
			close();
		}
	}


}


class ScriptWrapper	{
	private Text editor;
	private Script script;
	public ScriptWrapper(Text editor, Script script) {
		super();
		this.editor = editor;
		this.script = script;
	}
	/**
	 * @return the editor
	 */
	public Text getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(Text editor) {
		this.editor = editor;
	}
	/**
	 * @return the script
	 */
	public Script getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(Script script) {
		this.script = script;
	}	
	
	
	
	
	
}