package no.sintef.bvr.ui.editor.mvc.resolutionV2.tools;

import java.util.List;

import no.sintef.bvr.ui.editor.mvc.resolutionV2.UIElements.BVRViewV2;
import no.sintef.bvr.ui.editor.mvc.resolutionV2.commands.ResCommand;
import bvr.VSpec;
import bvr.VSpecResolution;

public class Iterators {
	private static Iterators instance = null;
	private Iterators(){
		
	}
	public static synchronized Iterators getInstance(){
		if(instance == null){
			instance = new Iterators();
		}
		return instance;
	}

	public void iterateEmpty(BVRViewV2  view, ResCommand command, VSpec vsParent, VSpecResolution vsrParent, boolean onlyOneInstance) {
		List<VSpecResolution> newResolutions = command.init(view, vsParent, vsrParent, onlyOneInstance).execute();
		if (newResolutions != null) {
			for (VSpecResolution newResolution : newResolutions) {
				iterateEmptyOnChildren(view, command, vsParent, newResolution, onlyOneInstance);
			}
		}
		//System.out.println("no new resolutions");
	}

	public void iterateEmptyOnChildren(BVRViewV2  view, ResCommand command, VSpec vsParent, VSpecResolution vsrParent, boolean onlyOneInstance) {
		if (vsParent != null) {
			for (VSpec x : vsParent.getChild()) {
				command.init(view, x, vsrParent, onlyOneInstance);
				List<VSpecResolution> newResolutions = command.execute();
				for (VSpecResolution newResolution : newResolutions) {
					iterateEmptyOnChildren(view, command, x, newResolution, onlyOneInstance);
				}
			}
		}
	}
	public void iterateExisting(BVRViewV2  view, ResCommand command,VSpec vsParent, VSpecResolution vsrParent, boolean onlyOneInstance){
		command.init(view, vsParent, vsrParent, onlyOneInstance).execute();
		vsParent = vsrParent.getResolvedVSpec();
		for(VSpecResolution vsr : vsrParent.getChild()){//TODO
			iterateExisting(view, command, vsParent, vsr, onlyOneInstance);			
		}
	}

}