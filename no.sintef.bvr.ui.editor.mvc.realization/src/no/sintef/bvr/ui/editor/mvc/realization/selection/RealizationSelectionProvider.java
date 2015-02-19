package no.sintef.bvr.ui.editor.mvc.realization.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class RealizationSelectionProvider implements ISelectionProvider {
	
	private List<ISelectionChangedListener> selectionListener;
	private ISelection currentSelection;
	
	public RealizationSelectionProvider() {
		selectionListener = new ArrayList<ISelectionChangedListener>();
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListener.add(listener);

	}

	@Override
	public ISelection getSelection() {
		return currentSelection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListener.remove(listener);

	}

	@Override
	public void setSelection(ISelection selection) {
		currentSelection = selection;
		for(ISelectionChangedListener listener : selectionListener) {
			listener.selectionChanged(new SelectionChangedEvent(this, selection));
		}
		
	}

}
