package no.sintef.bvr.ui.editor.mvc.realization.property.model;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import bvr.NamedElement;


public class BoundaryListContentProvider implements IStructuredContentProvider {
	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		IBoundaryListViewerModel model = (IBoundaryListViewerModel) inputElement;
		return model.getBindings().toArray();
	}

}
