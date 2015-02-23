package no.sintef.bvr.ui.editor.mvc.realization.test.selection;

import static org.junit.Assert.*;

import java.util.List;

import no.sintef.bvr.ui.editor.mvc.realization.selection.BindingPropertyCellEditor;
import no.sintef.bvr.ui.editor.mvc.realization.selection.BindingPropertyDescriptor;
import no.sintef.bvr.ui.editor.mvc.realization.selection.BindingSelectionPropertySource;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bvr.BoundaryElementBinding;
import bvr.NamedElement;

public class BindingSelectionPropertySourceTest {

	private IPropertySource bindingPropSource;
	private List<NamedElement> boundaryElements;
	private BoundaryElementBinding binding;

	@Before
	public void setUp() throws Exception {
		boundaryElements = new BasicEList<NamedElement>();
		bindingPropSource = new BindingSelectionPropertySource(binding, boundaryElements);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPropertyDescriptor() {
		 IPropertyDescriptor[] descriptors = bindingPropSource.getPropertyDescriptors();
		 assertNotNull("no descriptor found", descriptors);
		 
		 assertEquals("To many descriptors", 1, descriptors.length);
		 
		 IPropertyDescriptor descriptor = descriptors[0];
		 
		 assertTrue("wrong descriptor", descriptor instanceof BindingPropertyDescriptor);
	}
	
	@Test
	public void testPropertyEditorDescriptorEditor() {
		IPropertyDescriptor[] descriptors = bindingPropSource.getPropertyDescriptors();
		assertEquals(1, descriptors.length);
		
		BindingPropertyDescriptor descriptor = (BindingPropertyDescriptor) descriptors[0];
		
		CellEditor editor = descriptor.createPropertyEditor(null);
		 
		 assertTrue("wrong cell editor", editor instanceof BindingPropertyCellEditor);
	}

}
