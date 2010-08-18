/**
 * 
 */
package org.opensixen.omvc.console.view;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.omvc.console.dialog.AbstractDialog;
import org.opensixen.omvc.riena.ServiceFactory;

/**
 * @author harlock
 *
 */
public class RevisionModelProvider{
	private static RevisionModelProvider content;
	private List<Revision> revisions;

	private IRevisionDownloader downloader = ServiceFactory.getDownloader();
	private IRemoteConsole console = ServiceFactory.getConsole();
	
	public RevisionModelProvider() {
		revisions = console.getRevisions();
	}
	
	public static synchronized RevisionModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new RevisionModelProvider();
		return content;
	}

	public List<Revision> getRevisions() {
		return revisions;
	}

	public Revision getRevision(int index)	{
		return revisions.get(index);
	}
	
	

}
