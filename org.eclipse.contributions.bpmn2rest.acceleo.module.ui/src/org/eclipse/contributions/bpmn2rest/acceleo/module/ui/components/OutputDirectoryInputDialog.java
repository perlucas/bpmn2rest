package org.eclipse.contributions.bpmn2rest.acceleo.module.ui.components;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


public class OutputDirectoryInputDialog extends InputDialog {
	
	protected URI inputFile;
		
	/**
	 * constructor
	 * 
	 * @param parentShell
	 * @param dialogTitle
	 * @param dialogMessage
	 * @param initialValue
	 * @param validator
	 */
	public OutputDirectoryInputDialog(
			URI inputFile,
			Shell parentShell, 
			String dialogTitle, 
			String dialogMessage, 
			String initialValue,
			IInputValidator validator
	) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		this.inputFile = inputFile;
	}
	
	/**
	 * metodo constructor del Dialog
	 * 
	 * @param defaultDirectory String
	 * @return InputDialog
	 */
	public static OutputDirectoryInputDialog create(String defaultDirectory, URI inputFileUri) {
		return new OutputDirectoryInputDialog(
			inputFileUri,
			new Shell(),
			"BPMN 2 REST: output directory",
			"Enter the output directory",
			defaultDirectory,
			new IInputValidator() {

				public String isValid(String newText) {
					File ff = new File(newText);
					if (!ff.exists() || !ff.isDirectory()) {
						return "Specified directory not found";
					}
					return null;
				}
				
			}
		);
	}
	
	@Override
	protected void okPressed() {		
		try {
			this.close();
			File file = new File(this.getValue());
			ExecuteCommandRunnable operation = new ExecuteCommandRunnable(this.inputFile, file);
			PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
		} catch(Exception e) {
			e.printStackTrace();
		}
		super.okPressed();	
	}
}
