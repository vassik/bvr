package no.sintef.bvr.ui.editor.mvc.realization.property.model;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import bvr.BoundaryElementBinding;

public interface ISelectedBoundary {
	
	public BoundaryElementBinding getSelectedBoundary();
	
	public List<EObject> getBindingEObjects();

}
