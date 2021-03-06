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
package no.sintef.bvr.tool.strategy.impl;

import org.eclipse.emf.ecore.EObject;

import bvr.BVRModel;
import bvr.CompoundNode;
import bvr.VType;
import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.exception.UnexpectedException;
import no.sintef.bvr.tool.primitive.SymbolTableEObject;
import no.sintef.bvr.tool.primitive.impl.SymbolCompoundNode;
import no.sintef.bvr.tool.primitive.impl.SymbolTableCompoundNode;
import no.sintef.bvr.tool.strategy.ContextBuilderStrategy;

public class CompoundNodeScopeBuilderStrategy implements ContextBuilderStrategy {

	private SymbolTableEObject globalScope;
	

	@Override
	public void reset() {
		globalScope = null;
	}

	@Override
	public SymbolTableEObject getRootScope() {
		return globalScope;
	}

	@Override
	public void testEObject(EObject parent, EObject child) {
		if(parent instanceof BVRModel && child instanceof CompoundNode && globalScope == null){
			globalScope = new SymbolTableCompoundNode((CompoundNode) child);
			globalScope.insert(new SymbolCompoundNode((CompoundNode) child));
			return;
		}
		
		if(!(parent instanceof CompoundNode && child instanceof CompoundNode)){
			//Context.eINSTANCE.logger.debug("VSpecContextBuilderStrategyImpl does not fitt for these model elements " + parent + " " + child);
			return;
		}
		
		CompoundNode childCN = (CompoundNode) child;
		
		if(globalScope == null)
			return;
		
		SymbolTableEObject table = findParentScope(child, globalScope);
		if(table == null)
			return;
			//throw new UnexpectedException("can not find scope for the element : " + child);
		
		if(childCN instanceof VType){
			SymbolTableCompoundNode tableChild = new SymbolTableCompoundNode(childCN);
			table.getChildren().add(tableChild);
			tableChild.setParent(table);
			table = tableChild;
		}
		
		table.insert(new SymbolCompoundNode(childCN));
	}
	
	private SymbolTableEObject findParentScope(EObject eObject, SymbolTableEObject tableEObject){
		SymbolTableEObject table = tableEObject.lookupTable(eObject.eContainer());
		if(!(table != null || eObject instanceof BVRModel)){
			for(SymbolTableEObject t : tableEObject.getChildren()){
				table = findParentScope(eObject.eContainer(), t);
				if(table != null) break;
			}
		}
		return table;
	}
	
	

}
