package no.sintef.cvl.ui.commands.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import no.sintef.cvl.ui.framework.elements.ChoicePanel;
import no.sintef.cvl.ui.loader.CVLView;
import no.sintef.cvl.ui.loader.Main;
import no.sintef.cvl.ui.loader.Pair;
import cvl.ConfigurableUnit;
import cvl.VSpec;

public class PasteChildEvent implements ActionListener {

	private Object p;
	private Map<JComponent, VSpec> vmMap;
	private CVLView view;

	public PasteChildEvent(Object cp, Map<JComponent, VSpec> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings, CVLView view) {
		this.p = cp;
		this.vmMap = vmMap;
		this.view = view;
	}

	public void actionPerformed(ActionEvent e) {
		VSpec v = vmMap.get(p);
		//System.out.println("we are here " + p.getTitle() + ", " + v);
		
		// Modify model
		if(Main.vSpecCut != null){
			if(v != null){
				v.getChild().add(Main.vSpecCut);
			}else{
				ConfigurableUnit cu = view.getCU();
				cu.getOwnedVSpec().add(Main.vSpecCut);
			}
			Main.vSpecCut = null;
		}
		
		// Regenerate view
		view.notifyVspecViewUpdate();
	}

}