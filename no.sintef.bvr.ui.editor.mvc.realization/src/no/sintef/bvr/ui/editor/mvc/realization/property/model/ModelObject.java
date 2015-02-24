package no.sintef.bvr.ui.editor.mvc.realization.property.model;

import java.beans.PropertyChangeSupport;

public class ModelObject extends PropertyChangeSupport {

	private static final long serialVersionUID = 1580725381424034833L;
	
	public ModelObject(Object sourceBean) {
		super(sourceBean);
	}

}
