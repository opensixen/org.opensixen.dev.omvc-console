package org.opensixen.omvc.console;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.opensixen.omvc.console.view.ProjectView;
import org.opensixen.omvc.console.view.RevisionsView;
import org.opensixen.omvc.console.view.UserView;

public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "org.opensixen.dev.omvc-console.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		
		/*
		IFolderLayout projectFolder = layout.createFolder("projects", IPageLayout.TOP, 0.5f, editorArea);
		projectFolder.addPlaceholder(ProjectView.ID + ":*");
		projectFolder.addView(ProjectView.ID);
		

		
		IFolderLayout userFolder = layout.createFolder("users", IPageLayout.TOP, 0.5f, editorArea);
		userFolder.addPlaceholder(UserView.ID + ":*");
		userFolder.addView(UserView.ID);
		*/
		
		IFolderLayout folder = layout.createFolder("folder", IPageLayout.TOP, 0.5f, editorArea);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
		folder.addPlaceholder(RevisionsView.ID + ":*");
		folder.addView(RevisionsView.ID);
		
		folder.addPlaceholder(ProjectView.ID + ":*");
		folder.addView(ProjectView.ID);
		
		folder.addPlaceholder(UserView.ID + ":*");
		folder.addView(UserView.ID);
		
		
		
	}
}
