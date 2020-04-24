package org.eclipse.contributions.bpmn2rest.acceleo.module.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.contributions.bpmn2rest.acceleo.module.ui.components.OutputDirectoryInputDialog;


public class TransformBpmn2RestHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get workbench window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// set structured selection
		IStructuredSelection structured = (IStructuredSelection) window.getSelectionService().getSelection();
			 
		//check if it is an IFile
		if (structured.getFirstElement() instanceof IFile) {
			// get the selected file
			IFile file = (IFile) structured.getFirstElement();
			
			// create input dialog
			OutputDirectoryInputDialog inputWindow = OutputDirectoryInputDialog.create(
				file.getRawLocation().removeLastSegments(1).toString(),
				URI.createPlatformResourceURI(file.getFullPath().toString(), true)
			);
			
			// open the window and delegate the execution
			inputWindow.open();
		}
		
		return null;
	}
}
