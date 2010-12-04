package org.opensixen.omvc.console;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opensixen.omvc.console.dialog.ConfigDialog;

public class OpenConfigDialog extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ConfigUtil config = ConfigUtil.getInstance();
		config.loadConf();
		ConfigDialog dialog = new ConfigDialog(HandlerUtil.getActiveWorkbenchWindow(
				event).getShell(), config );
		dialog.create();
		dialog.open();
		return null;
	}

}
