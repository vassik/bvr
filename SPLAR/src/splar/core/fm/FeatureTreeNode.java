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
package splar.core.fm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import splar.core.constraints.BooleanVariableInterface;

public class FeatureTreeNode extends DefaultMutableTreeNode implements BooleanVariableInterface, Cloneable {

	public static final int UNKNOWN    = -1;
	public static final int DESELECTED = 0;
	public static final int SELECTED   = 1;
	
	protected int value = UNKNOWN;
	protected boolean isImmutable = false;
	protected Map<String,Object> properties;
	
	private String name = "";
	private String id = "";
	
	private HashMap<String,FeatureTreeNodeState> states;
	
	// list variables that were propagated from this node
	private List<FeatureTreeNode> propagatedNodes = new Vector<FeatureTreeNode>();
	
	// points to the node that caused this node automatic assignment (propagation)
	private BooleanVariableInterface propagationVar = null;
	
	public FeatureTreeNode( String id, String name) {
		this.id = id;
		this.name = name;
		states = new HashMap<String, FeatureTreeNodeState>();
		reset();
	}
	
	public FeatureTreeNodeState getState(String stateID) {
		return states.get(stateID);
	}
	
	public void saveState(String stateID) {
		FeatureTreeNodeState state = new FeatureTreeNodeState(stateID, this);
		state.save();
		states.put(stateID, state);
	}
	
	public void restoreState(String stateID) {
		restoreState(stateID, true);
	}
	
	public void restoreState(String stateID, boolean discardState) {		
		FeatureTreeNodeState state = states.get(stateID);
		if ( state != null ) {
			state.restore();
			if ( discardState ) {
				discardState(stateID);
			}
		}
	}
	
	public void discardState(String stateID) {
		FeatureTreeNodeState state = states.get(stateID);
		if ( state != null ) {
			state.discard();
			states.remove(stateID);
		}
	}
	
	public void reset() {
		this.value = UNKNOWN;
		properties = new LinkedHashMap<String, Object>();
	}
	
	public void setProperty(String name, Object object) {
		properties.put(name, object);
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public void setID(String ID) {
		this.id = ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAncestorOf(FeatureTreeNode node) {
		FeatureTreeNode ancestor = node;
		do {
			ancestor = (FeatureTreeNode)ancestor.getParent();
			if ( this == ancestor ) {
				return true;
			}
		}
		while( ancestor != null);
		return false;
	}
	
	public void setPropagatedNodes(List<FeatureTreeNode> propagatedNodes) {
		this.propagatedNodes = propagatedNodes;
	}
	
	public List<FeatureTreeNode> getPropagatedNodes() {
		return propagatedNodes;
	}
	
	public String getID() {
		return id;		
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {		
		return getName();
	}
	
	public void setImmutable(boolean immutable) {
		isImmutable = immutable;
	}
	
	public boolean isImmutable() {
		return isImmutable;
	}
	
	public boolean isInstantiated() {
		return (value != UNKNOWN); 
	}
	
	public void resetValue() {
		if ( !isImmutable ) {
			value = UNKNOWN;
		}
	}
	
	public boolean isPropagation() {
		return (propagationVar != null);
	}
	
	public BooleanVariableInterface getPropagationVariable() {
		return propagationVar;
	}

	public void assignValue(int value, BooleanVariableInterface propagationVar) {
		if ( !isImmutable ) {			
			this.value = value;
			this.propagationVar = propagationVar;
		}
	}
	
	public void assignValue(int value) {
		if ( !isImmutable ) {
			this.value = value;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public int hashCode() {
		return id.hashCode();		
	}
	
	public boolean equals(Object var) {
		if ( var == null )
			return false;
//		if ( var instanceof BooleanVariable) {
//			return id.equals(((BooleanVariable)var).getID());
//		}
		return id.equals(((BooleanVariableInterface)var).getID());
	}
	
	public String toString() {
		return getName() + "(" + getID() +")";   // <!-- :value=" + value +"-->";
	}
	
	public void dump() {
		System.out.println(getName() + "(" + getID() +") = "+ value );
		System.out.println("Properties");
		for( String key : properties.keySet() ) {
			System.out.println("-> " + key + " = " + properties.get(key));
		}
	}
		
	private Object attachedData;
	public void attachData(Object object) {
		attachedData= object;
	}
	
	public void resetAttachedData() {
		attachedData = null;
	}
	
	public Object getAttachedData() {
		return attachedData;
	}
}
