package no.sintef.cvl.tool.ui.command.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import no.sintef.cvl.tool.ui.loader.VSpecView;
import no.sintef.cvl.tool.ui.loader.Main;
import no.sintef.cvl.tool.ui.loader.Pair;
import cvl.Choice;
import cvl.ConfigurableUnit;
import cvl.NamedElement;
import cvl.ObjectHandle;
import cvl.PlacementFragment;
import cvl.ReplacementFragment;
import cvl.VSpec;
import cvl.CvlFactory;


public class AddChoiceEvent implements ActionListener {
	private JComponent p;
	private Map<JComponent, NamedElement> vmMap;
	private VSpecView view;

	public AddChoiceEvent(JComponent p, Map<JComponent, NamedElement> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings, VSpecView view) {
		this.p = p;
		this.vmMap = vmMap;
		this.view = view;
	}
	
	static int x = 1;

	public void actionPerformed(ActionEvent arg0) {
		//System.out.println("we are here " + p.getTitle() + ", " + v);
		VSpec v = (VSpec)vmMap.get(p);
		
		// Modify model
		Choice c = CvlFactory.eINSTANCE.createChoice();
		c.setName("Choice"+x);
		x++;
		
		if(v != null){
			v.getChild().add(c);
		}else{
			ConfigurableUnit cu = view.getCU();
			cu.getOwnedVSpec().add(c);
		}
		
		// Regenerate view
		view.notifyVspecViewUpdate();
	}
}
