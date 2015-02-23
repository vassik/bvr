package no.sintef.bvr.ui.editor.mvc.realization.action;

import java.util.List;

import no.sintef.bvr.ui.editor.mvc.realization.selection.BindingSelectionAdapter;
import no.sintef.bvr.ui.editor.mvc.realization.selection.RealizationSelectionProvider;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

import bvr.BoundaryElementBinding;
import bvr.BvrFactory;
import bvr.BvrPackage;
import bvr.NamedElement;
import bvr.ToBinding;


public class TestProperty extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IWorkbenchPartSite partSite = editor.getSite();
		
		IViewPart[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews();
		
		ISelectionService anotherselectionService = partSite.getWorkbenchWindow().getSelectionService();
		
		List<NamedElement> boundaryElements = new BasicEList<NamedElement>();
		BoundaryElementBinding binding = BvrFactory.eINSTANCE.createToBinding();
		binding.setName("someName");
		
		IAdaptable selection = new BindingSelectionAdapter(binding, boundaryElements);
		ISelectionProvider currentSelectionProvider = editor.getSite().getSelectionProvider();
		currentSelectionProvider.setSelection(new StructuredSelection(selection));
		return null;
	}

}
