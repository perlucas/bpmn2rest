package org.eclipse.contributions.bpmn2rest.modeler;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.ui.IEditorInput;
import org.xml.sax.InputSource;
import org.eclipse.bpmn2.modeler.ui.AbstractBpmn2RuntimeExtension;

public class Bpmn2RestRuntimeExtension extends AbstractBpmn2RuntimeExtension {
	
	public final static String namespace = "http://www.bpmn2rest.org";
	
	public String getTargetNamespace(Bpmn2DiagramType arg0) {
		return namespace;
	}

	public boolean isContentForRuntime(IEditorInput input) {
		InputSource source = new InputSource(FileService.getInputContents(input));
		RootElementParser parser = new RootElementParser(namespace);
		parser.parse(source);
		return parser.getResult();
	}

}
