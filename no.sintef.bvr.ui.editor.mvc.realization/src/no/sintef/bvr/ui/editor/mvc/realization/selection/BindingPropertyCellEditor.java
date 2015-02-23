package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.List;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import bvr.BoundaryElementBinding;
import bvr.NamedElement;

public class BindingPropertyCellEditor extends DialogCellEditor {

	private BoundaryElementBinding binding;
	private List<NamedElement> boundaryElements;

	protected BindingPropertyCellEditor(Composite _parent, BoundaryElementBinding _binding, List<NamedElement> _boundaryElements) {
		super(_parent);
		binding = _binding;
		boundaryElements = _boundaryElements;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
		return null;
	}

}
