package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.List;

import no.sintef.bvr.ui.editor.mvc.realization.property.model.SelectedBoundary;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import bvr.BoundaryElementBinding;
import bvr.NamedElement;
import bvr.ToBinding;

public class BindingSelectionPropertySource implements IPropertySource {


	private static final String PROPERTY_BINDING = "property.binding";
	private IPropertyDescriptor[] propertyDescriptors;
	private BoundaryElementBinding binding;
	private List<NamedElement> boundaryElements;

	public BindingSelectionPropertySource(BoundaryElementBinding _binding, List<NamedElement> _boundaryElements) {
		binding = _binding;
		boundaryElements = _boundaryElements;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		String label = (binding instanceof ToBinding) ? "ToBinding" : "FromBinding";
		
		IPropertyDescriptor descriptor = new BindingPropertyDescriptor(PROPERTY_BINDING, label);
		((BindingPropertyDescriptor) descriptor).setCurrentBinding(binding);
		((BindingPropertyDescriptor) descriptor).setBoundaryCandidates(boundaryElements);
		
		propertyDescriptors = new IPropertyDescriptor[] {
				descriptor
		};
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		System.out.println("Object getPropertyValue(Object id) " + id);
		return new SelectedBoundary();
	}

	@Override
	public boolean isPropertySet(Object id) {
		System.out.println("public boolean isPropertySet(Object id) " + id);
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		System.out.println("public void resetPropertyValue(Object id) " + id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		System.out.println("public void setPropertyValue(Object id, Object value) " + id + " "+ value);
	}

}
