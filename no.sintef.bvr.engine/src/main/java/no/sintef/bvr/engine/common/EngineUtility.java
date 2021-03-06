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
package no.sintef.bvr.engine.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import no.sintef.bvr.common.engine.error.BasicBVREngineException;
import no.sintef.bvr.common.engine.error.GeneralBVREngineException;
import no.sintef.bvr.engine.fragment.impl.PlacementElementHolder;
import no.sintef.bvr.engine.fragment.impl.ReplacementElementHolder;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import bvr.BvrFactory;
import bvr.FromPlacement;
import bvr.ObjectHandle;
import bvr.PlacementFragment;
import bvr.ReplacementFragmentType;
import bvr.ToReplacement;

public class EngineUtility {
	
	private static BvrFactory factory = BvrFactory.eINSTANCE;

	public static EList<EObject> resolveProxies(EList<? extends EObject> proxyList){
		EList<EObject> resolvedList = new BasicEList<EObject>();
		for(EObject proxy : proxyList){
			EObject resolvedProxy = null;
			if(proxy instanceof ObjectHandle){
				resolvedProxy = ((ObjectHandle) proxy).getMOFRef();
			}else{
				resolvedProxy = proxy;
			}
			if(resolvedProxy != null){
				resolvedList.add(resolvedProxy);
			}
		}
		return resolvedList;
	}
	
	public static EObject resolveProxies(EObject proxy){
		EObject resolvedProxy = null;
		if(proxy instanceof ObjectHandle){
			resolvedProxy = ((ObjectHandle) proxy).getMOFRef();
		}else{
			resolvedProxy = proxy;
		}
		return resolvedProxy;
	}
	
	public static EList<ObjectHandle> getPlacementObjectHandlesByEObjects(PlacementFragment placement, EList<EObject> eObjectList) {
		EList<ObjectHandle> objectHandles = new BasicEList<ObjectHandle>();
		EList<ObjectHandle> objectHandlesAll = placement.getSourceObject();
		for(EObject eObject : eObjectList){
			ObjectHandle oh = getObjectHandle(eObject, objectHandlesAll);
			if(oh == null){
				oh = factory.createObjectHandle();
				oh.setMOFRef(eObject);
				placement.getSourceObject().add(oh);
			}
			objectHandles.add(oh);
		}
		return objectHandles;
	}
	
	public static EList<ObjectHandle> getReplacementObjectHandlesByEObjects(ReplacementFragmentType replacement, EList<EObject> eObjectList) {
		EList<ObjectHandle> objectHandles = new BasicEList<ObjectHandle>();
		EList<ObjectHandle> objectHandlesAll = replacement.getSourceObject();
		for(EObject eObject : eObjectList){
			ObjectHandle oh = getObjectHandle(eObject, objectHandlesAll);
			if(oh == null){
				oh = factory.createObjectHandle();
				oh.setMOFRef(eObject);
				replacement.getSourceObject().add(oh);
			}
			objectHandles.add(oh);
		}
		return objectHandles;
	}
		
	public static ObjectHandle getObjectHandle(EObject eObject, EList<ObjectHandle> objectHandles){
		for(ObjectHandle oh : objectHandles){
			EObject eObjectBuf = oh.getMOFRef();
			if((eObjectBuf == null && eObjectBuf == eObject) || (eObjectBuf !=null && eObjectBuf.equals(eObject))){
				return oh;
			}
		}
		return null;
	}
	
	public static EList<EObject> getListPropertyValue(EObject holder, EStructuralFeature property) throws GeneralBVREngineException{
		Object propertyValue = holder.eGet(property);
		if(!(propertyValue instanceof EList)){
			throw new GeneralBVREngineException("property is not the list " + propertyValue);
		}
		@SuppressWarnings("unchecked") EList<EObject> eList = (EList<EObject>) propertyValue;
		return eList;
	}
	
	public static EList<EObject> subtractAugmentList(EList<EObject> elementsOrig, EList<EObject> elementsToRemove, EList<EObject> elementsToAdd){
		EList<EObject> eList = new BasicEList<EObject>(elementsOrig);
		eList.removeAll(elementsToRemove);
		for(EObject eObject : elementsToAdd){
			if(!eList.contains(eObject)){
				eList.add(eObject);
			}
		}
		return eList;
	}
	
	public static <K,V> HashMap<V,K> reverseMap(Map<K,V> map) {
		if(map == null)
			return null;
	    HashMap<V,K> rev = new HashMap<V, K>();
	    for(Map.Entry<K,V> entry : map.entrySet())
	        rev.put(entry.getValue(), entry.getKey());
	    return rev;
	}
	
	public static boolean isDummyToReplacement(ToReplacement toReplacement){
		EList<ObjectHandle> insideBoundaryElements = toReplacement.getInsideBoundaryElement();
		ObjectHandle outsideBoundaryElement = toReplacement.getOutsideBoundaryElement();
		if(insideBoundaryElements.size() == 1 && outsideBoundaryElement.equals(insideBoundaryElements.get(0)) && outsideBoundaryElement.getMOFRef() == null){
			return true;
		}
		return false;
	}

	public static boolean isDummyFromPlacement(FromPlacement fromPlacement) {
		ObjectHandle insideBoundaryElement = fromPlacement.getInsideBoundaryElement();
		EList<ObjectHandle> outsideBoundaryelements = fromPlacement.getOutsideBoundaryElement();
		if(outsideBoundaryelements.size() == 1 && insideBoundaryElement.equals(outsideBoundaryelements.get(0)) && insideBoundaryElement.getMOFRef() == null){
			return true;
		}
		return false;
	}
	
	public static HashSet<EObject> clearSet(HashSet<EObject> set){
		if(set != null && set.contains(null)){
			set.remove(null);
		}
		return set;
	}
	
	public static void setProperty(EList<EObject> original, EList<EObject> toRemove, EList<EObject> toAdd){
		for(EObject eObject : toRemove){
			original.remove(eObject);
		}
		original.addAll(toAdd);
	}
	
	public static final int NOT_CNTND = 0; //a placement is not contained in the replacement
	public static final int CNTND = 1; // a placement is contained in the replacement
	public static final int P_CNTND = 2; // a placement is not fully contained in the replacement
	public static int testPlacementIntersection(ReplacementFragmentType replcnt, PlacementFragment plcnt){
		ReplacementElementHolder replcntHolder;
		PlacementElementHolder plcntHolder;
		try {
			replcntHolder = new ReplacementElementHolder(replcnt);
			plcntHolder = new PlacementElementHolder(plcnt);
		} catch (BasicBVREngineException e) {
			throw new UnsupportedOperationException(e);
		}
		HashSet<EObject> plcntElements = plcntHolder.getElements();
		HashSet<EObject> replntElements = replcntHolder.getElements();
		SetView<EObject> difference = Sets.difference(plcntElements, replntElements);
		if(difference.isEmpty())
			return CNTND;
		difference = Sets.intersection(plcntElements, replntElements);
		if(!difference.isEmpty())
			return P_CNTND;
		return NOT_CNTND;
	}
	
	public static void setProperty(EObject targetEObject, EStructuralFeature feature, Object value){
		targetEObject.eSet(feature, value);
	}
}
