package no.sintef.bvr.tool.decorator;

import javax.swing.JComponent;

import no.sintef.bvr.tool.ui.command.UpdateConstraint;

public class UpdateConstraintBatchCommandDecorator extends
		UpdateConstraintAbstractDecorator implements CommandBatchDecorator {

	public UpdateConstraintBatchCommandDecorator(UpdateConstraint command) {
		super(command);
	}
	
	@Override
	public JComponent execute() {
		preExecute();
		JComponent component = super.execute();
		postExecute();
		return component;
	}

	@Override
	public void preExecute() {
		controller.getVSpecControllerInterface().enableBatchCommandProcessing();
	}

	@Override
	public void postExecute() {
		controller.getVSpecControllerInterface().executeCommandBatch();
		controller.getVSpecControllerInterface().disableBatchCommandProcessing();
	}

}
