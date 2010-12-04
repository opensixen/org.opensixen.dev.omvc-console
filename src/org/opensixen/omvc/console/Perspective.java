package org.opensixen.omvc.console;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.opensixen.omvc.console.view.ProjectView;
import org.opensixen.omvc.console.view.RevisionView;
import org.opensixen.omvc.console.view.UserView;

public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "org.opensixen.dev.omvc-console.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		//layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		
		IFolderLayout folder = layout.createFolder("folder", IPageLayout.TOP, 0.5f, editorArea);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
		folder.addPlaceholder(RevisionView.ID + ":*");
		folder.addView(RevisionView.ID);
		
		folder.addPlaceholder(ProjectView.ID + ":*");
		folder.addView(ProjectView.ID);
		
		folder.addPlaceholder(UserView.ID + ":*");
		folder.addView(UserView.ID);
				
		
	}
}
