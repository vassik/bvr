package no.sintef.bvr.ui.editor.mvc.realization.property.model;

import java.util.List;

import bvr.NamedElement;

public interface IBoundaryListViewerModel {

	public List<NamedElement> getBindings();
	
	public void addBoundary(NamedElement  boundary);
}
