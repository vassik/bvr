package no.sintef.bvr.ui.editor.mvc.resolutionV2.commands;

import java.util.ArrayList;
import java.util.List;

import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.ui.editor.mvc.resolutionV2.UIElements.BVRResolutionToolView;
import no.sintef.bvr.ui.editor.mvc.resolutionV2.tools.Inheritance;
//import bvr.ChoiceResolution;
import bvr.VSpec;
import bvr.VSpecResolution;

public class ToggleChoiceCommand implements ResCommand {
	VSpecResolution vsr;

	@Override
	public ResCommand init(BVRResolutionToolView  view, VSpec vs, VSpecResolution vsr, boolean onlyOneInstance) {
		this.vsr = vsr;
		return this;
	}

	@Override
	public List<VSpecResolution> execute() {
		return null;
	/*	if (vsr instanceof ChoiceResolution) {
			Context.eINSTANCE.getEditorCommands().setResolutionDecision((ChoiceResolution) vsr, !((ChoiceResolution) vsr).isDecision());
			Inheritance.getInstance().passInheritance( (ChoiceResolution) vsr, ((ChoiceResolution) vsr).isDecision());
		}
		ArrayList<VSpecResolution> a = new ArrayList<VSpecResolution>();
		a.add(vsr);
		return a;*/
	}

}
