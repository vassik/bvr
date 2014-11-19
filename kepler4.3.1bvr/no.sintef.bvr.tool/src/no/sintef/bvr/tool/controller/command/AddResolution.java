package no.sintef.bvr.tool.controller.command;

import java.util.ArrayList;
import java.util.List;

import no.sintef.bvr.common.CommonUtility;
import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.controller.BVRNotifiableController;
import no.sintef.bvr.tool.controller.BVRResolutionToolView;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.model.PrimitiveTypeFacade;
import bvr.BvrFactory;
import bvr.Choice;
import bvr.ChoiceResolution;
import bvr.CompoundResolution;
import bvr.PosResolution;
//import bvr.ChoiceResolution;
//import bvr.PrimitiveTypeEnum;
import bvr.PrimitiveValueSpecification;
import bvr.PrimitveType;
import bvr.VClassifier;
//import bvr.VInstance;
import bvr.VSpec;
import bvr.VSpecResolution;
import bvr.ValueResolution;
import bvr.Variable;
//import bvr.VariableValueAssignment;
//import bvr.common.PrimitiveTypeHandler;

public class AddResolution implements ResCommand{
	private BVRToolModel view;
	private VSpec target;
	private boolean onlyOneInstance;
	private VSpecResolution parent;
/**
 * NOT TRANSACTIONAL
 * ONLY for use with nodes NOT added to model
 */
	@Override
	public ResCommand init(BVRToolModel view, VSpec vs, VSpecResolution vsr, boolean onlyOneInstance) {
		this.view = view;
		this.target = vs;
		this.parent = vsr;
		this.onlyOneInstance = onlyOneInstance;
		return this;
	}

	@Override
	public List<VSpecResolution> execute() {
		// TODO Auto-generated method stub
		
		List<VSpecResolution> thisResolution = new ArrayList<VSpecResolution>();
		if (target instanceof Choice) {
			thisResolution.add(addResolution((Choice) target, parent));
		}
		if (target instanceof VClassifier) {
			int min;
			if (((VClassifier) target).getInstanceMultiplicity() != null && !onlyOneInstance) {
				min = ((VClassifier) target).getInstanceMultiplicity().getLower();
			} else {
				min = 1;
			}
			for (int i = 0; i < min; i++) {
				thisResolution.add(addResolution((VClassifier) target, parent));
			}
			min = 0;
		}
		if (target instanceof Variable) {
			
			thisResolution.add(addResolution((Variable) target, parent));
		}

		return thisResolution;
	}

	private VSpecResolution addResolution(VClassifier target, VSpecResolution parent) {
	
		PosResolution thisResolution = BvrFactory.eINSTANCE.createPosResolution();
		 //count++;
		
		//thisResolution.setName("I " + view.getIncrementedNameCounter());
		thisResolution.setName("I " + view.getIncrementedInstanceCount());
		thisResolution = (PosResolution) CommonUtility.setResolved(thisResolution, target);
		
		((CompoundResolution) parent).getMembers().add(thisResolution);
		return thisResolution;
	}

	// resolve Choice
	private VSpecResolution addResolution(Choice target, VSpecResolution parent) {
		
		ChoiceResolution thisResolution = BvrFactory.eINSTANCE.createPosResolution();
		thisResolution.setName(target.getName());
		thisResolution = (PosResolution) CommonUtility.setResolved(thisResolution, target);
		((CompoundResolution)parent).getMembers().add(thisResolution);
		return thisResolution;
	}

	// resolve Variable
	private VSpecResolution addResolution(Variable vSpecFound, VSpecResolution parent) {
		
		ValueResolution thisResolution = PrimitiveTypeFacade.getInstance().createDefaultValueResolution(vSpecFound);
		//Context.eINSTANCE.getEditorCommands().addValueResolution((CompoundResolution) parent, valueResolution);
		((CompoundResolution)parent).getMembers().add(thisResolution);
		return thisResolution;
	}
}
