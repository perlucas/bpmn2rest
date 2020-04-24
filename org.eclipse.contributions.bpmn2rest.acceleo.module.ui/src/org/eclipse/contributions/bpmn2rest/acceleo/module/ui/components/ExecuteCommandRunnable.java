package org.eclipse.contributions.bpmn2rest.acceleo.module.ui.components;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.validation.ResourcePropertyTester;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.contributions.bpmn2rest.acceleo.module.ui.Activator;
import org.eclipse.contributions.bpmn2rest.acceleo.module.ui.common.GenerateAll;
import org.eclipse.contributions.bpmn2rest.validation.CustomConstraintFilter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ExecuteCommandRunnable implements IRunnableWithProgress {
	protected URI modelUri;
	protected File outputFolder;
	
	public ExecuteCommandRunnable(URI model, File folder) {
		this.modelUri = model;
		this.outputFolder = folder;
	}
	
	/**
	 * executes the transform command
	 * 
	 * @param monitor
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		String message;
		boolean success;
		try {
				
			//Register the BPMN2ResourceFactory in Factory registry
			Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
			reg.getExtensionToFactoryMap().put("bpmn2", new Bpmn2ResourceFactoryImpl());

			// load the resource and resolve 
			ResourceSet rs = new ResourceSetImpl();
			rs.getPackageRegistry().put(org.eclipse.bpmn2.Bpmn2Package.eINSTANCE.getNsURI(), org.eclipse.bpmn2.Bpmn2Package.eINSTANCE);
			Resource res = rs.createResource(this.modelUri);
			
			// load it
			res.load(null);
			
			EObject model = res.getContents().get(0);
			
			// validate the model
			IStatus rr = getValidationResult(model);
			
			if (rr.isOK()) {
				GenerateAll generator = new GenerateAll(this.modelUri, this.outputFolder, getArguments());
				generator.doGenerate(monitor);
				message = "BPMN2REST execution finished successfully";
				success = true;
			} else {
				IStatus r0 = rr.isMultiStatus() ? rr.getChildren()[0] : rr;
				message = "BPMN2REST validation failed: " + r0.getMessage();
				success = false;
			}
			
			// display finished message
			PlatformUI.getWorkbench().getDisplay().asyncExec(new ShowFeedBackToUser(
				message,
				success
			));
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
			
			// display failed message
			PlatformUI.getWorkbench().getDisplay().asyncExec(new ShowFeedBackToUser(
				"BPMN2REST execution failed: " + status.getMessage(), 
				false
			));
		}
	}
	
	/**
	 * validates the model and returns the validation result
	 * 
	 * @return IStatus
	 * @throws IOException 
	 */
	private IStatus getValidationResult(EObject model) throws IOException {
		DocumentRoot root = (DocumentRoot) model;
		
		// get elements to validate
		ArrayList<EObject> objects = new ArrayList<EObject>();
		
		// get Definitions
		Definitions def = root.getDefinitions();
		
		// check if this model has been created with the bpmn2rest target runtime
		ResourcePropertyTester tester = new ResourcePropertyTester();
		Object[] args = {};
		
		if (!tester.test(def, "targetRuntimeId", args, "org.eclipse.contributions.bpmn2rest.modeler.runtime")) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "The BPMN model has not been created with the BPMN2REST Target Runtime");
		}
		objects.add(def);
		
		// get Collaborations
		for (RootElement element : root.getDefinitions().getRootElements()) {
			if (element instanceof Collaboration) {
				objects.add(element);
			}
		}
		
		// validate all elements (Definitions & Collaboration)
		IBatchValidator vv = ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		vv.addConstraintFilter(new CustomConstraintFilter());
		return vv.validate(objects);
	}

	/**
	 * Computes the arguments of the generator.
	 * 
	 * @return the arguments
	 * @generated
	 */
	protected List<? extends Object> getArguments() {
		return new ArrayList<String>();
	}
	
	/**
	 * runnable that opens a window showing the execution result to user
	 * @author lucas
	 *
	 */
	public class ShowFeedBackToUser implements Runnable {
		protected String msg;
		protected boolean executionSucceeded;
		protected static final String title = "BPMN2REST Extension";
		
		/**
		 * creates this runnable
		 * @param m - message
		 * @param success - true if is a successfull state
		 */
		public ShowFeedBackToUser(String m, boolean success) {
			this.msg = m;
			this.executionSucceeded = success;
		}

		/**
		 * shows the execution result
		 * @return void
		 */
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			if (shell == null) return;
			if (this.executionSucceeded) {
				MessageDialog.openInformation(shell,title,this.msg);				
			} else {
				MessageDialog.openError(shell, title, this.msg);
			}
		}
		
	}

}
