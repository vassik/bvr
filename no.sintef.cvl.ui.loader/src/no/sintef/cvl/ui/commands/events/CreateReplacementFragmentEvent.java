package no.sintef.cvl.ui.commands.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import no.sintef.cvl.ui.loader.CVLModel;
import no.sintef.cvl.ui.loader.CVLView;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import cvl.ConfigurableUnit;
import cvl.CvlFactory;
import cvl.PlacementFragment;
import cvl.ReplacementFragment;

public class CreateReplacementFragmentEvent implements ActionListener {

	private IWorkbenchWindow w;
	private JTabbedPane filePane;
	private List<CVLModel> models;
	private List<CVLView> views;

	public CreateReplacementFragmentEvent(JTabbedPane filePane, List<CVLModel> models, List<CVLView> views, IWorkbenchWindow w) {
		this.filePane = filePane;
		this.models = models;
		this.views = views;
		this.w = w;
	}
	
	static int count = 0;

	@Override
	public void actionPerformed(ActionEvent ev) {
		int tab = filePane.getSelectedIndex();
		CVLModel m = models.get(tab);
		
		ConfigurableUnit cu = m.getCU();
		
		ReplacementFragment rf = CvlFactory.eINSTANCE.createReplacementFragment();
		count++;
		rf.setName("ReplacementFragment" + count);
		
		cu.getOwnedVariationPoint().add(rf);
		
		views.get(tab).notifyCVLRelalizationView();
		
		/*
		System.out.println(w);
		if(w == null){
			JOptionPane.showMessageDialog(null, "No Eclipse Connection Available");
		}else{
			
			ISelection s = w.getActivePage().getActiveEditor().getSite().getSelectionProvider().getSelection();
			
			StructuredSelection ss = (StructuredSelection) s;
			
			List<EObject> selected = new ArrayList<EObject>();
			for(Object o: ss.toList()){
				EObject e = null;
				try {
					Method method = o.getClass().getMethod("resolveSemanticElement");
					e = (EObject) method.invoke(o, new Object[0]);
				} catch (Exception ex) {
				}
				
				if(e == null){
					try{
						e = (EObject)o;
					}catch(ClassCastException ex){
						
					}
				}
				selected.add(e);
			}
			
			String selstring = "Selected: \n";
			
			for(EObject e : selected){
				selstring += " * " + e + "\n";
			}
			
			JOptionPane.showMessageDialog(null, "Selection : " + selstring);
		}
		
		*/
	}

}


//int x = w.getWorkbench().getWorkbenchWindowCount();
/*

editor.get

if(editor instanceof EcoreEditor){
	EcoreEditor e = (EcoreEditor) editor;
	ISelection x = e.getSelection();
	JOptionPane.showMessageDialog(null, "Selection " + x.getClass());
}else{
	JOptionPane.showMessageDialog(null, "Unknown editor " + editor.getClass().getName() + ".." + EcoreEditor.class.getName());	
}
*/

//EcoreEditor e = w.getEcoreEditor();
//ISelection x = e.getSelection();*/

/*			JOptionPane.showMessageDialog(null, editor.getClass().getName());

if(editor.getClass().getSimpleName().equals("EcoreEditor")){
	try {
		Method method = editor.getClass().getMethod("getSelection");
		s = (ISelection) method.invoke(editor, new Object[0]);
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
	}
	
	TreeSelection ts = (TreeSelection)s;
	
	List l = ts.toList();
	
	EObject e = (EObject)l.get(0);
	
	JOptionPane.showMessageDialog(null, "Selection e " + s.getClass() + " .. " + e);
}else if(editor.getClass().getSimpleName().equals("EcoreDiagramEditor")){
	EObject o = null;
	try {
		Method method = editor.getClass().getMethod("getDiagram");
		Object diagram = method.invoke(editor, new Object[0]);
		Method method2 = diagram.getClass().getMethod("getElement");
		o = (EObject)method2.invoke(diagram, new Object[0]);
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
	}
	
	JOptionPane.showMessageDialog(null, "Selection d " + o);
}else{
	JOptionPane.showMessageDialog(null, "Unknown editor");
}
*/

//EcoreEditor
//EcoreDiagramEditor

/*
EObject e = null;
try {
	Object o = ss.getFirstElement();
	Method method = o.getClass().getMethod("resolveSemanticElement");
	e = (EObject) method.invoke(o, new Object[0]);
} catch (Exception ex) {
	JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
}*/