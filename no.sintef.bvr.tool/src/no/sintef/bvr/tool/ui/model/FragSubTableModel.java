/*******************************************************************************
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package no.sintef.bvr.tool.ui.model;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import no.sintef.bvr.tool.common.Constants;
import no.sintef.bvr.tool.exception.UnexpectedException;
import no.sintef.bvr.tool.primitive.DataItem;
import no.sintef.bvr.tool.primitive.impl.DataNamedElementItem;
import no.sintef.bvr.tool.primitive.impl.DataVSpecItem;

import org.eclipse.emf.common.util.EList;

import bvr.FragmentSubstitution;
import bvr.VSpec;
import bvr.VariationPoint;

public class FragSubTableModel extends AbstractTableModel
		implements TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6757147907729864204L;
	private String[] columnNames = {Constants.FRAG_SUB_VP_CLMN_NAME, Constants.FRAG_SUB_VSPEC_CLMN_NAME};
	private ArrayList<ArrayList<DataItem>> data;
	
	public FragSubTableModel(EList<VariationPoint> varPoints, ArrayList<DataVSpecItem> vSpecMap){
		data = new ArrayList<ArrayList<DataItem>>();
		loadData(varPoints, vSpecMap);
	}
	
	public FragSubTableModel() {
		data = new ArrayList<ArrayList<DataItem>>();
	}

	private DataVSpecItem getVSpecItem(VSpec vSpec, ArrayList<DataVSpecItem> vSpecMap){
		for(DataItem item : vSpecMap){
			if(item.getNamedElement().equals(vSpec)){
				return (DataVSpecItem) item;
			}
		}
		return null;
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
		DataItem item = data.get(rowIndex).get(columnIndex);
		return item;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch(columnIndex){
			case Constants.FRAG_SUBS_VARIATION_POINT_CLMN :{
				data.get(rowIndex).set(columnIndex, (DataNamedElementItem) aValue);
			};break;
			case Constants.FRAG_SUBS_VSPEC_CLMN:{
				data.get(rowIndex).set(columnIndex, (DataVSpecItem) aValue);
			}; break;
			default : {
				throw new UnsupportedOperationException("table setter is not implemented for this column " + columnIndex);
			}
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class cl;
		switch(columnIndex) {
			case Constants.FRAG_SUBS_VSPEC_CLMN : cl = DataVSpecItem.class;
				break;
			case Constants.FRAG_SUBS_VARIATION_POINT_CLMN : cl = DataNamedElementItem.class;
				break;
			default : cl = String.class;
				break;
		}
		return cl;
	}
	

	public ArrayList<ArrayList<DataItem>> getData() {
		return data;
	}
	
	public void setData(EList<VariationPoint> varPoints, ArrayList<DataVSpecItem> vSpecMap){
		loadData(varPoints, vSpecMap);
		this.fireTableDataChanged();
	}
	
	private void loadData(EList<VariationPoint> varPoints, ArrayList<DataVSpecItem> vSpecMap){
		data = new ArrayList<ArrayList<DataItem>>();
		for(VariationPoint varPoint : varPoints){
			if(varPoint instanceof FragmentSubstitution){
				FragmentSubstitution fragmentSubstitution = (FragmentSubstitution) varPoint;
				VSpec referenceVSpec = fragmentSubstitution.getBindingVSpec();
				if(referenceVSpec == null){
					DataNamedElementItem cellFSN = new DataNamedElementItem(new JLabel(fragmentSubstitution.getName()), fragmentSubstitution);
					//data item with null vspec is the first in the list
					DataVSpecItem cellVSN = (DataVSpecItem) vSpecMap.get(0);

					ArrayList<DataItem> row = new ArrayList<DataItem>(Arrays.asList(cellFSN, cellVSN));
					data.add(row);
				}else{
					DataNamedElementItem cellFSN = new DataNamedElementItem(new JLabel(fragmentSubstitution.getName()), fragmentSubstitution);
					DataVSpecItem cellVSN = getVSpecItem(referenceVSpec, vSpecMap);
					
					if(cellVSN == null)
						throw new UnexpectedException("can not fined referenced VSpec : " + referenceVSpec + " in the map " + vSpecMap);

					ArrayList<DataItem> row = new ArrayList<DataItem>(Arrays.asList(cellFSN, cellVSN));
					data.add(row);
				}
			}
		}
	}
}
