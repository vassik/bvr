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
package no.sintef.bvr.engine.containment;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.Sets;

import bvr.FromPlacement;
import bvr.FromReplacement;
import bvr.ObjectHandle;
import bvr.PlacementFragment;
import bvr.ReplacementFragmentType;
import bvr.ToPlacement;
import bvr.ToReplacement;
import no.sintef.bvr.engine.common.EngineUtility;
import no.sintef.bvr.engine.fragment.impl.FragmentSubstitutionHolder;


public class ReplacPlacCotainmentResolver {

	private ReplacPlacCotainmentFinder finder;

	public ReplacPlacCotainmentResolver(ReplacPlacCotainmentFinder intersectionFinder){
		finder = intersectionFinder;
	}
	
	public void resolve(FragmentSubstitutionHolder fragment){
		adjustBoundaries(fragment);
	}
	
	private void adjustBoundaries(FragmentSubstitutionHolder fragmentHolder){
		PlacementFragment placement = fragmentHolder.getPlacement().getPlacementFragment();
		HashMap<ReplacementFragmentType, HashMap<ToPlacement, HashSet<ToReplacement>>> replacementToBoundary = finder.getAdjacentToBoundaries().get(placement);
		if(replacementToBoundary != null){
			HashSet<EObject> placementStaleElements = finder.getPlacementStaleElements().get(placement);
			EList<EObject> invalidPlacementElements = new BasicEList<EObject>(Sets.difference(placementStaleElements, fragmentHolder.getPlacement().getElements()));
			for(Map.Entry<ReplacementFragmentType, HashMap<ToPlacement, HashSet<ToReplacement>>> entry : replacementToBoundary.entrySet()){
				HashMap<ToPlacement, HashSet<ToReplacement>> boundarysMap = entry.getValue();
				for(Map.Entry<ToPlacement, HashSet<ToReplacement>> boundaryMap : boundarysMap.entrySet()){
					ToPlacement toPlacement = boundaryMap.getKey();
					HashSet<ToReplacement> toReplacements = boundaryMap.getValue();
					for(ToReplacement toReplacement : toReplacements){
						removeInvalidReferences(toReplacement, invalidPlacementElements);
						adjustInsideBoundaryRefereneces(toReplacement, toPlacement, placement);
					}
				}
			}
		}
		
		HashMap<ReplacementFragmentType, HashMap<FromPlacement, HashSet<FromReplacement>>> replacementFromBoundary = finder.getAdjacentFromBoundaries().get(placement);
		if(replacementFromBoundary != null){
			for(Map.Entry<ReplacementFragmentType, HashMap<FromPlacement, HashSet<FromReplacement>>> entry : replacementFromBoundary.entrySet()){
				HashMap<FromPlacement, HashSet<FromReplacement>> boundarysMap = entry.getValue();
				for(Map.Entry<FromPlacement, HashSet<FromReplacement>> boundaryMap : boundarysMap.entrySet()){
					FromPlacement fromPlacement = boundaryMap.getKey();
					HashSet<FromReplacement> fromReplacements = boundaryMap.getValue();
					EObject insideElementPlac = fromPlacement.getInsideBoundaryElement().getMOFRef();
					for(FromReplacement fromReplacement : fromReplacements){
						EObject insideElementRepl = fromReplacement.getInsideBoundaryElement().getMOFRef();
						if(!insideElementPlac.equals(insideElementRepl)){
							ObjectHandle objectHandle = no.sintef.bvr.common.CommonUtility.testObjectHandle(entry.getKey(), insideElementPlac);
							fromReplacement.setInsideBoundaryElement(objectHandle);
						}
					}
				}
			}
		}
	}
	
	
	private void adjustInsideBoundaryRefereneces(ToReplacement toReplacement, ToPlacement toPlacement, PlacementFragment placement){
		EList<EObject> insideEObjectsRepl = EngineUtility.resolveProxies(toReplacement.getInsideBoundaryElement());
		EList<EObject> insideEObjectsPlc = EngineUtility.resolveProxies(toPlacement.getInsideBoundaryElement());
		for(EObject eObject : insideEObjectsPlc){
			if(insideEObjectsRepl.indexOf(eObject) < 0){
				ObjectHandle objectHandle = no.sintef.bvr.common.CommonUtility.testObjectHandle(placement, eObject);
				toReplacement.getInsideBoundaryElement().add(objectHandle);
			}
		}
	}
	
	private void removeInvalidReferences(ToReplacement toReplacement, EList<EObject> invalidEObjects){
		if(invalidEObjects.isEmpty())
			return;
		EList<ObjectHandle> objectHandles = toReplacement.getInsideBoundaryElement();
		Iterator<ObjectHandle> iterator = objectHandles.iterator();
		while(iterator.hasNext()){
			ObjectHandle objectHandle = iterator.next();
			EObject eObject = objectHandle.getMOFRef();
			if(invalidEObjects.indexOf(eObject) >= 0){
				iterator.remove();
			}
		}
	}
}
