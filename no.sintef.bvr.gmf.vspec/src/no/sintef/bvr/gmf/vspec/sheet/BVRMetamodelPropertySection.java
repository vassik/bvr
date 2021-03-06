package no.sintef.bvr.gmf.vspec.sheet;

import no.sintef.bvr.gmf.vspec.custom.property.CustomPropertySource;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.gmf.tooling.runtime.sheet.DefaultPropertySection;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

/**
 * @generated NOT
 */
public class BVRMetamodelPropertySection extends DefaultPropertySection
		implements IPropertySourceProvider {

	/**
	 * Modify/unwrap selection.
	 * 
	 * @generated
	 */
	@Override
	protected Object transformSelection(Object selected) {
		selected = /* super. */transformSelectionToDomain(selected);
		return selected;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public IPropertySource getPropertySource(Object object) {
		AdapterFactory af = getAdapterFactory(object);
		IItemPropertySource ips = null;
		if (af != null) {
			ips = (IItemPropertySource) af.adapt(object,
					IItemPropertySource.class);
		}
		if (object != null && ips != null) {
			return new CustomPropertySource(object, ips);
		} else {
			return super.getPropertySource(object);
		}
	}
}
