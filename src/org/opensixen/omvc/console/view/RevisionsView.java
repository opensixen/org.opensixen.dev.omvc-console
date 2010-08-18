/**
 * 
 */
package org.opensixen.omvc.console.view;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.omvc.console.dialog.AbstractDialog;
import org.opensixen.omvc.console.dialog.RevisionCommitDialog;
import org.opensixen.omvc.console.dialog.RevisionEditDialog;
import org.opensixen.omvc.riena.ServiceFactory;

/**
 * @author harlock
 *
 */
public class RevisionsView extends ViewPart implements SelectionListener{
	public static final String ID = "org.opensixen.omvc.console.view.revisionsView";
	
	private TableViewer viewer;

	private Button addBtn;

	private Button editBtn;

	private Button deleteBtn;

	private Composite parent;

	private Table table;

	private IRemoteConsole console;

	private RevisionModelProvider modelProvider;

	private Button reloadBtn;




	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		console = ServiceFactory.getConsole();
		createViewer(parent);
		modelProvider = RevisionModelProvider.getInstance();
		viewer.setInput(modelProvider.getRevisions());		
	}

	private void createViewer(Composite parent) {
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
		/*
		deleteBtn = new Button(btnComposite, SWT.PUSH);
		deleteBtn.setText("Eliminar");
		deleteBtn.addSelectionListener(this);
		*/
		
		viewer = new TableViewer(top, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		createColumns(viewer);
		viewer.setContentProvider(new RevisionContentProvider());
		viewer.setLabelProvider(new RevisionLabelProvider());
	}

	// This will create the columns for the table
	private void createColumns(TableViewer viewer) {

		String[] titles = { "Revision", "Fecha", "Proyecto",  "Descripcion" };
		int[] bounds = { 100, 100, 100, 100 };

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}


	private void reload()	{
		viewer.refresh();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// Añadir
		if (e.getSource().equals(addBtn))	{
			RevisionCommitDialog dialog = new RevisionCommitDialog(parent.getShell());
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
			
			
			
			Revision revision = modelProvider.getRevision(index);
			RevisionEditDialog dialog = new RevisionEditDialog(parent.getShell(), revision);
			
			if (dialog.open() == SWT.OK)	{
				reload();
			}			
			reload();
		}
		// Actualizar
		else if (e.getSource().equals(reloadBtn))	{
			reload();
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
