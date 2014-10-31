package no.sintef.bvr.tool.ui.command.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import no.sintef.bvr.common.CommonUtility;
import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.controller.BVRNotifiableController;
import no.sintef.bvr.tool.controller.BVRResolutionToolView;
import no.sintef.bvr.tool.controller.command.AddResolution;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.model.CloneResFacade;
import no.sintef.bvr.tool.model.ResolutionModelIterator;
import bvr.BvrFactory;
import bvr.Choice;
import bvr.ChoiceResolution;
import bvr.CompoundResolution;
import bvr.NegResolution;
import bvr.PosResolution;
//import bvr.ChoiceResolution;
//import bvr.VInstance;
import bvr.VSpec;
import bvr.VSpecResolution;

//import bvr.VariableValueAssignment;

public class AddMultipleInstanceTreesEvent implements ActionListener {
	int currentInstances;
	int instancesRequested;
	VSpecResolution parent;
	VSpec target;
	BVRNotifiableController view;

	public AddMultipleInstanceTreesEvent(int instancesRequested, VSpecResolution parent, VSpec target, BVRNotifiableController view) {
		super();
		this.instancesRequested = instancesRequested;
		this.parent = parent;
		this.target = target;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		view.getResolutionControllerInterface().addMultipleTrees(instancesRequested, parent, target);
	}

}
