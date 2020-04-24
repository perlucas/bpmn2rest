package org.eclipse.contributions.bpmn2rest.validation;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;

public class CustomConstraintFilter implements IConstraintFilter {

	protected static String CAT_ID = "org.eclipse.contributions.bpmn2rest.validation.category";
	
	public boolean accept(IConstraintDescriptor des, EObject o) {
		int cont = 0;
		Iterator<Category> it = des.getCategories().iterator();
		while(it.hasNext()) {
			if (it.next().getId().equals(CAT_ID))
				cont++;
		}
		return cont > 0;
	}

}
