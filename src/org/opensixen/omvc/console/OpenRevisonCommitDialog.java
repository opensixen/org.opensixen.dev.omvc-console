package org.opensixen.omvc.console;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opensixen.omvc.console.dialog.RevisionCommitDialog;

public class OpenRevisonCommitDialog extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RevisionCommitDialog dialog = new RevisionCommitDialog(HandlerUtil.getActiveWorkbenchWindow(
				event).getShell());
		dialog.create();
		if (dialog.open() == Window.OK) {
		}
		
		return null;
	}

}
