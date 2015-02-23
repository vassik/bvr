package no.sintef.bvr.ui.editor.mvc.realization.selection;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class BindingPropertyDescriptor extends PropertyDescriptor {

	public BindingPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		return super.createPropertyEditor(parent);
	}
	
	

}
