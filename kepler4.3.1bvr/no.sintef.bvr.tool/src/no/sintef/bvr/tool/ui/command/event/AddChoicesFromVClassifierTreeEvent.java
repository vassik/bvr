package no.sintef.bvr.tool.ui.command.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.antlr.v4.tool.ToolMessage;

import no.sintef.bvr.common.CommonUtility;
import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.controller.BVRNotifiableController;
import no.sintef.bvr.tool.controller.BVRResolutionToolView;
import no.sintef.bvr.tool.controller.command.AddResolution;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.model.ResolutionModelIterator;
import bvr.BvrFactory;
import bvr.PosResolution;
//import bvr.VInstance;
import bvr.VSpec;
import bvr.VSpecResolution;

public class AddChoicesFromVClassifierTreeEvent implements ActionListener {

	private BVRNotifiableController view;
	private VSpec target;
	JComponent parent;

	public AddChoicesFromVClassifierTreeEvent(JComponent parent, VSpec target, BVRNotifiableController view) {

		this.view = view;
		this.target = target;
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent arg0) {
		view.getResolutionControllerInterface().addChoiceTree(parent, target);
		 
	}

}