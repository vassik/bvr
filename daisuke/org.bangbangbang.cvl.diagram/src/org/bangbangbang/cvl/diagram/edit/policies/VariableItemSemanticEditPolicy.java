package org.bangbangbang.cvl.diagram.edit.policies;

import java.util.Iterator;
import java.util.List;

import org.bangbangbang.cvl.VSpec;
import org.bangbangbang.cvl.diagram.custom.collapse.EditPartViewElementUtil;
import org.bangbangbang.cvl.diagram.edit.commands.ConstraintContextCreateCommand;
import org.bangbangbang.cvl.diagram.edit.commands.ConstraintContextReorientCommand;
import org.bangbangbang.cvl.diagram.edit.commands.VSpecChildCreateCommand;
import org.bangbangbang.cvl.diagram.edit.parts.ConstraintContextEditPart;
import org.bangbangbang.cvl.diagram.edit.parts.VSpecChildEditPart;
import org.bangbangbang.cvl.diagram.edit.parts.VariableEditPart;
import org.bangbangbang.cvl.diagram.part.CVLMetamodelVisualIDRegistry;
import org.bangbangbang.cvl.diagram.providers.CVLMetamodelElementTypes;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class VariableItemSemanticEditPolicy extends
		CVLMetamodelBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public VariableItemSemanticEditPolicy() {
		super(CVLMetamodelElementTypes.Variable_2016);
	}

	/**
	 * @generated NOT
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(
				getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (CVLMetamodelVisualIDRegistry.getVisualID(incomingLink) == VSpecChildEditPart.VISUAL_ID) {
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
			if (CVLMetamodelVisualIDRegistry.getVisualID(incomingLink) == ConstraintContextEditPart.VISUAL_ID) {
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
			if (CVLMetamodelVisualIDRegistry.getVisualID(outgoingLink) == VSpecChildEditPart.VISUAL_ID) {
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

				// Remove target node
				@SuppressWarnings("unchecked")
				List<EditPart> vspecChilds = ((VariableEditPart) getHost())
						.getSourceConnections();
				for (EditPart ep : vspecChilds) {
					if (ep instanceof VSpecChildEditPart
							&& ((VSpecChildEditPart) ep).getTarget() != null) {
						cmd.add(new CommandProxy(
								((VSpecChildEditPart) ep)
										.getTarget()
										.getCommand(
												new EditCommandRequestWrapper(
														new DestroyElementRequest(
																false)))));
					}
				}
				continue;
			}
		}

		// Delete invisible(Collapsed) views
		List<EObject> children = EditPartViewElementUtil
				.getSemanticChildren((GraphicalEditPart) getHost());
		for (View v : EditPartViewElementUtil
				.getInvisibleViews((GraphicalEditPart) getHost())) {
			if (children.contains(v.getElement())
					&& v.getElement() instanceof VSpec) {
				VSpec vs = (VSpec) v.getElement();
				for (Iterator<VSpec> ite = vs.getChild().iterator(); ite
						.hasNext();) {
					VSpec vsTarget = ite.next();
					DestroyReferenceRequest r = new DestroyReferenceRequest(vs,
							null, vsTarget, false);
					cmd.add(new DestroyReferenceCommand(r) {
						protected CommandResult doExecuteWithResult(
								IProgressMonitor progressMonitor,
								IAdaptable info) throws ExecutionException {
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
				}
				cmd.add(new DestroyElementCommand(new DestroyElementRequest(
						getEditingDomain(), v.getElement(), false)));
				cmd.add(new DeleteCommand(getEditingDomain(), v));
			}
		}

		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands:
			// false
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
		if (CVLMetamodelElementTypes.VSpecChild_4001 == req.getElementType()) {
			return getGEFWrapper(new VSpecChildCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (CVLMetamodelElementTypes.ConstraintContext_4003 == req
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
		if (CVLMetamodelElementTypes.VSpecChild_4001 == req.getElementType()) {
			return getGEFWrapper(new VSpecChildCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (CVLMetamodelElementTypes.ConstraintContext_4003 == req
				.getElementType()) {
			return getGEFWrapper(new ConstraintContextCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EReference based link. New link target or
	 * source should be the domain model element associated with this node.
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