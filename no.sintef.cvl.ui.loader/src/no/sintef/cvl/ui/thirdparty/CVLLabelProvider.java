package no.sintef.cvl.ui.thirdparty;

import java.util.ArrayList;
import java.util.Hashtable;

import no.sintef.cvl.ui.common.Constants;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


public class CVLLabelProvider implements ILabelProvider, ITableLabelProvider, INotifyChangedListener, IColorProvider, ITableColorProvider, IFontProvider, ITableFontProvider {
	
	protected AdapterFactoryLabelProvider adapterFactoryLabelProvider = null;
	
	protected Hashtable<Object, Integer> highlight = new Hashtable<Object, Integer>();
	
	public CVLLabelProvider(AdapterFactoryLabelProvider adapterFactoryLabelProvider) {
		this.adapterFactoryLabelProvider = adapterFactoryLabelProvider;
	}
	
	FontRegistry registry = new FontRegistry();
	
	@Override
	public Font getFont(Object element) {
		return getFont(element, 0);
	}

	@Override
	public Color getBackground(Object element) {
		return getBackground(element, 0);
	}

	@Override
	public Color getForeground(Object element) {
		return getForeground(element, 0);
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		if (highlight.containsKey(element)) {
			return registry.getBold(Display.getCurrent().getSystemFont().getFontData()[0].getName());
		}
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		if (!highlight.containsKey(element)) return null;
		
		switch (highlight.get(element)) {
		case ICVLEnabledEditor.HL_PLACEMENT : 
			return Constants.RED;
		case ICVLEnabledEditor.HL_PLACEMENT_OUT : 
			return Constants.ORANGE;
		case ICVLEnabledEditor.HL_PLACEMENT_IN : 
			return Constants.VIOLET;
		case ICVLEnabledEditor.HL_REPLACEMENT : 
			return Constants.BLUE;
		case ICVLEnabledEditor.HL_REPLACEMENT_OUT : 
			return Constants.GREEN;
		case ICVLEnabledEditor.HL_REPLACEMENT_IN : 
			return Constants.PINK;
		default : 
			return null;
		}
	}

	@Override
	public Image getImage(Object element) {
		return adapterFactoryLabelProvider.getImage(element);
	}

	@Override
	public String getText(Object element) {
		return adapterFactoryLabelProvider.getText(element);
	}

	@Override
	public void notifyChanged(Notification notification) {
		adapterFactoryLabelProvider.notifyChanged(notification);
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return adapterFactoryLabelProvider.getColumnImage(element, columnIndex);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		return adapterFactoryLabelProvider.getColumnText(element, columnIndex);
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		adapterFactoryLabelProvider.addListener(listener);
		
	}

	@Override
	public void dispose() {
		adapterFactoryLabelProvider.dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return adapterFactoryLabelProvider.isLabelProperty(element, property);
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		adapterFactoryLabelProvider.removeListener(listener);
	}
	
	public void clearHighlighting() {
		highlight.clear();
	}

	public void highlightObjects(ArrayList<Object> objects, int type) {
		if (type == ICVLEnabledEditor.HL_NONE) {
			for(Object o : objects) highlight.remove(o);
		}
		else {
			for(Object o : objects) highlight.put(o, type);
		}
	}
	
	public void highlightObject(Object o, int type) {
		if (type == ICVLEnabledEditor.HL_NONE) {
			highlight.remove(o);
		}
		else {
			highlight.put(o, type);
		}
	}
	
}
