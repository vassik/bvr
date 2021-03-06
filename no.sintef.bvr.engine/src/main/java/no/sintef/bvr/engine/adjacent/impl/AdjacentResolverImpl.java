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
package no.sintef.bvr.engine.adjacent.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import bvr.FromBinding;
import bvr.FromPlacement;
import bvr.ObjectHandle;
import bvr.ToBinding;
import bvr.ToPlacement;
import no.sintef.bvr.common.engine.error.BasicBVREngineException;
import no.sintef.bvr.common.engine.error.GeneralBVREngineException;
import no.sintef.bvr.common.engine.error.IllegalBVROperation;
import no.sintef.bvr.common.engine.error.IncorrectBVRModel;
import no.sintef.bvr.common.engine.error.UnexpectedOperationFailure;
import no.sintef.bvr.engine.common.EngineUtility;
import no.sintef.bvr.engine.fragment.impl.FragmentSubstitutionHolder;
import no.sintef.bvr.engine.interfaces.adjacent.IAdjacentFinder;
import no.sintef.bvr.engine.interfaces.adjacent.IAdjacentFragment;
import no.sintef.bvr.engine.interfaces.adjacent.IAdjacentResolver;
import no.sintef.bvr.engine.interfaces.fragment.IFragSubHolder;

public class AdjacentResolverImpl implements IAdjacentResolver {

	private IAdjacentFinder adjacentFinder;

	public AdjacentResolverImpl(IAdjacentFinder finder){
		this.adjacentFinder = finder;
	}
	
	@Override
	public void resolve(IFragSubHolder fragmentHolderCurrent) throws BasicBVREngineException {
		IAdjacentFragment aFrag = this.adjacentFinder.getAdjacentMap().get(fragmentHolderCurrent);
		if(aFrag == null)
			return;
		
		HashSet<IAdjacentFragment> adjacentFragments = aFrag.getAdjacentFragments();
		if(adjacentFragments.isEmpty())
			throw new GeneralBVREngineException("can not find any adjacent fragments to the fragment that seems to be adjacent" + fragmentHolderCurrent);

		HashSet<IAdjacentFragment> twinAFrag = aFrag.getTwinFragments();		
		for(IAdjacentFragment adjacentFragment : adjacentFragments){
			IFragSubHolder fragHolderAdjacent = adjacentFragment.getFragmentHolder();
			HashMap<FromBinding, ToBinding> adjacentBindingsToCurrent = EngineUtility.reverseMap(aFrag.getAdjacentToBindings(adjacentFragment));
			adjacentBindingsToCurrent = (adjacentBindingsToCurrent != null) ? adjacentBindingsToCurrent : new HashMap<FromBinding, ToBinding>();
			for(Map.Entry<FromBinding, ToBinding> entry : adjacentBindingsToCurrent.entrySet()){
				FromBinding fromBinding = entry.getKey();
				ToBinding toBinding = entry.getValue();

				EList<ObjectHandle> outsideBOHElmtsPlc = fromBinding.getFromPlacement().getOutsideBoundaryElement();
				EList<ObjectHandle> insideBOHElmtsPlcReplaced = calculateInsideBoundaryElements(twinAFrag, adjacentFragment, fromBinding, toBinding);
				
				HashMap<FromPlacement, HashSet<ObjectHandle>> insideBoundaryElementsFromPlacementMap = ((FragmentSubstitutionHolder) fragHolderAdjacent).getFromPlacementInsideBoundaryElementMap();
				HashSet<ObjectHandle> insideBoundaryElementsFromPlacement = insideBoundaryElementsFromPlacementMap.get(fromBinding.getFromPlacement());
				if(insideBoundaryElementsFromPlacement == null)
					throw new GeneralBVREngineException("failed to find insideBoundaryElements in the map for a given fromPlacement " + fromBinding.getFromPlacement() + " of the fromBinding " + fromBinding);

				HashSet<ObjectHandle> validOutsideOHFromPlacementBElements = new HashSet<ObjectHandle>();
				
				for(ObjectHandle objectHandle : insideBoundaryElementsFromPlacement){
					EObject insideBoundaryElementPlc = EngineUtility.resolveProxies(objectHandle);
					String propertyName = fromBinding.getFromReplacement().getPropertyName();
					EStructuralFeature property = insideBoundaryElementPlc.eClass().getEStructuralFeature(propertyName);
					if(property == null)
						throw new GeneralBVREngineException("failed to find property to adjust, property name : " + propertyName);

					validOutsideOHFromPlacementBElements.clear();
					validOutsideOHFromPlacementBElements.addAll(insideBOHElmtsPlcReplaced);
					
					int upperBound = property.getUpperBound();
					if(upperBound == -1 || upperBound > 1){
						EList<EObject> values = EngineUtility.getListPropertyValue(insideBoundaryElementPlc, property);
						
						SetView<EObject> invalidOutsideBEFromPlacement = Sets.difference(
								new HashSet<EObject>(EngineUtility.resolveProxies(outsideBOHElmtsPlc)),
								new HashSet<EObject>(values));
						EList<EObject> elementsToRemove = new BasicEList<EObject>(invalidOutsideBEFromPlacement);
						
						EList<ObjectHandle> invalidObjectHandles = findObjectHandlesByEObjects(new BasicEList<EObject>(invalidOutsideBEFromPlacement), outsideBOHElmtsPlc);
						validOutsideOHFromPlacementBElements.addAll(Sets.difference(new HashSet<ObjectHandle>(outsideBOHElmtsPlc), new HashSet<ObjectHandle>(invalidObjectHandles)));
						
						EList<EObject> elementsToAdd = EngineUtility.resolveProxies(insideBOHElmtsPlcReplaced);
						EList<EObject> propertyValueNew = EngineUtility.subtractAugmentList(values, elementsToRemove, elementsToAdd);
						
						if(upperBound != -1 && propertyValueNew.size() > upperBound)
							throw new IllegalBVROperation("cardinality does not correspond for property : " + propertyName + "of" + fragHolderAdjacent.getFragment());

						EngineUtility.setProperty(values, elementsToRemove, elementsToAdd);
						
						EList<EObject> propertyValueSet = EngineUtility.getListPropertyValue(insideBoundaryElementPlc, property);
						if(!propertyValueNew.equals(propertyValueSet))
							throw new UnexpectedOperationFailure("EPIC FAIL: property has not been adjusted : '" + propertyName + "' of " + fragHolderAdjacent.getFragment());
					}else{
						//property.getUpperBound() == 0 || == 1
						if(upperBound == 0)
							throw new IncorrectBVRModel("model is incorrect, cardianlity for reference is set to 0, but something is there " + insideBoundaryElementPlc.eGet(property));

						if(insideBOHElmtsPlcReplaced.size() != upperBound)
							throw new IllegalBVROperation("cardinality does not match for property : '" + propertyName + "' of " + fragHolderAdjacent.getFragment() + " objects: " + EngineUtility.resolveProxies(insideBOHElmtsPlcReplaced));

						EObject propertyValueNew = EngineUtility.resolveProxies(insideBOHElmtsPlcReplaced).get(0);
						EngineUtility.setProperty(insideBoundaryElementPlc, property, propertyValueNew);
						
						Object propertyValueSet = insideBoundaryElementPlc.eGet(property);
						if(!propertyValueNew.equals(propertyValueSet))
							throw new UnexpectedOperationFailure("EPIC FAIL: property has not been adjusted : " + propertyName + "of" + fragHolderAdjacent.getFragment());					
					}
				}
				
				//update variability model : boundaries so the point to the correct elements
				fromBinding.getFromPlacement().getOutsideBoundaryElement().clear();
				fromBinding.getFromPlacement().getOutsideBoundaryElement().addAll(validOutsideOHFromPlacementBElements);
								
				HashMap<ToPlacement, HashSet<ObjectHandle>> outsideBoundaryElementsToPlacementMap = ((FragmentSubstitutionHolder) fragmentHolderCurrent).getToPlacementOutsideBoundaryElementMap();
				HashSet<ObjectHandle> outsideBoundaryElementsToPlacement = outsideBoundaryElementsToPlacementMap.get(toBinding.getToPlacement());
				outsideBoundaryElementsToPlacement.clear();
				outsideBoundaryElementsToPlacement.addAll(insideBoundaryElementsFromPlacement);
			}
			
			HashMap<FromBinding, ToBinding> adjacentBindingsFromCurrent = aFrag.getAdjacentFromBindings(adjacentFragment);
			adjacentBindingsFromCurrent = (adjacentBindingsFromCurrent != null) ? adjacentBindingsFromCurrent : new HashMap<FromBinding, ToBinding>();
			for(Map.Entry<FromBinding, ToBinding> entry : adjacentBindingsFromCurrent.entrySet()){
				FromBinding fromBinding = entry.getKey();
				ToBinding toBinding = entry.getValue();
				
				ObjectHandle insideBOHElmtsPlcReplaced = fromBinding.getFromPlacement().getInsideBoundaryElement();
				toBinding.getToPlacement().setOutsideBoundaryElement(insideBOHElmtsPlcReplaced);
								
				HashSet<ObjectHandle> insideBoundaryElementsFromPlacement = calculateOutsideBoundaryElementsToPlacementAdjacentToCurrent(aFrag, fromBinding.getFromPlacement());
				HashMap<ToPlacement, HashSet<ObjectHandle>> outsideBoundaryElementsToPlacementMap = ((FragmentSubstitutionHolder) fragHolderAdjacent).getToPlacementOutsideBoundaryElementMap();
				HashSet<ObjectHandle> outsideBoundaryElementsToPlacement = outsideBoundaryElementsToPlacementMap.get(toBinding.getToPlacement());
				outsideBoundaryElementsToPlacement.clear();
				outsideBoundaryElementsToPlacement.addAll(insideBoundaryElementsFromPlacement);
			}
			((FragmentSubstitutionHolder) fragHolderAdjacent).refresh();
		}
		((FragmentSubstitutionHolder) fragmentHolderCurrent).refresh();
	}
	
	private EList<ObjectHandle> findObjectHandlesByEObjects(EList<EObject> eObjects, EList<ObjectHandle> objectHandles){
		HashSet<ObjectHandle> objHandles = new HashSet<ObjectHandle>();
		for(EObject eObject : eObjects){
			for(ObjectHandle objectHandle : objectHandles){
				if(objectHandle.getMOFRef() == eObject){
					objHandles.add(objectHandle);
				}
			}
		}
		return new BasicEList<ObjectHandle>(objHandles); 
	}
	
	private HashSet<ObjectHandle> calculateOutsideBoundaryElementsToPlacementAdjacentToCurrent(
			IAdjacentFragment adjacentCurrent,
			FromPlacement fromPlacementAdjacentCurrent)
	{
		HashSet<ObjectHandle> insideBoundaryElements = new HashSet<ObjectHandle>();
		FragmentSubstitutionHolder fragmentHolderCurrent = (FragmentSubstitutionHolder) adjacentCurrent.getFragmentHolder();
		HashMap<FromPlacement, HashSet<ObjectHandle>> insideBoundaryElementsFromPlacementMap = fragmentHolderCurrent.getFromPlacementInsideBoundaryElementMap();
		HashSet<IAdjacentFragment> twins = adjacentCurrent.getTwinFragments();
		if(twins.isEmpty()){
			insideBoundaryElements.addAll(insideBoundaryElementsFromPlacementMap.get(fromPlacementAdjacentCurrent));
		}else{
			Iterator<IAdjacentFragment> iterator = twins.iterator();
			while(iterator.hasNext()){
				IAdjacentFragment twin = iterator.next();				
				HashMap<FromPlacement, FromPlacement> fromPlacementMap = adjacentCurrent.getTwinFromPlacement(twin);
				if(fromPlacementMap == null){
					throw new UnexpectedOperationFailure("twin fromPlacement boundary map is null");
				}
				FromPlacement fromPlacementTwin = fromPlacementMap.get(fromPlacementAdjacentCurrent);
				if(fromPlacementTwin == null){
					throw new UnexpectedOperationFailure("can not find a twin fromPlacement boundary");
				}
				FragmentSubstitutionHolder fragmentHolderTwin = (FragmentSubstitutionHolder) twin.getFragmentHolder();
				HashMap<FromPlacement, HashSet<ObjectHandle>> insideBoundaryElementsFromPlacementMapTwin = fragmentHolderTwin.getFromPlacementInsideBoundaryElementMap();
				
				insideBoundaryElements.addAll(insideBoundaryElementsFromPlacementMap.get(fromPlacementAdjacentCurrent));
				insideBoundaryElements.addAll(insideBoundaryElementsFromPlacementMapTwin.get(fromPlacementTwin));
			}
			
		}
		return insideBoundaryElements;
	}
	
	private EList<ObjectHandle> calculateInsideBoundaryElements(
			HashSet<IAdjacentFragment> twins,
			IAdjacentFragment adjacentFragment,
			FromBinding fromBindingAdjacent,
			ToBinding toBindingCurrent)
	{
		HashSet<ObjectHandle> insideBEObjectHandles = new HashSet<ObjectHandle>();
		insideBEObjectHandles.addAll(toBindingCurrent.getToPlacement().getInsideBoundaryElement());
		for(IAdjacentFragment twin : twins){
			HashMap<FromBinding, ToBinding> adjacentBindingsToCurrent =
					EngineUtility.reverseMap(twin.getAdjacentToBindings(adjacentFragment));
			if(adjacentBindingsToCurrent == null)
				throw new UnexpectedOperationFailure("a twin must have an adjacent bindings with the given adjacent fragment");
			ToBinding toBindingTwin = adjacentBindingsToCurrent.get(fromBindingAdjacent);
			if(toBindingTwin == null)
				throw new UnexpectedOperationFailure("twin must have an adjacent binding of the same kind");
			EList<ObjectHandle> insideBEObjectHandlesTwin = toBindingTwin.getToPlacement().getInsideBoundaryElement();
			insideBEObjectHandles.addAll(insideBEObjectHandlesTwin);
		}
		return new BasicEList<ObjectHandle>(insideBEObjectHandles);
	}
}
