package no.sintef.bvr.table.resolution.custom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryContentProvider;

import bvr.VClassifier;
import bvr.VInstance;
import bvr.VSpec;
import bvr.VSpecResolution;

public class CustomAdapterFactoryContentProvider extends
		TransactionalAdapterFactoryContentProvider {

	public CustomAdapterFactoryContentProvider(
			TransactionalEditingDomain domain, AdapterFactory adapterFactory) {
		super(domain, adapterFactory);
	}

	@Override
	public Object[] getChildren(Object object) {
		if (!(object instanceof VSpecResolution)) {
			// This is not VSpecResolution case
			return super.getChildren(object);
		} else if (object instanceof VirtualVClassifier) {
			// This object is Virtual.
			// This children are VInstance(ResolvedVClassifier is matched) of
			// child of parent of the object

			List<VSpecResolution> resolutionList = new ArrayList<VSpecResolution>();
			EList<VSpecResolution> childs = ((VirtualVClassifier) object)
					.getParent().getChild();

			for (Iterator<VSpecResolution> iterator = childs.iterator(); iterator
					.hasNext();) {
				VSpecResolution target = iterator.next();
				if (target instanceof VInstance
						&& ((VirtualVClassifier) object).getResolvedVSpec() == ((VInstance) target)
								.getResolvedVSpec()) {
					resolutionList.add((VSpecResolution) target);
				}
			}
			return resolutionList.toArray();
		} else {
			// For all VSpecResolution which may have VInstance,
			// Add Virtual VClassisfer as child to the object.
			List<VSpecResolution> resolutionList = new ArrayList<VSpecResolution>();
			Object[] objList = super.getChildren(object);

			for (int i = 0; i < objList.length; i++) {
				if (!(objList[i] instanceof VInstance)
						&& objList[i] instanceof VSpecResolution) {
					resolutionList.add((VSpecResolution) objList[i]);
				}
			}

			List<VSpec> vspecList = ((VSpecResolution) object)
					.getResolvedVSpec().getChild();
			for (Iterator<VSpec> iterator = vspecList.iterator(); iterator
					.hasNext();) {
				VSpec vs = iterator.next();
				if (vs instanceof VClassifier) {
					VirtualVClassifier virtual = VirtualVClassifierHolder
							.getInstance().getVirtualInstance(
									((VSpecResolution) object),
									(VClassifier) vs);
					if (virtual == null) {
						virtual = (VirtualVClassifier) new VirtualVClassifierImpl();
						virtual.setResolvedVSpec(vs);
						virtual.setParent((VSpecResolution) object);
						VirtualVClassifierHolder.getInstance()
								.addVirtualClassifier(virtual);
					}
					resolutionList.add(virtual);
				}
			}

			return resolutionList.toArray();
		}
	}

	@Override
	public boolean hasChildren(Object object) {
		if (!(object instanceof VSpecResolution)) {
			return super.hasChildren(object);
		} else if (object instanceof VirtualVClassifier) {
			// When Virtual Classifier's parent's children are only VInstance of
			// zero, return false;
			EList<VSpecResolution> childs = ((VirtualVClassifier) object)
					.getParent().getChild();

			for (Iterator<VSpecResolution> iterator = childs.iterator(); iterator
					.hasNext();) {
				VSpecResolution target = iterator.next();
				if (target instanceof VInstance
						&& ((VirtualVClassifier) object).getResolvedVSpec() == ((VInstance) target)
								.getResolvedVSpec()) {
					return true;
				}
			}
			return false;
		} else {
			// When Normal VSpecResolution may have VInstance as a child,
			// return true;
			List<VSpec> vspecList = ((VSpecResolution) object)
					.getResolvedVSpec().getChild();
			for (Iterator<VSpec> iterator = vspecList.iterator(); iterator
					.hasNext();) {
				VSpec vs = iterator.next();
				if (vs instanceof VClassifier) {
					return true;
				}
			}

			return super.hasChildren(object);
		}
	}

	@Override
	public Object getParent(Object object) {
		if (!(object instanceof VSpecResolution)) {
			return super.getParent(object);
		} else if (object instanceof VirtualVClassifier) {
			return ((VirtualVClassifier) object).getParent();
		} else if (object instanceof VInstance) {
			VSpecResolution parent = (VSpecResolution) ((VInstance) object)
					.eContainer();
			for (int i = 0; i < this.getChildren(parent).length; i++) {
				if (this.getChildren(parent)[i] instanceof VirtualVClassifier
						&& ((VirtualVClassifier) this.getChildren(parent)[i])
								.getResolvedVSpec() == ((VInstance) object)
								.getResolvedVSpec()) {
					return this.getChildren(parent)[i];
				}
			}
			return null;
		} else {
			return super.getParent(object);
		}

	}

}
