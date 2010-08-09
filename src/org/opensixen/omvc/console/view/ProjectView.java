package org.opensixen.omvc.console.view;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.opensixen.dev.omvc.interfaces.IPO;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.omvc.console.dialog.AbstractDialog;
import org.opensixen.omvc.console.dialog.ProjectEditorDialog;
import org.opensixen.omvc.riena.ServiceFactory;

public class ProjectView extends AbstractView {
	
	public static final String ID = "org.opensixen.dev.omvc-console.projectView";
	
	private ArrayList<Project> projects;
	
	public ProjectView() {
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
		projects = console.getAll(Project.class);
		
		for (Project project:projects)	{
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(0, new Integer (project.getProject_ID()).toString());
			item.setText(1, project.getName());
		}
		
	}

	@Override
	protected AbstractDialog getNewRecordDialog(Shell shell) {
		ProjectEditorDialog dialog = new ProjectEditorDialog(shell, new Project());
		return dialog;
	}

	@Override
	protected AbstractDialog getEditDialog(Shell shell, int index) {
		Project project = (Project) projects.get(index);
		ProjectEditorDialog dialog = new ProjectEditorDialog(shell, project);
		return dialog;
	}

}
