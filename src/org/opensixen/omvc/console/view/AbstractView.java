package org.opensixen.omvc.console.view;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
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

public abstract class AbstractView extends ViewPart implements SelectionListener{

	protected IRemoteConsole console;
		
	private Composite parent;
	private Button addBtn, editBtn, deleteBtn;
	
	protected Table table;

	private Button reloadBtn;
		
	public AbstractView() {
		super();
		this.console = ServiceFactory.getConsole();
	}
	
	public void createPartControl(Composite parent) {
		this.parent = parent;
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		top.setLayout(layout);
		// Botones
		Composite btnComposite = new Composite(top, SWT.NONE);
		btnComposite.setLayout(new FillLayout());
		reloadBtn = new Button(btnComposite, SWT.PUSH);
		reloadBtn.setText("Recargar");
		reloadBtn.addSelectionListener(this);
		addBtn = new Button(btnComposite, SWT.PUSH);
		addBtn.setText("Nuevo");
		addBtn.addSelectionListener(this);
		editBtn = new Button(btnComposite, SWT.PUSH);
		editBtn.setText("Editar");
		editBtn.addSelectionListener(this);
		deleteBtn = new Button(btnComposite, SWT.PUSH);
		deleteBtn.setText("Eliminar");
		deleteBtn.addSelectionListener(this);
		

		// Tabla
		table = new Table (top, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		// Obtenemos los titulos de la ventana
		String[] titles = getColumnsTitles();
		
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	
		
		// Rellenamos la tabla
		fillTable(table);				
				
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack();
		}			
	}

	
	public void setFocus() {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// Añadir
		if (e.getSource().equals(addBtn))	{
			TitleAreaDialog dialog = getNewRecordDialog(parent.getShell());
			if (dialog.open() == SWT.OK)	{
				reload();
			}
			reload();
		}
		// Editar
		else if (e.getSource().equals(editBtn))	{
			int index = table.getSelectionIndex(); 
			if (index == -1)	{
				return;
			}
			AbstractDialog dialog = getEditDialog(parent.getShell(), index);
			
			if (dialog.open() == SWT.OK)	{
				reload();
			}			
			reload();
		}
		// Eliminar
		else if (e.getSource().equals(deleteBtn))	{
			
		}
		// Eliminar
		else if (e.getSource().equals(reloadBtn))	{
			reload();
		}
	
	}
	
	
	/**
	 * Limpia la tabla y llama a fillTable
	 */
	protected void reload()	{
		table.clearAll();
		fillTable(table);
	}
	
	protected abstract AbstractDialog getNewRecordDialog(Shell shell);
	protected abstract AbstractDialog getEditDialog(Shell shell, int index);
	protected abstract String[] getColumnsTitles();
	protected abstract void fillTable(Table table);
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}


	
	
}
