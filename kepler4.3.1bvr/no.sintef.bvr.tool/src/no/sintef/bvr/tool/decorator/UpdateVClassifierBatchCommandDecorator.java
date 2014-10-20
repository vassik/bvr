package no.sintef.bvr.tool.decorator;

import javax.swing.JComponent;

import no.sintef.bvr.tool.ui.command.UpdateVClassifier;


public class UpdateVClassifierBatchCommandDecorator extends
	UpdateVClassifierAbstractDecorator implements CommandBatchDecorator {

	public UpdateVClassifierBatchCommandDecorator(UpdateVClassifier _command) {
		super(_command);
	}
	
	@Override
	public JComponent execute() {
		preExecute();
		JComponent component = command.execute();
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
