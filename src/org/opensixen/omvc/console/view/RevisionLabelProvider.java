/**
 * 
 */
package org.opensixen.omvc.console.view;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;


/**
 * @author harlock
 *
 */
public class RevisionLabelProvider implements ITableLabelProvider {

	HashMap<Integer, String> projectsIndex;
	
	public RevisionLabelProvider() {
		super();
		//init();
	}

	private void init()	{
		RemoteConsoleProxy console = RemoteConsoleProxy.getInstance();
		List<Project> projects = console.getProjects();
		projectsIndex = new HashMap<Integer, String>();
		for (Project project:projects)	{
			projectsIndex.put(project.getProject_ID(), project.getName());
		}
	}
	
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		Revision revision = (Revision) element;
		
		switch (columnIndex) {
		case 0:
			return "#" + revision.getRevision_ID();
		case 1:
			return df.format(revision.getCreated());
		case 2:
				return revision.getProject().getName();
		case 3:
			return revision.getDescription();
		default:
			throw new RuntimeException("Esta solicitando una columna inexsitente");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
