package no.sintef.bvr.engine.common;

import java.util.HashMap;

import no.sintef.bvr.common.logging.Logger;
import no.sintef.bvr.engine.adjacent.AdjacentFinder;
import no.sintef.bvr.engine.adjacent.AdjacentResolver;
import no.sintef.bvr.engine.adjacent.impl.AdjacentFinderImpl;
import no.sintef.bvr.engine.adjacent.impl.AdjacentResolverImpl;
import no.sintef.bvr.engine.containment.ReplacPlacCotainmentFinder;
import no.sintef.bvr.engine.containment.ReplacPlacCotainmentResolver;
import no.sintef.bvr.engine.crossing.PlacementCrossingFinder;
import no.sintef.bvr.engine.error.BasicBVREngineException;
import no.sintef.bvr.engine.error.ContainmentBVRModelException;
import no.sintef.bvr.engine.fragment.impl.FragmentSubstitutionHolder;
import no.sintef.bvr.engine.operation.impl.FragmentSubOperation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import bvr.FragmentSubstitution;
import bvr.ObjectHandle;


public final class SubstitutionEngine {

	public static final SubstitutionEngine eINSTANCE = getEngine();
	
	private final SubstitutionContext context = SubstitutionContext.ME;
	
	private HashMap<FragmentSubstitution, FragmentSubstitutionHolder> fsMap;
	private AdjacentFinder adjacentFinder;
	private AdjacentResolver adjacentResolver;
	private ReplacPlacCotainmentFinder placementInReplacementFinder;
	private ReplacPlacCotainmentResolver placementInReplacementResolver;
	private PlacementCrossingFinder placementCrossingFinder;
	

	private static SubstitutionEngine getEngine() {
		return new SubstitutionEngine();
	}
	
	public void setLogger(Logger logger){
		context.setLogger(logger);
	}
	
	public void init(EList<FragmentSubstitution> fragmentSubstitutions){
		fsMap = new HashMap<FragmentSubstitution, FragmentSubstitutionHolder>();
		try{
			for(FragmentSubstitution fragment : fragmentSubstitutions)
				fsMap.put(fragment, new FragmentSubstitutionHolder(fragment));
			
			//NOTE: the call of computeCopyBaseModel should be exactly here,
			//since FragmentSubstitutionHolder actually loads all resources as a side effect
			//which we lately use to define 'base model'
			computeCopyBaseModel();
			
			placementCrossingFinder = new PlacementCrossingFinder(fsMap.values());
			
			adjacentFinder = new AdjacentFinderImpl(new BasicEList<FragmentSubstitutionHolder>(fsMap.values()));
			adjacentResolver = new AdjacentResolverImpl(adjacentFinder);
			
			placementInReplacementFinder = new ReplacPlacCotainmentFinder(fsMap.values());
			placementInReplacementResolver = new ReplacPlacCotainmentResolver(placementInReplacementFinder);
		} catch (BasicBVREngineException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public void subsitute(FragmentSubstitution fragmentSubstitution, boolean replace) throws ContainmentBVRModelException{
		FragmentSubstitutionHolder fragmentHolder = fsMap.get(fragmentSubstitution);
		if(fragmentHolder == null){
			context.getLogger().warn("engine is not initialized with this fragment substitution " + fragmentSubstitution);
			return;
		}
		FragmentSubOperation subsOperation = new FragmentSubOperation(fragmentHolder);
		try {
			subsOperation.execute(replace);
			adjacentResolver.resolve(fragmentHolder);
			placementInReplacementResolver.resolve(fragmentHolder);
		} catch (BasicBVREngineException e) {
			throw new UnsupportedOperationException(e);
		}
		//this check seems to be invalid here since we may work with copies of base and library models
		//which are not contained by any resource in the first place
		//subsOperation.checkConsistence();
	}
	
	
	public HashMap<Resource, ResourceContentCopier> getCopiedBaseModels(){
		return context.getCopyBaseModelMap();
	}
	
	private void computeCopyBaseModel(){
		EList<Resource> baseResources = new BasicEList<Resource>();
		EList<FragmentSubstitution> fragments = new BasicEList<FragmentSubstitution>(fsMap.keySet());
		for(FragmentSubstitution fragment : fragments){
			EList<ObjectHandle> objectHandles = fragment.getPlacement().getSourceObject();
			EList<EObject> eObjects = EngineUtility.resolveProxies(objectHandles);
			for(EObject eObject : eObjects){
				if(eObject != null && eObject.eResource() != null && baseResources.indexOf(eObject.eResource()) < 0){
					baseResources.add(eObject.eResource());
				}
			}
		}
		context.setBaseModel(baseResources);
		
		HashMap<Resource, ResourceContentCopier> copyMap = new HashMap<Resource, ResourceContentCopier>();
		for(Resource resource : baseResources){
			ResourceContentCopier copier = new ResourceContentCopier();
			copier.copyResource(resource);
			copyMap.put(resource, copier);
		}
		context.setCopyBaseModelMap(copyMap);
	}
}