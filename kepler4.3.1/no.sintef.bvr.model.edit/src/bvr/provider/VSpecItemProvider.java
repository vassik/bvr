/**
 */
package bvr.provider;


import bvr.BvrFactory;
import bvr.BvrPackage;
import bvr.VSpec;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link bvr.VSpec} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VSpecItemProvider
	extends VPackageableItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VSpecItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addResolutionTimePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Resolution Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addResolutionTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VSpec_resolutionTime_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VSpec_resolutionTime_feature", "_UI_VSpec_type"),
				 BvrPackage.Literals.VSPEC__RESOLUTION_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(BvrPackage.Literals.VSPEC__GROUP_MULTIPLICITY);
			childrenFeatures.add(BvrPackage.Literals.VSPEC__OWNED_VSPEC_DERIVATION);
			childrenFeatures.add(BvrPackage.Literals.VSPEC__CHILD);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((VSpec)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_VSpec_type") :
			getString("_UI_VSpec_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(VSpec.class)) {
			case BvrPackage.VSPEC__RESOLUTION_TIME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case BvrPackage.VSPEC__GROUP_MULTIPLICITY:
			case BvrPackage.VSPEC__OWNED_VSPEC_DERIVATION:
			case BvrPackage.VSPEC__CHILD:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__GROUP_MULTIPLICITY,
				 BvrFactory.eINSTANCE.createMultiplicityInterval()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__OWNED_VSPEC_DERIVATION,
				 BvrFactory.eINSTANCE.createVSpecDerivation()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__OWNED_VSPEC_DERIVATION,
				 BvrFactory.eINSTANCE.createChoiceDerivation()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__OWNED_VSPEC_DERIVATION,
				 BvrFactory.eINSTANCE.createVariableDerivation()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__OWNED_VSPEC_DERIVATION,
				 BvrFactory.eINSTANCE.createCVSpecDerivation()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__CHILD,
				 BvrFactory.eINSTANCE.createChoice()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__CHILD,
				 BvrFactory.eINSTANCE.createVClassifier()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__CHILD,
				 BvrFactory.eINSTANCE.createVariable()));

		newChildDescriptors.add
			(createChildParameter
				(BvrPackage.Literals.VSPEC__CHILD,
				 BvrFactory.eINSTANCE.createCVSpec()));
	}

}
