package no.sintef.bvr.gmf.vspec.part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.sintef.bvr.ui.editor.commands.EditorEMFTransactionalCommands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.AbstractDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.DiagramIOUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @generated
 */
public class BVRMetamodelDocumentProvider extends AbstractDocumentProvider
		implements IDiagramDocumentProvider {

	/**
	 * @generated
	 */
	protected ElementInfo createElementInfo(Object element)
			throws CoreException {
		if (false == element instanceof FileEditorInput
				&& false == element instanceof URIEditorInput) {
			throw new CoreException(
					new Status(
							IStatus.ERROR,
							BVRMetamodelDiagramEditorPlugin.ID,
							0,
							NLS.bind(
									Messages.BVRMetamodelDocumentProvider_IncorrectInputError,
									new Object[] {
											element,
											"org.eclipse.ui.part.FileEditorInput", "org.eclipse.emf.common.ui.URIEditorInput" }), //$NON-NLS-1$ //$NON-NLS-2$ 
							null));
		}
		IEditorInput editorInput = (IEditorInput) element;
		IDiagramDocument document = (IDiagramDocument) createDocument(editorInput);

		ResourceSetInfo info = new ResourceSetInfo(document, editorInput);
		info.setModificationStamp(computeModificationStamp(info));
		info.fStatus = null;
		return info;
	}

	/**
	 * @generated
	 */
	protected IDocument createDocument(Object element) throws CoreException {
		if (false == element instanceof FileEditorInput
				&& false == element instanceof URIEditorInput) {
			throw new CoreException(
					new Status(
							IStatus.ERROR,
							BVRMetamodelDiagramEditorPlugin.ID,
							0,
							NLS.bind(
									Messages.BVRMetamodelDocumentProvider_IncorrectInputError,
									new Object[] {
											element,
											"org.eclipse.ui.part.FileEditorInput", "org.eclipse.emf.common.ui.URIEditorInput" }), //$NON-NLS-1$ //$NON-NLS-2$ 
							null));
		}
		IDocument document = createEmptyDocument();
		setDocumentContent(document, (IEditorInput) element);
		setupDocument(element, document);
		return document;
	}

	/**
	 * Sets up the given document as it would be provided for the given element.
	 * The content of the document is not changed. This default implementation
	 * is empty. Subclasses may reimplement.
	 * 
	 * @param element
	 *            the blue-print element
	 * @param document
	 *            the document to set up
	 * @generated
	 */
	protected void setupDocument(Object element, IDocument document) {
		// for subclasses
	}

	/**
	 * @generated
	 */
	private long computeModificationStamp(ResourceSetInfo info) {
		int result = 0;
		for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
				.hasNext();) {
			Resource nextResource = it.next();
			IFile file = WorkspaceSynchronizer.getFile(nextResource);
			if (file != null) {
				if (file.getLocation() != null) {
					result += file.getLocation().toFile().lastModified();
				} else {
					result += file.getModificationStamp();
				}
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	protected IDocument createEmptyDocument() {
		DiagramDocument document = new DiagramDocument();
		document.setEditingDomain(createEditingDomain());
		return document;
	}

	/**
	 * @generated NOT
	 */
	private TransactionalEditingDomain createEditingDomain() {
		TransactionalEditingDomain editingDomain = EditorEMFTransactionalCommands
				.Get().testTransactionalEditingDomain();
		final NotificationFilter diagramResourceModifiedFilter = NotificationFilter
				.createNotifierFilter(editingDomain.getResourceSet())
				.and(NotificationFilter.createEventTypeFilter(Notification.ADD))
				.and(NotificationFilter.createFeatureFilter(ResourceSet.class,
						ResourceSet.RESOURCE_SET__RESOURCES));
		editingDomain.getResourceSet().eAdapters().add(new Adapter() {

			private Notifier myTarger;

			public Notifier getTarget() {
				return myTarger;
			}

			public boolean isAdapterForType(Object type) {
				return false;
			}

			public void notifyChanged(Notification notification) {
				if (diagramResourceModifiedFilter.matches(notification)) {
					Object value = notification.getNewValue();
					if (value instanceof Resource) {
						((Resource) value).setTrackingModification(true);
					}
				}
			}

			public void setTarget(Notifier newTarget) {
				myTarger = newTarget;
			}

		});

		return editingDomain;
	}

	/**
	 * @generated
	 */
	protected void setDocumentContent(IDocument document, IEditorInput element)
			throws CoreException {
		IDiagramDocument diagramDocument = (IDiagramDocument) document;
		TransactionalEditingDomain domain = diagramDocument.getEditingDomain();
		if (element instanceof FileEditorInput) {
			IStorage storage = ((FileEditorInput) element).getStorage();
			Diagram diagram = DiagramIOUtil.load(domain, storage, true,
					getProgressMonitor());
			document.setContent(diagram);
		} else if (element instanceof URIEditorInput) {
			URI uri = ((URIEditorInput) element).getURI();
			Resource resource = null;
			try {
				resource = domain.getResourceSet().getResource(
						uri.trimFragment(), false);
				if (resource == null) {
					resource = domain.getResourceSet().createResource(
							uri.trimFragment());
				}
				if (!resource.isLoaded()) {
					try {
						Map options = new HashMap(
								GMFResourceFactory.getDefaultLoadOptions());
						// @see 171060
						// options.put(org.eclipse.emf.ecore.xmi.XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,
						// Boolean.TRUE);
						resource.load(options);
					} catch (IOException e) {
						resource.unload();
						throw e;
					}
				}
				if (uri.fragment() != null) {
					EObject rootElement = resource.getEObject(uri.fragment());
					if (rootElement instanceof Diagram) {
						document.setContent((Diagram) rootElement);
						return;
					}
				} else {
					for (Iterator it = resource.getContents().iterator(); it
							.hasNext();) {
						Object rootElement = it.next();
						if (rootElement instanceof Diagram) {
							document.setContent((Diagram) rootElement);
							return;
						}
					}
				}
				throw new RuntimeException(
						Messages.BVRMetamodelDocumentProvider_NoDiagramInResourceError);
			} catch (Exception e) {
				CoreException thrownExcp = null;
				if (e instanceof CoreException) {
					thrownExcp = (CoreException) e;
				} else {
					String msg = e.getLocalizedMessage();
					thrownExcp = new CoreException(
							new Status(
									IStatus.ERROR,
									BVRMetamodelDiagramEditorPlugin.ID,
									0,
									msg != null ? msg
											: Messages.BVRMetamodelDocumentProvider_DiagramLoadingError,
									e));
				}
				throw thrownExcp;
			}
		} else {
			throw new CoreException(
					new Status(
							IStatus.ERROR,
							BVRMetamodelDiagramEditorPlugin.ID,
							0,
							NLS.bind(
									Messages.BVRMetamodelDocumentProvider_IncorrectInputError,
									new Object[] {
											element,
											"org.eclipse.ui.part.FileEditorInput", "org.eclipse.emf.common.ui.URIEditorInput" }), //$NON-NLS-1$ //$NON-NLS-2$ 
							null));
		}
	}

	/**
	 * @generated
	 */
	public long getModificationStamp(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			return computeModificationStamp(info);
		}
		return super.getModificationStamp(element);
	}

	/**
	 * @generated
	 */
	public boolean isDeleted(Object element) {
		IDiagramDocument document = getDiagramDocument(element);
		if (document != null) {
			Resource diagramResource = document.getDiagram().eResource();
			if (diagramResource != null) {
				IFile file = WorkspaceSynchronizer.getFile(diagramResource);
				return file == null || file.getLocation() == null
						|| !file.getLocation().toFile().exists();
			}
		}
		return super.isDeleted(element);
	}

	/**
	 * @generated
	 */
	public ResourceSetInfo getResourceSetInfo(Object editorInput) {
		return (ResourceSetInfo) super.getElementInfo(editorInput);
	}

	/**
	 * @generated
	 */
	protected void disposeElementInfo(Object element, ElementInfo info) {
		if (info instanceof ResourceSetInfo) {
			ResourceSetInfo resourceSetInfo = (ResourceSetInfo) info;
			resourceSetInfo.dispose();
		}
		super.disposeElementInfo(element, info);
	}

	/**
	 * @generated
	 */
	protected void doValidateState(Object element, Object computationContext)
			throws CoreException {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			LinkedList<IFile> files2Validate = new LinkedList<IFile>();
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null && file.isReadOnly()) {
					files2Validate.add(file);
				}
			}
			ResourcesPlugin.getWorkspace().validateEdit(
					(IFile[]) files2Validate.toArray(new IFile[files2Validate
							.size()]), computationContext);
		}

		super.doValidateState(element, computationContext);
	}

	/**
	 * @generated
	 */
	public boolean isReadOnly(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			if (info.isUpdateCache()) {
				try {
					updateCache(element);
				} catch (CoreException ex) {
					BVRMetamodelDiagramEditorPlugin.getInstance().logError(
							Messages.BVRMetamodelDocumentProvider_isModifiable,
							ex);
					// Error message to log was initially taken from
					// org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages.StorageDocumentProvider_isModifiable
				}
			}
			return info.isReadOnly();
		}
		return super.isReadOnly(element);
	}

	/**
	 * @generated
	 */
	public boolean isModifiable(Object element) {
		if (!isStateValidated(element)) {
			if (element instanceof FileEditorInput
					|| element instanceof URIEditorInput) {
				return true;
			}
		}
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			if (info.isUpdateCache()) {
				try {
					updateCache(element);
				} catch (CoreException ex) {
					BVRMetamodelDiagramEditorPlugin.getInstance().logError(
							Messages.BVRMetamodelDocumentProvider_isModifiable,
							ex);
					// Error message to log was initially taken from
					// org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages.StorageDocumentProvider_isModifiable
				}
			}
			return info.isModifiable();
		}
		return super.isModifiable(element);
	}

	/**
	 * @generated
	 */
	protected void updateCache(Object element) throws CoreException {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null && file.isReadOnly()) {
					info.setReadOnly(true);
					info.setModifiable(false);
					return;
				}
			}
			info.setReadOnly(false);
			info.setModifiable(true);
			return;
		}
	}

	/**
	 * @generated
	 */
	protected void doUpdateStateCache(Object element) throws CoreException {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			info.setUpdateCache(true);
		}
		super.doUpdateStateCache(element);
	}

	/**
	 * @generated
	 */
	public boolean isSynchronized(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			return info.isSynchronized();
		}
		return super.isSynchronized(element);
	}

	/**
	 * @generated
	 */
	protected ISchedulingRule getResetRule(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			LinkedList<ISchedulingRule> rules = new LinkedList<ISchedulingRule>();
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null) {
					rules.add(ResourcesPlugin.getWorkspace().getRuleFactory()
							.modifyRule(file));
				}
			}
			return new MultiRule(
					(ISchedulingRule[]) rules.toArray(new ISchedulingRule[rules
							.size()]));
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected ISchedulingRule getSaveRule(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			LinkedList<ISchedulingRule> rules = new LinkedList<ISchedulingRule>();
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null) {
					rules.add(computeSchedulingRule(file));
				}
			}
			return new MultiRule(
					(ISchedulingRule[]) rules.toArray(new ISchedulingRule[rules
							.size()]));
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected ISchedulingRule getSynchronizeRule(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			LinkedList<ISchedulingRule> rules = new LinkedList<ISchedulingRule>();
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null) {
					rules.add(ResourcesPlugin.getWorkspace().getRuleFactory()
							.refreshRule(file));
				}
			}
			return new MultiRule(
					(ISchedulingRule[]) rules.toArray(new ISchedulingRule[rules
							.size()]));
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected ISchedulingRule getValidateStateRule(Object element) {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			LinkedList<ISchedulingRule> files = new LinkedList<ISchedulingRule>();
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				IFile file = WorkspaceSynchronizer.getFile(nextResource);
				if (file != null) {
					files.add(file);
				}
			}
			return ResourcesPlugin
					.getWorkspace()
					.getRuleFactory()
					.validateEditRule(
							(IFile[]) files.toArray(new IFile[files.size()]));
		}
		return null;
	}

	/**
	 * @generated
	 */
	private ISchedulingRule computeSchedulingRule(IResource toCreateOrModify) {
		if (toCreateOrModify.exists())
			return ResourcesPlugin.getWorkspace().getRuleFactory()
					.modifyRule(toCreateOrModify);

		IResource parent = toCreateOrModify;
		do {
			/*
			 * XXX This is a workaround for
			 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=67601
			 * IResourceRuleFactory.createRule should iterate the hierarchy
			 * itself.
			 */
			toCreateOrModify = parent;
			parent = toCreateOrModify.getParent();
		} while (parent != null && !parent.exists());

		return ResourcesPlugin.getWorkspace().getRuleFactory()
				.createRule(toCreateOrModify);
	}

	/**
	 * @generated
	 */
	protected void doSynchronize(Object element, IProgressMonitor monitor)
			throws CoreException {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
					.hasNext();) {
				Resource nextResource = it.next();
				handleElementChanged(info, nextResource, monitor);
			}
			return;
		}
		super.doSynchronize(element, monitor);
	}

	/**
	 * @generated NOT
	 */
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
		ResourceSetInfo info = getResourceSetInfo(element);
		if (info != null) {
			if (!overwrite && !info.isSynchronized()) {
				throw new CoreException(
						new Status(
								IStatus.ERROR,
								BVRMetamodelDiagramEditorPlugin.ID,
								IResourceStatus.OUT_OF_SYNC_LOCAL,
								Messages.BVRMetamodelDocumentProvider_UnsynchronizedFileSaveError,
								null));
			}
			info.stopResourceListening();
			fireElementStateChanging(element);
			try {
				monitor.beginTask(
						Messages.BVRMetamodelDocumentProvider_SaveDiagramTask,
						info.getResourceSet().getResources().size() + 1); // "Saving diagram"

				// Get target EMF and GMF resource URI
				URI emf = null;
				URI gmf = null;
				if (info.fDocument.getContent() != null
						&& info.fDocument.getContent() instanceof Diagram) {
					emf = EcoreUtil.getURI(
							((Diagram) info.fDocument.getContent())
									.getElement()).trimFragment();
				}
				gmf = EditUIUtil.getURI((IEditorInput) element);

				for (Iterator<Resource> it = info.getLoadedResourcesIterator(); it
						.hasNext();) {
					Resource nextResource = it.next();

					// Avoid other resources
					if (nextResource.getURI().equals(emf)
							|| nextResource.getURI().equals(gmf)) {

						monitor.setTaskName(NLS
								.bind(Messages.BVRMetamodelDocumentProvider_SaveNextResourceTask,
										nextResource.getURI()));
						if (nextResource.isLoaded()
								&& !info.getEditingDomain().isReadOnly(
										nextResource)) {
							try {
								nextResource.save(BVRMetamodelDiagramEditorUtil
										.getSaveOptions());
							} catch (IOException e) {
								fireElementStateChangeFailed(element);
								throw new CoreException(new Status(
										IStatus.ERROR,
										BVRMetamodelDiagramEditorPlugin.ID,
										EditorStatusCodes.RESOURCE_FAILURE,
										e.getLocalizedMessage(), null));
							}
						}
						monitor.worked(1);
					}
				}
				monitor.done();
				info.setModificationStamp(computeModificationStamp(info));
			} catch (RuntimeException x) {
				fireElementStateChangeFailed(element);
				throw x;
			} finally {
				info.startResourceListening();
			}
		} else {
			URI newResoruceURI;
			List<IFile> affectedFiles = null;
			if (element instanceof FileEditorInput) {
				IFile newFile = ((FileEditorInput) element).getFile();
				affectedFiles = Collections.singletonList(newFile);
				newResoruceURI = URI.createPlatformResourceURI(newFile
						.getFullPath().toString(), true);
			} else if (element instanceof URIEditorInput) {
				newResoruceURI = ((URIEditorInput) element).getURI();
			} else {
				fireElementStateChangeFailed(element);
				throw new CoreException(
						new Status(
								IStatus.ERROR,
								BVRMetamodelDiagramEditorPlugin.ID,
								0,
								NLS.bind(
										Messages.BVRMetamodelDocumentProvider_IncorrectInputError,
										new Object[] {
												element,
												"org.eclipse.ui.part.FileEditorInput", "org.eclipse.emf.common.ui.URIEditorInput" }), //$NON-NLS-1$ //$NON-NLS-2$ 
								null));
			}
			if (false == document instanceof IDiagramDocument) {
				fireElementStateChangeFailed(element);
				throw new CoreException(
						new Status(
								IStatus.ERROR,
								BVRMetamodelDiagramEditorPlugin.ID,
								0,
								"Incorrect document used: " + document + " instead of org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument", null)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			IDiagramDocument diagramDocument = (IDiagramDocument) document;
			final Resource newResource = diagramDocument.getEditingDomain()
					.getResourceSet().createResource(newResoruceURI);
			final Diagram diagramCopy = (Diagram) EcoreUtil
					.copy(diagramDocument.getDiagram());
			try {
				new AbstractTransactionalCommand(
						diagramDocument.getEditingDomain(),
						NLS.bind(
								Messages.BVRMetamodelDocumentProvider_SaveAsOperation,
								diagramCopy.getName()), affectedFiles) {
					protected CommandResult doExecuteWithResult(
							IProgressMonitor monitor, IAdaptable info)
							throws ExecutionException {
						newResource.getContents().add(diagramCopy);
						return CommandResult.newOKCommandResult();
					}
				}.execute(monitor, null);
				newResource
						.save(BVRMetamodelDiagramEditorUtil.getSaveOptions());
			} catch (ExecutionException e) {
				fireElementStateChangeFailed(element);
				throw new CoreException(new Status(IStatus.ERROR,
						BVRMetamodelDiagramEditorPlugin.ID, 0,
						e.getLocalizedMessage(), null));
			} catch (IOException e) {
				fireElementStateChangeFailed(element);
				throw new CoreException(new Status(IStatus.ERROR,
						BVRMetamodelDiagramEditorPlugin.ID, 0,
						e.getLocalizedMessage(), null));
			}
			// newResource.unload();
		}
	}

	/**
	 * @generated NOT
	 */
	protected void handleElementChanged(ResourceSetInfo info,
			Resource changedResource, IProgressMonitor monitor) {
		IFile file = WorkspaceSynchronizer.getFile(changedResource);
		if (file != null) {
			try {
				file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException ex) {
				BVRMetamodelDiagramEditorPlugin
						.getInstance()
						.logError(
								Messages.BVRMetamodelDocumentProvider_handleElementContentChanged,
								ex);
				// Error message to log was initially taken from
				// org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages.FileDocumentProvider_handleElementContentChanged
			}
		}
		// REMOVE unload after save.
		// changedResource.unload();

		fireElementContentAboutToBeReplaced(info.getEditorInput());
		removeUnchangedElementListeners(info.getEditorInput(), info);
		info.fStatus = null;
		try {
			setDocumentContent(info.fDocument, info.getEditorInput());
		} catch (CoreException e) {
			info.fStatus = e.getStatus();
		}
		if (!info.fCanBeSaved) {
			info.setModificationStamp(computeModificationStamp(info));
		}
		addUnchangedElementListeners(info.getEditorInput(), info);
		fireElementContentReplaced(info.getEditorInput());
	}

	/**
	 * @generated
	 */
	protected void handleElementMoved(IEditorInput input, URI uri) {
		if (input instanceof FileEditorInput) {
			IFile newFile = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.getFile(
							new Path(URI.decode(uri.path()))
									.removeFirstSegments(1));
			fireElementMoved(input, newFile == null ? null
					: new FileEditorInput(newFile));
			return;
		}
		// TODO: append suffix to the URI! (use diagram as a parameter)
		fireElementMoved(input, new URIEditorInput(uri));
	}

	/**
	 * @generated
	 */
	public IEditorInput createInputWithEditingDomain(IEditorInput editorInput,
			TransactionalEditingDomain domain) {
		return editorInput;
	}

	/**
	 * @generated
	 */
	public IDiagramDocument getDiagramDocument(Object element) {
		IDocument doc = getDocument(element);
		if (doc instanceof IDiagramDocument) {
			return (IDiagramDocument) doc;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
	}

	/**
	 * @generated
	 */
	protected class ResourceSetInfo extends ElementInfo {

		/**
		 * @generated
		 */
		private long myModificationStamp = IResource.NULL_STAMP;

		/**
		 * @generated
		 */
		private WorkspaceSynchronizer mySynchronizer;

		/**
		 * @generated
		 */
		private LinkedList<Resource> myUnSynchronizedResources = new LinkedList<Resource>();

		/**
		 * @generated
		 */
		private IDiagramDocument myDocument;

		/**
		 * @generated
		 */
		private IEditorInput myEditorInput;

		/**
		 * @generated
		 */
		private boolean myUpdateCache = true;

		/**
		 * @generated
		 */
		private boolean myModifiable = false;

		/**
		 * @generated
		 */
		private boolean myReadOnly = true;

		/**
		 * @generated
		 */
		private ResourceSetModificationListener myResourceSetListener;

		/**
		 * @generated
		 */
		public ResourceSetInfo(IDiagramDocument document,
				IEditorInput editorInput) {
			super(document);
			myDocument = document;
			myEditorInput = editorInput;
			startResourceListening();
			myResourceSetListener = new ResourceSetModificationListener(this);
			getResourceSet().eAdapters().add(myResourceSetListener);
		}

		/**
		 * @generated
		 */
		public long getModificationStamp() {
			return myModificationStamp;
		}

		/**
		 * @generated
		 */
		public void setModificationStamp(long modificationStamp) {
			myModificationStamp = modificationStamp;
		}

		/**
		 * @generated
		 */
		public TransactionalEditingDomain getEditingDomain() {
			return myDocument.getEditingDomain();
		}

		/**
		 * @generated
		 */
		public ResourceSet getResourceSet() {
			return getEditingDomain().getResourceSet();
		}

		/**
		 * @generated
		 */
		public Iterator<Resource> getLoadedResourcesIterator() {
			return new ArrayList<Resource>(getResourceSet().getResources())
					.iterator();
		}

		/**
		 * @generated
		 */
		public IEditorInput getEditorInput() {
			return myEditorInput;
		}

		/**
		 * @generated NOT
		 */
		public void dispose() {
			stopResourceListening();
			getResourceSet().eAdapters().remove(myResourceSetListener);
			// for (Iterator<Resource> it = getLoadedResourcesIterator(); it
			// .hasNext();) {
			// Resource resource = it.next();
			// resource.unload();
			// }
			// getEditingDomain().dispose();
			Resource res = ((Diagram) fDocument.getContent()).getElement().eResource();
			IEditorReference[] editorReferences = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();
			boolean isCurrentResourceUsed = EditorEMFTransactionalCommands
					.Get().testXMIResourceUnload((XMIResource) res,
							editorReferences);
			
		}

		/**
		 * @generated
		 */
		public boolean isSynchronized() {
			return myUnSynchronizedResources.size() == 0;
		}

		/**
		 * @generated
		 */
		public void setUnSynchronized(Resource resource) {
			myUnSynchronizedResources.add(resource);
		}

		/**
		 * @generated
		 */
		public void setSynchronized(Resource resource) {
			myUnSynchronizedResources.remove(resource);
		}

		/**
		 * @generated
		 */
		public final void stopResourceListening() {
			mySynchronizer.dispose();
			mySynchronizer = null;
		}

		/**
		 * @generated
		 */
		public final void startResourceListening() {
			mySynchronizer = new WorkspaceSynchronizer(getEditingDomain(),
					new SynchronizerDelegate());
		}

		/**
		 * @generated
		 */
		public boolean isUpdateCache() {
			return myUpdateCache;
		}

		/**
		 * @generated
		 */
		public void setUpdateCache(boolean update) {
			myUpdateCache = update;
		}

		/**
		 * @generated
		 */
		public boolean isModifiable() {
			return myModifiable;
		}

		/**
		 * @generated
		 */
		public void setModifiable(boolean modifiable) {
			myModifiable = modifiable;
		}

		/**
		 * @generated
		 */
		public boolean isReadOnly() {
			return myReadOnly;
		}

		/**
		 * @generated
		 */
		public void setReadOnly(boolean readOnly) {
			myReadOnly = readOnly;
		}

		/**
		 * @generated
		 */
		private class SynchronizerDelegate implements
				WorkspaceSynchronizer.Delegate {

			/**
			 * @generated
			 */
			public void dispose() {
			}

			/**
			 * @generated NOT
			 */
			public boolean handleResourceChanged(final Resource resource) {
				// Get target EMF and GMF resource URI
				// Avoid other resources
				URI emf = null;
				URI gmf = null;
				if (fDocument.getContent() != null
						&& fDocument.getContent() instanceof Diagram) {
					emf = EcoreUtil.getURI(
							((Diagram) fDocument.getContent()).getElement())
							.trimFragment();
				}
				gmf = EditUIUtil.getURI((IEditorInput) fElement);
				if (!resource.getURI().equals(emf)
						&& !resource.getURI().equals(gmf)) {
					return true;
				}

				synchronized (ResourceSetInfo.this) {
					if (ResourceSetInfo.this.fCanBeSaved) {
						ResourceSetInfo.this.setUnSynchronized(resource);
						return true;
					}
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						handleElementChanged(ResourceSetInfo.this, resource,
								null);
					}
				});
				return true;
			}

			/**
			 * @generated
			 */
			public boolean handleResourceDeleted(Resource resource) {
				synchronized (ResourceSetInfo.this) {
					if (ResourceSetInfo.this.fCanBeSaved) {
						ResourceSetInfo.this.setUnSynchronized(resource);
						return true;
					}
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						fireElementDeleted(ResourceSetInfo.this
								.getEditorInput());
					}
				});
				return true;
			}

			/**
			 * @generated
			 */
			public boolean handleResourceMoved(Resource resource,
					final URI newURI) {
				synchronized (ResourceSetInfo.this) {
					if (ResourceSetInfo.this.fCanBeSaved) {
						ResourceSetInfo.this.setUnSynchronized(resource);
						return true;
					}
				}
				if (myDocument.getDiagram().eResource() == resource) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							handleElementMoved(
									ResourceSetInfo.this.getEditorInput(),
									newURI);
						}
					});
				} else {
					handleResourceDeleted(resource);
				}
				return true;
			}

		}

	}

	/**
	 * @generated
	 */
	private class ResourceSetModificationListener extends EContentAdapter {

		/**
		 * @generated
		 */
		private NotificationFilter myModifiedFilter;

		/**
		 * @generated
		 */
		private ResourceSetInfo myInfo;

		/**
		 * @generated
		 */
		public ResourceSetModificationListener(ResourceSetInfo info) {
			myInfo = info;
			myModifiedFilter = NotificationFilter
					.createEventTypeFilter(Notification.SET)
					.or(NotificationFilter
							.createEventTypeFilter(Notification.UNSET))
					.and(NotificationFilter.createFeatureFilter(Resource.class,
							Resource.RESOURCE__IS_MODIFIED));
		}

		/**
		 * @generated NOT
		 */
		public void notifyChanged(Notification notification) {
			if (notification.getNotifier() instanceof ResourceSet) {
				super.notifyChanged(notification);
			}
			if (!notification.isTouch()
					&& myModifiedFilter.matches(notification)) {
				if (notification.getNotifier() instanceof Resource) {
					Resource resource = (Resource) notification.getNotifier();
					if (resource.isLoaded()) {
						boolean modified = false;
						// Get target EMF and GMF resource URI
						URI emf = null;
						URI gmf = null;

						if (myInfo.fDocument.getContent() != null
								&& myInfo.fDocument.getContent() instanceof Diagram) {
							emf = EcoreUtil.getURI(
									((Diagram) myInfo.fDocument.getContent())
											.getElement()).trimFragment();
						}
						gmf = EditUIUtil.getURI((IEditorInput) myInfo.fElement);

						for (Iterator/* <org.eclipse.emf.ecore.resource.Resource> */it = myInfo
								.getLoadedResourcesIterator(); it.hasNext()
								&& !modified;) {
							Resource nextResource = (Resource) it.next();
							// Avoid other resources
							if (nextResource.getURI().equals(emf)
									|| nextResource.getURI().equals(gmf)) {

								if (nextResource.isLoaded()) {
									modified = nextResource.isModified();
								}
							}
						}
						boolean dirtyStateChanged = false;
						synchronized (myInfo) {
							if (modified != myInfo.fCanBeSaved) {
								myInfo.fCanBeSaved = modified;
								dirtyStateChanged = true;
							}
							if (!resource.isModified()) {
								myInfo.setSynchronized(resource);
							}
						}
						if (dirtyStateChanged) {
							fireElementDirtyStateChanged(
									myInfo.getEditorInput(), modified);

							if (!modified) {
								myInfo.setModificationStamp(computeModificationStamp(myInfo));
							}
						}
					}
				}
			}
			// Update diagram
			if (notification.getNotifier() instanceof EObject
					|| notification.getNotifier() instanceof Resource) {
				URI target = null;
				if (notification.getNotifier() instanceof EObject) {
					target = EcoreUtil.getURI(
							(EObject) notification.getNotifier())
							.trimFragment();
				} else {
					target = ((Resource) notification.getNotifier()).getURI();
				}
				URI emf = null;
				if (myInfo.fDocument.getContent() != null
						&& myInfo.fDocument.getContent() instanceof Diagram) {
					emf = EcoreUtil.getURI(
							((Diagram) myInfo.fDocument.getContent())
									.getElement()).trimFragment();
				}

				final URI gmf = EditUIUtil
						.getURI((IEditorInput) myInfo.fElement);

				if (target.equals(emf)) {
					updateExecution();
				}

			}

		}

		private void updateExecution() {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (PlatformUI.getWorkbench() == null
							|| PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow() == null
							|| PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage() == null) {
						return;
					}
					IEditorReference[] editors = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getEditorReferences();
					for (IEditorReference editor : editors) {
						try {
							if (editor.getEditorInput().equals(
									myInfo.myEditorInput)
									&& editor.getId().equals(
											BVRMetamodelDiagramEditor.ID)) {

								EditPart ep = null;
								if (editor.getEditor(true) instanceof GraphicalEditor) {
									GraphicalEditor graphicalEditor = (GraphicalEditor) editor
											.getEditor(true);
									ep = (RootEditPart) graphicalEditor
											.getAdapter(EditPart.class);
								}

								if (ep.getRoot().getContents() instanceof EditPart
										&& ep.getRoot().getContents()
												.getModel() instanceof View) {
									EObject modelElement = ((View) ep.getRoot()
											.getContents().getModel())
											.getElement();
									List editPolicies = CanonicalEditPolicy
											.getRegisteredEditPolicies(modelElement);
									for (Iterator it = editPolicies.iterator(); it
											.hasNext();) {
										CanonicalEditPolicy nextEditPolicy = (CanonicalEditPolicy) it
												.next();
										nextEditPolicy.refresh();
									}

								}
							}
						} catch (PartInitException e) {
							BVRMetamodelDiagramEditorPlugin.getInstance()
									.logError("Update command fail");
						}
					}

				}
			});
		}
	}

}
