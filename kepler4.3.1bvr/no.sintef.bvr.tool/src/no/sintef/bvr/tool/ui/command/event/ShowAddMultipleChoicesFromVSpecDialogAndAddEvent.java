package no.sintef.bvr.tool.ui.command.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.controller.BVRNotifiableController;
import no.sintef.bvr.tool.controller.BVRResolutionToolView;
import no.sintef.bvr.tool.model.BVRToolModel;
import bvr.CompoundResolution;
import bvr.VClassifier;
import bvr.VSpecResolution;

public class ShowAddMultipleChoicesFromVSpecDialogAndAddEvent implements ActionListener {
	VClassifier toResolve;
	JComponent parent;
	BVRNotifiableController view;

	public ShowAddMultipleChoicesFromVSpecDialogAndAddEvent(JComponent parent, VClassifier toResolve, BVRNotifiableController view) {
		this.toResolve = toResolve;
		this.parent = parent;
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		view.getResolutionControllerInterface().addMultipleTreesFromDialog(parent, toResolve);
	}
}