package no.sintef.bvr.ui.editor.mvc.realization.property.model;


import org.eclipse.jface.viewers.LabelProvider;

import bvr.NamedElement;


public class BoundaryLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		String name = ((NamedElement) element).getName();
		return (!name.equals("")) ? name : "not-specified";
	}

}
