package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import bvr.BoundaryElementBinding;
import bvr.NamedElement;

public class BindingPropertyDescriptor extends PropertyDescriptor {

	private BoundaryElementBinding binding;
	private List<NamedElement> boundaryElements;
	private BindingPropertyCellEditor editor;

	public BindingPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		editor = new BindingPropertyCellEditor(parent, binding, boundaryElements);
		return editor;
	}

	public void setCurrentBinding(BoundaryElementBinding _binding) {
		binding = _binding;
		
	}

	public void setBoundaryCandidates(List<NamedElement> _boundaryElements) {
		boundaryElements = _boundaryElements;
	}
	
}
