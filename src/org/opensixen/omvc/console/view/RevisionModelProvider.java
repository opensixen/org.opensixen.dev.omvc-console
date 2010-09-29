/**
 * 
 */
package org.opensixen.omvc.console.view;

import java.util.List;


import org.opensixen.dev.omvc.model.Revision;

import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;
import org.opensixen.omvc.client.proxy.RevisionDownloaderProxy;


/**
 * @author harlock
 *
 */
public class RevisionModelProvider{
	private static RevisionModelProvider content;
	private List<Revision> revisions;

	private RevisionDownloaderProxy downloader = RevisionDownloaderProxy.getInstance();
	private RemoteConsoleProxy console = RemoteConsoleProxy.getInstance();
	
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
