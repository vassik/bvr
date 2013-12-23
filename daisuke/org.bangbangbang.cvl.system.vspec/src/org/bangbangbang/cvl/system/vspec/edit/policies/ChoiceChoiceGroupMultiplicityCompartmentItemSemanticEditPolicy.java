package org.bangbangbang.cvl.system.vspec.edit.policies;

import org.bangbangbang.cvl.system.vspec.edit.commands.MultiplicityInterval2CreateCommand;
import org.bangbangbang.cvl.system.vspec.edit.commands.MultiplicityIntervalCreateCommand;
import org.bangbangbang.cvl.system.vspec.providers.CVLMetamodelElementTypes;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @generated
 */
public class ChoiceChoiceGroupMultiplicityCompartmentItemSemanticEditPolicy
		extends CVLMetamodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ChoiceChoiceGroupMultiplicityCompartmentItemSemanticEditPolicy() {
		super(CVLMetamodelElementTypes.Choice_2006);
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (CVLMetamodelElementTypes.MultiplicityInterval_3002 == req
				.getElementType()) {
			return getGEFWrapper(new MultiplicityInterval2CreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

}