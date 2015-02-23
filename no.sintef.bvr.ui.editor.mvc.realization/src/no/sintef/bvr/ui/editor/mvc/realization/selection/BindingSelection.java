package no.sintef.bvr.ui.editor.mvc.realization.selection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class BindingSelection implements IAdaptable {

	@Override
	public Object getAdapter(Class adapter) {
		System.out.println("Object getAdapter(Class adapter)" + adapter);
		if(adapter == IPropertySource.class) {
			System.out.println("IPropertySource.class is requiested");
		}
		return null;
	}

}
