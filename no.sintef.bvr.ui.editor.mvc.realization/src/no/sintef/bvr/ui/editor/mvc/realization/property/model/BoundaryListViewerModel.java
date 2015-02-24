package no.sintef.bvr.ui.editor.mvc.realization.property.model;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;

import bvr.BvrFactory;
import bvr.NamedElement;

public class BoundaryListViewerModel implements IBoundaryListViewerModel {

	private List<NamedElement> boundaries;

	public BoundaryListViewerModel(List<NamedElement> _boundaries) {
		boundaries = _boundaries;
		if(boundaries == null) {
			boundaries = new BasicEList<NamedElement>();
			NamedElement nameElement = BvrFactory.eINSTANCE.createToPlacement();
			nameElement.setName("adasdasd");
			boundaries.add(nameElement);
			
			nameElement = BvrFactory.eINSTANCE.createToPlacement();
			nameElement.setName("adasdasd1");
			boundaries.add(nameElement);
		}
		
	}
	
	@Override
	public List<NamedElement> getBindings() {
		return boundaries;
	}

	@Override
	public void addBoundary(NamedElement boundary) {
		boundaries.add(boundary);
	}

}
