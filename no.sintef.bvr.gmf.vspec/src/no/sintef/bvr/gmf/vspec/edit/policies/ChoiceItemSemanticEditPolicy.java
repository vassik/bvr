package no.sintef.bvr.gmf.vspec.edit.policies;

import java.util.Iterator;

import no.sintef.bvr.gmf.vspec.edit.commands.ConstraintContextCreateCommand;
import no.sintef.bvr.gmf.vspec.edit.commands.ConstraintContextReorientCommand;
import no.sintef.bvr.gmf.vspec.edit.commands.VSpecChildCreateCommand;
import no.sintef.bvr.gmf.vspec.edit.commands.VSpecChildReorientCommand;
import no.sintef.bvr.gmf.vspec.edit.parts.ChoiceChoiceGroupMultiplicityCompartment2EditPart;
import no.sintef.bvr.gmf.vspec.edit.parts.ConstraintContextEditPart;
import no.sintef.bvr.gmf.vspec.edit.parts.MultiplicityIntervalEditPart;
import no.sintef.bvr.gmf.vspec.edit.parts.VSpecChildEditPart;
import no.sintef.bvr.gmf.vspec.part.BVRMetamodelVisualIDRegistry;
import no.sintef.bvr.gmf.vspec.providers.BVRMetamodelElementTypes;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class ChoiceItemSemanticEditPolicy extends
		BVRMetamodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ChoiceItemSemanticEditPolicy() {
		super(BVRMetamodelElementTypes.Choice_2005);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(
				getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (BVRMetamodelVisualIDRegistry.getVisualID(incomingLink) == VSpecChildEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r) {
					protected CommandResult doExecuteWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						EObject referencedObject = getReferencedObject();
						Resource resource = referencedObject.eResource();
						CommandResult result = super.doExecuteWithResult(
								progressMonitor, info);
						if (resource != null) {
							resource.getContents().add(referencedObject);
						}
						return result;
					}
				});
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (BVRMetamodelVisualIDRegistry.getVisualID(incomingLink) == ConstraintContextEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
		}
		for (Iterator<?> it = view.getSourceEdges().iterator(); it.hasNext();) {
			Edge outgoingLink = (Edge) it.next();
			if (BVRMetamodelVisualIDRegistry.getVisualID(outgoingLink) == VSpecChildEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(
						outgoingLink.getSource().getElement(), null,
						outgoingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r) {
					protected CommandResult doExecuteWithResult(
							IProgressMonitor progressMonitor, IAdaptable info)
							throws ExecutionException {
						EObject referencedObject = getReferencedObject();
						Resource resource = referencedObject.eResource();
						CommandResult result = super.doExecuteWithResult(
								progressMonitor, info);
						if (resource != null) {
							resource.getContents().add(referencedObject);
						}
						return result;
					}
				});
				cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
				continue;
			}
		}
		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands: false
			addDestroyChildNodesCommand(cmd);
			addDestroyShortcutsCommand(cmd, view);
			// delete host element
			cmd.add(new DestroyElementCommand(req));
		} else {
			cmd.add(new DeleteCommand(getEditingDomain(), view));
		}
		return getGEFWrapper(cmd.reduce());
	}

	/**
	 * @generated
	 */
	private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
		View view = (View) getHost().getModel();
		for (Iterator<?> nit = view.getChildren().iterator(); nit.hasNext();) {
			Node node = (Node) nit.next();
			switch (BVRMetamodelVisualIDRegistry.getVisualID(node)) {
			case ChoiceChoiceGroupMultiplicityCompartment2EditPart.VISUAL_ID:
				for (Iterator<?> cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (BVRMetamodelVisualIDRegistry.getVisualID(cnode)) {
					case MultiplicityIntervalEditPart.VISUAL_ID:
						cmd.add(new DestroyElementCommand(
								new DestroyElementRequest(getEditingDomain(),
										cnode.getElement(), false))); // directlyOwned: true
						// don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
						// cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
				: getCompleteCreateRelationshipCommand(req);
		return command != null ? command : super
				.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getStartCreateRelationshipCommand(
			CreateRelationshipRequest req) {
		if (BVRMetamodelElementTypes.VSpecChild_4001 == req.getElementType()) {
			return getGEFWrapper(new VSpecChildCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (BVRMetamodelElementTypes.ConstraintContext_4003 == req
				.getElementType()) {
			return null;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCompleteCreateRelationshipCommand(
			CreateRelationshipRequest req) {
		if (BVRMetamodelElementTypes.VSpecChild_4001 == req.getElementType()) {
			return getGEFWrapper(new VSpecChildCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (BVRMetamodelElementTypes.ConstraintContext_4003 == req
				.getElementType()) {
			return getGEFWrapper(new ConstraintContextCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EReference based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest req) {
		switch (getVisualID(req)) {
		case ConstraintContextEditPart.VISUAL_ID:
			return getGEFWrapper(new ConstraintContextReorientCommand(req));
		}
		return super.getReorientReferenceRelationshipCommand(req);
	}

}
