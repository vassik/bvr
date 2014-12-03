package no.sintef.bvr.tool.controller.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.controller.command.Command;
import no.sintef.bvr.tool.interfaces.ui.editor.BVRUIKernelInterface;
import no.sintef.bvr.tool.interfaces.ui.editor.Pair;
import bvr.NamedElement;
import bvr.Variable;
import bvr.VNode;


public class UpdateChoiceOccurence<EDITOR_PANEL, MODEL_PANEL> extends UpdateVSpec<EDITOR_PANEL, MODEL_PANEL> {
	
	protected Map<Variable, String> varNames = new HashMap<Variable, String>();
	protected Map<Variable, String> varTypes = new HashMap<Variable, String>();
	protected String comment;
	protected String strType;
	
	@Override
	public Command<EDITOR_PANEL, MODEL_PANEL> init(BVRUIKernelInterface<EDITOR_PANEL, MODEL_PANEL> rootPanel, Object p, JComponent parent,
			Map<JComponent, NamedElement> vmMap, List<JComponent> nodes,
			List<Pair<JComponent, JComponent>> bindings, BVRNotifiableController controller) {
		return super.init(rootPanel, p, parent, vmMap, nodes, bindings, controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JComponent execute() {
		controller.getVSpecControllerInterface().setNodeName(parent, name);
		controller.getVSpecControllerInterface().setNodeComment(parent, comment);
		controller.getVSpecControllerInterface().setChoiceOccurenceType(parent, strType);
		
		for(Variable v : ((VNode) namedElement).getVariable()){
			String newName = varNames.get(v);
			String newType = varTypes.get(v);
			controller.getVSpecControllerInterface().updateVariable(v, newName, newType);
		}
		return parent;
	}

	public void setVariable(Variable v, String name, String type) {
		varNames.put(v, name);
		varTypes.put(v, type);
	}
	
	public void setComment(String text) {
		comment = text;
	}
	
	public void setChoiceType(String type) {
		strType = type;
	}
}