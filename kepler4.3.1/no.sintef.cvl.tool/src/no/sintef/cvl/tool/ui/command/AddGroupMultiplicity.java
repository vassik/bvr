package no.sintef.cvl.tool.ui.command;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import no.sintef.cvl.tool.ui.editor.CVLUIKernel;
import no.sintef.cvl.tool.ui.loader.VSpecView;
import no.sintef.cvl.tool.ui.loader.Main;
import no.sintef.cvl.tool.ui.loader.Pair;
import no.sintef.cvl.ui.framework.OptionalElement.OPTION_STATE;
import no.sintef.cvl.ui.framework.elements.GroupPanel;
import cvl.MultiplicityInterval;
import cvl.NamedElement;
import cvl.VSpec;

public class AddGroupMultiplicity implements Command {

	CVLUIKernel rootPanel;
	VSpec v;
	JComponent parent;
	private List<JComponent> nodes;
	private List<Pair<JComponent, JComponent>> bindings;
	
	public Command init(CVLUIKernel rootPanel, Object p, JComponent parent, Map<JComponent, NamedElement> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings, VSpecView view) {
		if(p instanceof VSpec){
			this.rootPanel = rootPanel;
			this.v = (VSpec) p;
			this.parent = parent;
		}
		
		this.nodes = nodes;
		this.bindings = bindings;
		
		return this;
	}

	public JComponent execute() {
		if(v.getGroupMultiplicity() != null){
			MultiplicityInterval m = v.getGroupMultiplicity();
			GroupPanel group = new GroupPanel();
			nodes.add(group);
			int l = m.getLower();
			int u = m.getUpper();
			group.setCardinality(l, u);
			rootPanel.getModelPanel().addNode(group);

			Helper.bind(parent, group, rootPanel.getModelPanel(), OPTION_STATE.MANDATORY, bindings);
			
			return group;
		}
		return null;
	}

}
