package org.eclipse.contributions.bpmn2rest.acceleo.module;


import java.util.Iterator;
import org.eclipse.bpmn2.Operation;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;

public class BPMN2ServiceFacade {
	
	public static final String HTTP_METHOD_ATTRIBUTE = "rest_method";
	public static final String HTTP_URL_ATTRIBUTE = "rest_url";
	public static final String PREPEND_CONTROLLER = "rest_prepend_controller";
	private static final String[] methods = {"get", "post", "put", "delete", "patch", "options"};
	
	/**
	 * returns the http method configured for this service
	 * 
	 * @param Operation op
	 * @return String
	 */
	public String getServiceHttpMethod(Operation op) {
		Iterator<Entry> it = op.getAnyAttribute().iterator();
		String method = "";
		while(it.hasNext()) {
			Entry e = it.next();
			if (e.getEStructuralFeature().getName().equals(HTTP_METHOD_ATTRIBUTE)) {
				method = (String) e.getValue();
				break;
			}
		}
		for (String validMethod : methods) {
			if (method.toLowerCase().equals(validMethod)) return validMethod;
		}
		return "get"; // default value if property not exists or is invalid
	}
	
	/**
	 * returns the service url defined for this operation
	 * 
	 * @param Operation op
	 * @param Participant p
	 * @return String
	 */
	public String getServiceHttpUrl(Operation op, Participant p) {
		Iterator<Entry> it = op.getAnyAttribute().iterator();
		String baseUrl = "";
		CommonHelper helper = new CommonHelper();
		boolean prependControllerName = false;
		while(it.hasNext()) {
			Entry e = it.next();
			if (e.getEStructuralFeature().getName().equals(HTTP_URL_ATTRIBUTE)) baseUrl = (String) e.getValue();
			if (e.getEStructuralFeature().getName().equals(PREPEND_CONTROLLER)) prependControllerName = Boolean.parseBoolean((String) e.getValue());
		}
		if (baseUrl.length() == 0) baseUrl = helper.cleanAndLowerString(op.getName());
		if (!baseUrl.startsWith("/")) baseUrl = "/" + baseUrl;
		if (prependControllerName) baseUrl = helper.cleanAndLowerString(p.getName()) + baseUrl;
		return baseUrl;
	}
	
}
