/**
 * 
 */
package org.opensixen.omvc.console.view;

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.omvc.console.dialog.AbstractDialog;
import org.opensixen.omvc.console.dialog.RevisionCommitDialog;
import org.opensixen.omvc.console.dialog.RevisionEditDialog;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class RevisionView extends AbstractView {

	public static final String ID = "org.opensixen.omvc.console.view.revisionView";
	
	private List<Revision> revisions;
	
	
	public RevisionView() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.view.AbstractView#getNewRecordDialog(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected AbstractDialog getNewRecordDialog(Shell shell) {
		return new RevisionCommitDialog(shell);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.view.AbstractView#getEditDialog(org.eclipse.swt.widgets.Shell, int)
	 */
	@Override
	protected AbstractDialog getEditDialog(Shell shell, int index) {
		Revision revision = revisions.get(index);
		return new RevisionEditDialog(shell, revision);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.view.AbstractView#getColumnsTitles()
	 */
	@Override
	protected String[] getColumnsTitles() {
		String[] titles = { "Revision", "Fecha", "Proyecto",  "Descripcion" };
		return titles;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.omvc.console.view.AbstractView#fillTable(org.eclipse.swt.widgets.Table)
	 */
	@Override
	protected void fillTable(Table table) {
		revisions = console.getRevisions();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		for (Revision revision:revisions)	{
			int i=0;
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(i++, revision.getRevision_ID()+"");
			item.setText(i++, df.format(revision.getCreated()));
			item.setText(i++, revision.getProject().getName());
			item.setText(i++, revision.getDescription());						
		}		
	}

}
