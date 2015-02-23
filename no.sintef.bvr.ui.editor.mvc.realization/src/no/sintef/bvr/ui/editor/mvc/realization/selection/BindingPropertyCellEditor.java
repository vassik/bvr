package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.List;

import no.sintef.bvr.ui.editor.mvc.realization.property.BindingPropertyEditorDialog;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


import bvr.BoundaryElementBinding;
import bvr.NamedElement;

public class BindingPropertyCellEditor extends DialogCellEditor {

	private BoundaryElementBinding binding;
	private List<NamedElement> boundaryElements;
	private Composite parent;

	protected BindingPropertyCellEditor(Composite _parent, BoundaryElementBinding _binding, List<NamedElement> _boundaryElements) {
		super(_parent);
		binding = _binding;
		boundaryElements = _boundaryElements;
		parent = _parent;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		BindingPropertyEditorDialog dialog = new BindingPropertyEditorDialog(cellEditorWindow.getShell(), SWT.NONE);
		Object data = dialog.open();
        return data;
     }



}
