package no.sintef.bvr.ui.editor.mvc.realization.test.selection;

import static org.junit.Assert.*;
import no.sintef.bvr.ui.editor.mvc.realization.selection.RealizationSelectionProvider;
import no.sintef.bvr.ui.editor.mvc.realization.selection.BindingSelectionAdapter;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SelectionBasic {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelectionSelectionProvider() {
		LocalSelectionChangedListener changedListener = new LocalSelectionChangedListener();
		
		ISelectionProvider realizationSelection = new RealizationSelectionProvider();
		realizationSelection.addSelectionChangedListener(changedListener);
		IAdaptable selectionAdapter = new BindingSelectionAdapter();
		IStructuredSelection selection = new StructuredSelection(selectionAdapter);
		realizationSelection.setSelection(selection);
		
		assertEquals("SelectionProvider is incorrect", realizationSelection, changedListener.provider);
		assertEquals("Selection is incorrect", selection, changedListener.selection);
		
	}
	
	public class LocalSelectionChangedListener implements ISelectionChangedListener {

		public ISelection selection;
		public ISelectionProvider provider;

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			selection = event.getSelection();
			provider = event.getSelectionProvider();	
		}
		
	}

}
