package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;

import bvr.BoundaryElementBinding;
import bvr.NamedElement;

public class BindingSelectionAdapter implements IAdaptable {

	private BoundaryElementBinding binding;
	private List<NamedElement> boundaryElements;

	public BindingSelectionAdapter() {
	}
	
	public BindingSelectionAdapter(BoundaryElementBinding _binding, List<NamedElement> _boundaryElements) {
		binding = _binding;
		boundaryElements = _boundaryElements;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == IPropertySource.class) {			
			System.out.println("IPropertySource.class is requiested");
			IPropertySource propertySource = new BindingSelectionPropertySource(binding, boundaryElements);
			return propertySource;
		}
		return null;
	}

}
