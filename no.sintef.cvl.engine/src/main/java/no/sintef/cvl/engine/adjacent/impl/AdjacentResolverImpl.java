package no.sintef.cvl.engine.adjacent.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import cvl.FromBinding;
import cvl.FromPlacement;
import cvl.ObjectHandle;
import cvl.ToBinding;
import cvl.ToPlacement;

import no.sintef.cvl.engine.adjacent.AdjacentFinder;
import no.sintef.cvl.engine.adjacent.AdjacentFragment;
import no.sintef.cvl.engine.adjacent.AdjacentResolver;
import no.sintef.cvl.engine.common.Utility;
import no.sintef.cvl.engine.error.BasicCVLEngineException;
import no.sintef.cvl.engine.error.GeneralCVLEngineException;
import no.sintef.cvl.engine.error.IllegalCVLOperation;
import no.sintef.cvl.engine.error.IncorrectCVLModel;
import no.sintef.cvl.engine.error.UnexpectedOperationFailure;
import no.sintef.cvl.engine.fragment.FragSubHolder;
import no.sintef.cvl.engine.fragment.impl.FragmentSubstitutionHolder;

public class AdjacentResolverImpl implements AdjacentResolver {

	private AdjacentFinder adjacentFinder;

	public AdjacentResolverImpl(AdjacentFinder finder){
		this.adjacentFinder = finder;
	}
	
	@Override
	public void resolve(FragSubHolder fragmentHolderCurrent) throws BasicCVLEngineException {
		AdjacentFragment aFrag = this.adjacentFinder.getAdjacentMap().get(fragmentHolderCurrent);
		if(aFrag == null){
			return;
		}
		HashSet<AdjacentFragment> adjacentFragments = aFrag.getAdjacentFragments();
		if(adjacentFragments.isEmpty()){
			throw new GeneralCVLEngineException("can not find any adjacent fragments to the fragment that seems to be adjacent" + fragmentHolderCurrent);
		}
		for(AdjacentFragment adjacentFragment : adjacentFragments){
			FragSubHolder fragHolderAdjacent = adjacentFragment.getFragmentHolder();
			HashMap<FromBinding, ToBinding> adjacentBindingsToCurrent = Utility.reverseMap(aFrag.getAdjacentToBindings(adjacentFragment));
			adjacentBindingsToCurrent = (adjacentBindingsToCurrent != null) ? adjacentBindingsToCurrent : new HashMap<FromBinding, ToBinding>();
			for(Map.Entry<FromBinding, ToBinding> entry : adjacentBindingsToCurrent.entrySet()){
				FromBinding fromBinding = entry.getKey();
				ToBinding toBinding = entry.getValue();
				EList<ObjectHandle> outsideBOHElmtsPlc = fromBinding.getFromPlacement().getOutsideBoundaryElement();
				EList<ObjectHandle> insideBOHElmtsPlcReplaced = toBinding.getToPlacement().getInsideBoundaryElement();
				
				HashMap<FromPlacement, HashSet<ObjectHandle>> insideBoundaryElementsFromPlacementMap = ((FragmentSubstitutionHolder) fragHolderAdjacent).getFromPlacementInsideBoundaryElementMap();
				HashSet<ObjectHandle> insideBoundaryElementsFromPlacement = insideBoundaryElementsFromPlacementMap.get(fromBinding.getFromPlacement());
				if(insideBoundaryElementsFromPlacement == null){
					throw new GeneralCVLEngineException("failed to find insideBoundaryElements in the map for a given fromPlacement");
				}
				for(ObjectHandle objectHandle : insideBoundaryElementsFromPlacement){
					EObject insideBoundaryElementPlc = Utility.resolveProxies(objectHandle);
					String propertyName = fromBinding.getFromReplacement().getPropertyName();
					EStructuralFeature property = insideBoundaryElementPlc.eClass().getEStructuralFeature(propertyName);
					if(property == null){
						throw new GeneralCVLEngineException("failed to find property to adjust, property name : " + propertyName);
					}
					int upperBound = property.getUpperBound();
					if(upperBound == -1 || upperBound > 1){
						EList<EObject> values = Utility.getListPropertyValue(insideBoundaryElementPlc, property);
						EList<EObject> propertyValueNew = Utility.subtractAugmentList(values, Utility.resolveProxies(outsideBOHElmtsPlc), Utility.resolveProxies(insideBOHElmtsPlcReplaced));
						if(upperBound != -1 && propertyValueNew.size() > upperBound){
							throw new IllegalCVLOperation("cardinality does not correspond for property : " + propertyName + "of" + fragHolderAdjacent.getFragment());
						}
						insideBoundaryElementPlc.eSet(property, propertyValueNew);
						EList<EObject> propertyValueSet = Utility.getListPropertyValue(insideBoundaryElementPlc, property);
						if(!propertyValueNew.equals(propertyValueSet)){
							throw new UnexpectedOperationFailure("EPIC FAIL: property has not been adjusted : " + propertyName + "of" + fragHolderAdjacent.getFragment());
						}
					}else{
						//property.getUpperBound() == 0 || == 1
						if(upperBound == 0){
							throw new IncorrectCVLModel("model is incorrect, cardianlity for reference is set to 0, but something is there" + insideBoundaryElementPlc.eGet(property));
						}
						if(insideBOHElmtsPlcReplaced.size() != upperBound){
							throw new IllegalCVLOperation("cardinality does not match for property :" + propertyName + "of" + fragHolderAdjacent.getFragment());
						}

						EObject propertyValueNew = Utility.resolveProxies(insideBOHElmtsPlcReplaced).get(0);
						insideBoundaryElementPlc.eSet(property, propertyValueNew);
						Object propertyValueSet = insideBoundaryElementPlc.eGet(property);
						if(!propertyValueNew.equals(propertyValueSet)){
							throw new UnexpectedOperationFailure("EPIC FAIL: property has not been adjusted : " + propertyName + "of" + fragHolderAdjacent.getFragment());
						}						
					}
				}
				
				//update variability model : boundaries so the point to the correct elements
				fromBinding.getFromPlacement().getOutsideBoundaryElement().clear();
				fromBinding.getFromPlacement().getOutsideBoundaryElement().addAll(insideBOHElmtsPlcReplaced);
				
				HashMap<ToPlacement, HashSet<ObjectHandle>> outsideBoundaryElementsToPlacementMap = ((FragmentSubstitutionHolder) fragmentHolderCurrent).getToPlacementOutsideBoundaryElementMap();
				HashSet<ObjectHandle> outsideBoundaryElementsToPlacement = outsideBoundaryElementsToPlacementMap.get(toBinding.getToPlacement());
				outsideBoundaryElementsToPlacement.clear();
				outsideBoundaryElementsToPlacement.addAll(insideBoundaryElementsFromPlacement);
				outsideBoundaryElementsToPlacementMap.put(toBinding.getToPlacement(), outsideBoundaryElementsToPlacement);
				((FragmentSubstitutionHolder) fragmentHolderCurrent).setToPlacementOutsideBoundaryElementMap(outsideBoundaryElementsToPlacementMap);
			}
			
			HashMap<FromBinding, ToBinding> adjacentBindingsFromCurrent = aFrag.getAdjacentFromBindings(adjacentFragment);
			adjacentBindingsFromCurrent = (adjacentBindingsFromCurrent != null) ? adjacentBindingsFromCurrent : new HashMap<FromBinding, ToBinding>();
			for(Map.Entry<FromBinding, ToBinding> entry : adjacentBindingsFromCurrent.entrySet()){
				FromBinding fromBinding = entry.getKey();
				ToBinding toBinding = entry.getValue();
				
				ObjectHandle insideBOHElmtsPlcReplaced = fromBinding.getFromPlacement().getInsideBoundaryElement();
				toBinding.getToPlacement().setOutsideBoundaryElement(insideBOHElmtsPlcReplaced);
				
				//update boundary toPlacement boundary map
				HashMap<FromPlacement, HashSet<ObjectHandle>> insideBoundaryElementsFromPlacementMap = ((FragmentSubstitutionHolder) fragmentHolderCurrent).getFromPlacementInsideBoundaryElementMap();
				HashSet<ObjectHandle> insideBoundaryElementsFromPlacement = insideBoundaryElementsFromPlacementMap.get(fromBinding.getFromPlacement());
				HashMap<ToPlacement, HashSet<ObjectHandle>> outsideBoundaryElementsToPlacementMap = ((FragmentSubstitutionHolder) fragHolderAdjacent).getToPlacementOutsideBoundaryElementMap();
				HashSet<ObjectHandle> outsideBoundaryElementsToPlacement = outsideBoundaryElementsToPlacementMap.get(toBinding.getToPlacement());
				outsideBoundaryElementsToPlacement.clear();
				outsideBoundaryElementsToPlacement.addAll(insideBoundaryElementsFromPlacement);
				outsideBoundaryElementsToPlacementMap.put(toBinding.getToPlacement(), outsideBoundaryElementsToPlacement);
				((FragmentSubstitutionHolder) fragHolderAdjacent).setToPlacementOutsideBoundaryElementMap(outsideBoundaryElementsToPlacementMap);
			}
			((FragmentSubstitutionHolder) fragHolderAdjacent).refresh();
		}
		((FragmentSubstitutionHolder) fragmentHolderCurrent).refresh();
	}
}