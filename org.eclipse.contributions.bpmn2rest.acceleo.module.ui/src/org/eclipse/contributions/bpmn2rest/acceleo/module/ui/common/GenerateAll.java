/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.contributions.bpmn2rest.acceleo.module.ui.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil;
import org.eclipse.contributions.bpmn2rest.acceleo.module.main.Main;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;


/**
 * Main entry point of the 'BPMN2REST' generation module.
 */
public class GenerateAll {
	
	private File targetFolder;
	
	private List<?> arguments;

	private URI modelURI;
	
	/**
	 * Constructor.
	 * 
	 * @param modelURI
	 *            is the URI of the model.
	 * @param targetFolder
	 *            is the output folder
	 * @param arguments
	 *            are the other arguments
	 * @throws IOException
	 *             Thrown when the output cannot be saved.
	 * @generated
	 */
	public GenerateAll(URI modelURI, File targetFolder, List<? extends Object> arguments) throws IOException {
		this.modelURI = modelURI;
		this.targetFolder = targetFolder;
		this.arguments = arguments;
	}

	/**
	 * Launches the generation.
	 *
	 * @param monitor
	 *            This will be used to display progress information to the user.
	 * @throws IOException
	 *             Thrown when the output cannot be saved.
	 * @generated
	 */
	public void doGenerate(IProgressMonitor monitor) throws IOException {
		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}
		
		monitor.subTask("Loading...");
		Main gen0 = new Main(modelURI, targetFolder, arguments);
		monitor.worked(1);
		String generationID = AcceleoLaunchingUtil.computeUIProjectID("org.eclipse.contributions.bpmn2rest.acceleo.module", "org.eclipse.contributions.bpmn2rest.acceleo.module.main.Main", this.modelURI.toString(), targetFolder.getAbsolutePath().toString(), new ArrayList<String>());
		gen0.setGenerationID(generationID);
		gen0.doGenerate(BasicMonitor.toMonitor(monitor));
	}

}
