package no.sintef.cvl.ui.adapters.impl;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.eclipse.emf.common.util.EList;

import no.sintef.cvl.ui.common.Constants;
import no.sintef.cvl.ui.exceptions.AbstractUIError;
import no.sintef.cvl.ui.exceptions.ModelError;

import cvl.BoundaryElementBinding;
import cvl.FragmentSubstitution;
import cvl.FromBinding;
import cvl.FromPlacement;
import cvl.FromReplacement;
import cvl.PlacementFragment;
import cvl.ReplacementFragmentType;
import cvl.ToBinding;
import cvl.ToPlacement;
import cvl.ToReplacement;

public class BindingTableModel extends AbstractTableModel implements TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6876766227353366136L;
	private FragmentSubstitution fs;
	private String[] columnNames = {
			Constants.BINDING_TYPE_CLMN_NAME,
			Constants.BINDING_PROPERTY_CLMN_NAME,
			Constants.BINDING_VALUE_CLMN_NAME
		};
	private ArrayList<ArrayList<Object>> data; 
	
	public BindingTableModel(FragmentSubstitution fragSub) throws AbstractUIError{
		fs = fragSub;
		data = new ArrayList<ArrayList<Object>>();
		
		PlacementFragment placement = fs.getPlacement();
		ReplacementFragmentType replacement = fs.getReplacement();
		if(placement != null && replacement != null){
			ArrayList<ArrayList<Object>> dataToBindings = new ArrayList<ArrayList<Object>>();
			ArrayList<ArrayList<Object>> dataFromBindings = new ArrayList<ArrayList<Object>>();
			EList<BoundaryElementBinding> bindings = fs.getBoundaryElementBinding();
			for(BoundaryElementBinding binding : bindings){
				if(binding instanceof ToBinding){
					ToBinding toBinding = (ToBinding) binding;
					ToPlacement toPlacement = toBinding.getToPlacement();
					ToReplacement toReplacement = toBinding.getToReplacement();
					if(toPlacement == null || toReplacement == null){
						throw new ModelError("binding should reference toPlacement and toReplacement");
					}
					
					String typeName = Constants.BINDING_TYPE_TO_BINDING_NAME;
					JLabel labelToP = new JLabel(toPlacement.getName());
					DataNamedElementItem propToP = new DataNamedElementItem(labelToP, toPlacement);
					
					JLabel labelToR = new JLabel(toReplacement.getName());
					DataNamedElementItem propToR = new DataNamedElementItem(labelToR, toReplacement);
					
					ArrayList<Object> row = new ArrayList<Object>();
					row.add(typeName);
					row.add(labelToP);
					row.add(labelToR);
					dataToBindings.add(row);
				} else if (binding instanceof FromBinding){
					FromBinding fromBinding = (FromBinding) binding;
					FromPlacement fromPlacement = fromBinding.getFromPlacement();
					FromReplacement fromReplacement = fromBinding.getFromReplacement();
					if(fromPlacement == null || fromReplacement == null){
						throw new ModelError("binding should reference fromPlacement and fromReplacement");
					}
					
					String typeName = Constants.BINDING_TYPE_FROM_BINDING_NAME;
					JLabel labelFromP = new JLabel(fromPlacement.getName());
					DataNamedElementItem propFromP = new DataNamedElementItem(labelFromP, fromPlacement);
					
					JLabel labelFromR = new JLabel(fromReplacement.getName());
					DataNamedElementItem propFromR = new DataNamedElementItem(labelFromR, fromReplacement);
					
					ArrayList<Object> row = new ArrayList<Object>();
					row.add(typeName);
					row.add(labelFromR);
					row.add(labelFromP);
					dataFromBindings.add(row);
				}
			}
			data.addAll(dataToBindings);
			data.addAll(dataFromBindings);
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class cl;
		switch(columnIndex){
			case Constants.BINDING_PROP_CLMN : {
				cl = DataNamedElementItem.class;
			}; break;
			case Constants.BINDING_VALUE_CLMN : {
				cl = DataNamedElementItem.class;
			}
			case Constants.BINDING_TYPE_CLMN : {
				cl = String.class;
			}
			default : {
				cl = String.class;
			}
		}
		return cl;
	}

}
