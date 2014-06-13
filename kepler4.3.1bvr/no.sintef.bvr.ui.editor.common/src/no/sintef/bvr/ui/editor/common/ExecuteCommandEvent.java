package no.sintef.bvr.ui.editor.common;

import java.awt.AWTEvent;

import no.sintef.bvr.tool.ui.command.TrivialCommand;

public class ExecuteCommandEvent extends AWTEvent {


	private static final long serialVersionUID = -8635468107891567339L;
	
	public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 2;
	
	TrivialCommand command;
	
	public ExecuteCommandEvent(Object target, TrivialCommand _command) {
		super(target, EVENT_ID);
		command = _command;
	}
	
	public void execute() {
		command.execute();
	}
}