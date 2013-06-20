package no.sintef.cvl.ui.commands.events;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import no.sintef.cvl.ui.common.Constants;
import no.sintef.cvl.ui.common.NullVSpec;
import no.sintef.cvl.ui.exceptions.UnexpectedException;
import no.sintef.cvl.ui.loader.CVLView;
import no.sintef.cvl.ui.models.BindingTableModel;
import no.sintef.cvl.ui.primitives.DataItem;
import no.sintef.cvl.ui.primitives.impl.DataBindingItem;
import no.sintef.cvl.ui.primitives.impl.DataBoundaryItem;
import no.sintef.cvl.ui.primitives.impl.DataNamedElementItem;
import cvl.BoundaryElementBinding;
import cvl.ConfigurableUnit;
import cvl.FromBinding;
import cvl.FromPlacement;
import cvl.ToBinding;
import cvl.ToReplacement;
import cvl.VSpec;
import cvl.VariationPoint;

public class BindingModelTableEvent implements TableModelListener {

	private ConfigurableUnit cu;
	private CVLView view;

	public BindingModelTableEvent(ConfigurableUnit cu, CVLView view) {
		this.cu = cu;
		this.view = view;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		BindingTableModel model = (BindingTableModel) e.getSource();
		ArrayList<ArrayList<Object>> data = model.getData();
		if(TableModelEvent.UPDATE == e.getType()){
			if(e.getLastRow() == e.getFirstRow()){
				int rowIndex = e.getLastRow();
				int columnIndex = e.getColumn();
				DataBindingItem bindingCell = (DataBindingItem) data.get(rowIndex).get(Constants.BINDING_TYPE_CLMN);
				BoundaryElementBinding binding = (BoundaryElementBinding) bindingCell.getNamedElement();
				if(columnIndex == Constants.BINDING_VALUE_CLMN){
					DataBoundaryItem valueCell = (DataBoundaryItem) data.get(rowIndex).get(Constants.BINDING_VALUE_CLMN);
					VariationPoint boundary = (VariationPoint) valueCell.getNamedElement();
					if(binding instanceof ToBinding){
						ToBinding toBinding = (ToBinding) binding;
						toBinding.setToReplacement((ToReplacement) boundary);
					}else if(binding instanceof FromBinding) {
						FromBinding fromBinding = (FromBinding) binding;
						fromBinding.setFromPlacement((FromPlacement) boundary);
					}else{
						try {
							throw new UnexpectedException("OMG this should never happen!");
						} catch (UnexpectedException e1) {
							e1.printStackTrace();
						}
					}
					//should be uncommented of course, when we implement a smart way to update views
					//view.notifyRelalizationViewUpdate();
				}else{
					throw new UnsupportedOperationException("edition operaton for this celll is not implemented");
				}
			}
		}

	}

}