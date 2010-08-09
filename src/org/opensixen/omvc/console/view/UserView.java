package org.opensixen.omvc.console.view;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.opensixen.dev.omvc.model.Developer;
import org.opensixen.omvc.console.dialog.AbstractDialog;
import org.opensixen.omvc.console.dialog.UserEditorDialog;

public class UserView extends AbstractView {
	
	public static final String ID = "org.opensixen.dev.omvc-console.userView";
	
	private ArrayList<Developer> developers;
	
	public UserView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String[] getColumnsTitles() {
		String[] titles = {"ID", "Nombre"}; 
		return titles;
	}

	@Override
	protected void fillTable(Table table) {
		developers = console.getAll(Developer.class);		
		
		for (Developer developer:developers)	{
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(0, new Integer (developer.getDeveloper_ID()).toString());
			item.setText(1, developer.getUserName());
		}		
	}

	@Override
	protected AbstractDialog getNewRecordDialog(Shell shell) {
		UserEditorDialog dialog = new UserEditorDialog(shell, new Developer());
		return dialog;
	}

	@Override
	protected AbstractDialog getEditDialog(Shell shell, int index) {
		Developer developer = developers.get(index);
		UserEditorDialog dialog = new UserEditorDialog(shell, developer);
		return dialog;
	}

}
