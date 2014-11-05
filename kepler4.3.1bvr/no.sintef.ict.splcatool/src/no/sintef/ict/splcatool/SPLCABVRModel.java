package no.sintef.ict.splcatool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.sintef.bvr.common.CommonUtility;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.prop4j.Node;
import org.prop4j.NodeReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bvr.BCLConstraint;
import bvr.Choice;
import bvr.ChoiceResolution;
import bvr.CompoundResolution;
import bvr.MultiplicityInterval;
import bvr.NegResolution;
import bvr.OpaqueConstraint;
import bvr.PosResolution;
import bvr.VNode;
import bvr.VSpec;
import bvr.VSpecResolution;
import bvr.BvrFactory;
import bvr.BvrPackage;
import bvr.Constraint;
import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

public class SPLCABVRModel {
	protected bvr.BVRModel model;
	
	protected final static String utf8Encoding = "UTF-8"; 

	public SPLCABVRModel(){
		BvrPackage.eINSTANCE.eClass();
		BvrFactory factory = BvrFactory.eINSTANCE;
		model = factory.createBVRModel();
		model.setName("BVR Model 1");
	}

	public SPLCABVRModel(File f) {
		model = loadFromFile(f);
	}
	
	public SPLCABVRModel(String bvrFileName, boolean isPlatform) {
		model = (!isPlatform) ?  loadFromFile(new File(bvrFileName)) : loadFromPlatformFile(bvrFileName);
	}

	public SPLCABVRModel(bvr.BVRModel model) {
		this.model = model;
	}

	public SPLCABVRModel(String bvrfile) {
		this(new File(bvrfile));
	}
	
	private bvr.BVRModel loadFromFile(File file){
		BvrPackage.eINSTANCE.eClass();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(file.getAbsolutePath()), true);
		return (bvr.BVRModel) resource.getContents().get(0);
	}
	
	private bvr.BVRModel loadFromPlatformFile(String bvrFileName){
		BvrPackage.eINSTANCE.eClass();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		URI uri = URI.createPlatformResourceURI(bvrFileName, true);
		Resource resource = resSet.getResource(uri, true);
		return (bvr.BVRModel) resource.getContents().get(0);
	}
	
	public void writeToPlatformFile(String filename) throws IOException {
		//filename should be of in the form /<project>/<folders0..N>/filename
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	    ResourceSet resSet = new ResourceSetImpl();
	    URI uri = URI.createPlatformResourceURI(filename, true);
	    Resource resource = resSet.createResource(uri);
	    resource.getContents().add(model);
	    
	    Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMIResource.OPTION_ENCODING, utf8Encoding);
	    resource.save(options);
	}

	public void writeToFile(String filename) throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	    ResourceSet resSet = new ResourceSetImpl();
	    URI uri = URI.createFileURI(filename);
	    Resource resource = resSet.createResource(uri);
	    resource.getContents().add(model);
	    
	    Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMIResource.OPTION_ENCODING, utf8Encoding);
	    resource.save(options);
	}

	public bvr.BVRModel getRootBVRModel() {
		return model;
	}

	public GUIDSL getGUIDSL() throws IOException, UnsupportedModelException {
		FeatureModel fm = new FeatureModel();
		Feature root = recursiveConvert(fm, (Choice) model.getVariabilityModel()); // This is an assumption
		fm.setRoot(root);
		
		// Add constraints
		//System.out.println(fm.getFeatureNames());
		for(Constraint c : model.getVariabilityModel().getOwnedConstraint()){
			if(c instanceof OpaqueConstraint){
				OpaqueConstraint oc = (OpaqueConstraint)c;
				NodeReader nr = new NodeReader();
				Node n = nr.stringToNode(oc.getConstraint(), new ArrayList<String>(fm.getFeatureNames()));
				fm.addPropositionalNode(n);
				//System.out.println(n.toString(NodeWriter.textualSymbols));
				//System.out.println(oc.getConstraint() + " became " + NodeWriter.nodeToString(n));
			}else if(c instanceof BCLConstraint){
				BCLConstraint bc = (BCLConstraint)c;
				String s = new BCLPrettyPrinter().prettyPrint(bc.getExpression().get(0), model); // This is an assumption
				System.out.println(s);
				NodeReader nr = new NodeReader();
				Node n = nr.stringToNode(s, new ArrayList<String>(fm.getFeatureNames()));
				fm.addPropositionalNode(n);
			}else{
				throw new UnsupportedOperationException("Cannot read constraints from " + c);
			}
		}
		
		// Store
		GUIDSL gd = new GUIDSL(fm);
		
//		gd.writeToFile("temp.m");
		//String s = new FileUtility().readFileAsString("temp.m");
		//System.out.println(".m file: {\n" + s + "\n}");
		
//		gd = new GUIDSL("temp.m");
//		System.out.println(fm.getFeatureNames());
	
		return gd;
	}

	private Feature recursiveConvert(FeatureModel fm, Choice vs) throws UnsupportedModelException {
		Feature f = new Feature(fm);
		f.setName(vs.getName());
		fm.addFeature(f);
		
		// Add children
		for(VNode vc : vs.getMember()){
			Choice c = (Choice)vc;
			Feature fc = recursiveConvert(fm, c);
			fc.setMandatory(c.isIsImpliedByParent());
			f.addChild(fc);
		}
		
		// Set group
		if(vs.getGroupMultiplicity() != null){
			MultiplicityInterval gm = vs.getGroupMultiplicity();
			if(gm.getLower()==1 && gm.getUpper()==1){
				f.setAlternative();
			}else if(gm.getLower()==1 && gm.getUpper()==-1){
				f.setOr();
			}else{
				throw new UnsupportedModelException("Group multiplicity not supported: [" + gm.getLower() + ".." + gm.getUpper() + "]", 0);
			}
		}else{
			f.setAnd();
		}
		
		return f;
	}
	
	public void injectConfigurations(GraphMLFM gfm){
		EList<VSpecResolution> resolutions  = getChoiceResolutions(gfm);
		for(VSpecResolution resolution : resolutions)
			model.getResolutionModels().add((CompoundResolution) resolution);
	}

	public EList<VSpecResolution> getChoiceResolutions(GraphMLFM gfm) {
		Element e = gfm.graph;
		
		//System.out.println(e);
		
		List<Map<String, Boolean>> confs = new ArrayList<Map<String, Boolean>>();
		
		for(int i = 0; i < e.getChildNodes().getLength(); i++){
			org.w3c.dom.Node x = e.getChildNodes().item(i);
			if(!x.getNodeName().equals("node")) continue;
			String id = x.getAttributes().getNamedItem("id").getTextContent();
			String label = getLabel(e, id);
			
			String fname = label.split("=")[0];
			boolean fa = new Boolean(label.split("=")[1]);
			int nr = new Integer(id.substring(id.lastIndexOf('_')+1, id.length()));
			
			// done
			//System.out.println(fname + "["+nr+"]" + " = " + fa);
			
			// add
			while(confs.size()<=nr)
				confs.add(new HashMap<String, Boolean>());
			
			confs.get(nr).put(fname, fa);
		}
		
		EList<VSpecResolution> resolutions = new BasicEList<VSpecResolution>();
		for(Map<String, Boolean> conf : confs){
			ChoiceResolution cr = recursivelyResolve(conf, (Choice)model.getVariabilityModel());
			resolutions.add(cr);
		}
		return resolutions;
	}
	
	private ChoiceResolution recursivelyResolve(Map<String, Boolean> conf, Choice choice) {
		// Add node
		ChoiceResolution cr;
		if(conf.get(choice.getName())){
			cr = BvrFactory.eINSTANCE.createPosResolution();
		}else {
			cr = BvrFactory.eINSTANCE.createNegResolution();
		}
		CommonUtility.setResolved(cr, choice);
		
		// Add children
		for(VNode x : choice.getMember()){
			ChoiceResolution crc = recursivelyResolve(conf, (Choice) x);
			if(cr instanceof PosResolution){
				((PosResolution) cr).getMembers().add(crc);
			}
		}
		
		// Done
		return cr;
	}

	private org.w3c.dom.Node getXMLNode(Element graph, String id){
		org.w3c.dom.Node element = null;
		
		for(int i = 0; i < graph.getChildNodes().getLength(); i++){
			org.w3c.dom.Node x = graph.getChildNodes().item(i);
			if(!x.getNodeName().equals("node")) continue;
			//System.out.println(x.getAttributes().getNamedItem("id").getNodeValue());
			if(x.getAttributes().getNamedItem("id").getNodeValue().equals(id))
				element = x;
		}
		return element;
	}
	
	private String getLabel(Element graph, String id) {
		org.w3c.dom.Node element = getXMLNode(graph, id);
		
		NodeList nl = graph.getElementsByTagName("y:NodeLabel");
		
		String name = null;
		for(int i = 0; i < nl.getLength(); i++){
			org.w3c.dom.Node n = nl.item(i);
			org.w3c.dom.Node p = n.getParentNode().getParentNode().getParentNode();
			if(p == element){
				name =  n.getTextContent().trim();
			}
		}
		
		return name;
	}

	public CoveringArray getCoveringArray() throws BVRException, CSVException {
		//System.out.println("--------------------------------");
		
		//System.out.println(cu.getOwnedVSpecResolution().size());
		
		// Read in
		List<Map<String, Boolean>> prods = new ArrayList<Map<String,Boolean>>();
		for(CompoundResolution c : model.getResolutionModels()){
			Map<String, Boolean> as = new HashMap<String, Boolean>();
			if(c.getResolvedVClassifier() != null){
				throw new BVRException(c.getName() + " is not a choice resolution. Only choices supported in this mode.");
			}
			as.putAll(recurse((ChoiceResolution)c));
			//System.out.println(as);
			prods.add(as);
		}
		
		// Get features
		Set<String> fs = new HashSet<String>();
		for(Map<String, Boolean> p : prods){
			fs.addAll(p.keySet());		
		}
		//System.out.println(fs);
		
		// Convert
		//System.out.println(prods);
		//System.out.println("Size: " + (prods.size()+1) + ", " + (prods.get(0).size() + 1));
		String csv[][] = new String[prods.size()+1][prods.get(0).size() + 1];
		csv[0][0] = "Feature\\Product";
		for(int i = 0; i < prods.size(); i++){
			csv[i+1][0] = "" + (i+1);
		}
		
		{
		int i = 0;
		for(String f : fs){
			csv[0][i+1] = f;
			int j = 0;
			for(Map<String, Boolean> p : prods){
				csv[j+1][i+1] = p.get(f)?"X":"-"; 
				j++;
			}
			i++;
		}
		}
		
		// Print
		String csvString = "";
		for(int i = 0; i < csv[0].length; i++){
			for(int j = 0; j < csv.length; j++){
				csvString += csv[j][i] + ";";
			}
			csvString += "\n";
		}
		
		//System.out.println(csvString);
		
		CoveringArray ca = new CoveringArrayFile(csvString);
		return ca;
	}

	private Map<String, Boolean> recurse(ChoiceResolution x) throws BVRException {
		Map<String, Boolean> as = new HashMap<String, Boolean>();
		VSpec resolvedVSPec = CommonUtility.getResolvedVSpec(x);
		if(!(resolvedVSPec instanceof Choice))
			throw new BVRException(resolvedVSPec.getName() + " is not a choice resolution. Only choices supported in this mode.");
		
		if(!(x instanceof PosResolution || x instanceof NegResolution))
			throw new BVRException(x.getName() + "choice resolution is neither PosResolution nor NegResolution");
		
		as.put(CommonUtility.getResolvedVSpec(x).getName(), (x instanceof PosResolution) ? true : false);
		if(x instanceof CompoundResolution){
			for(VSpecResolution c : ((CompoundResolution) x).getMembers()){	
				as.putAll(recurse((ChoiceResolution)c));
			}
		}
		return as;
	}
}
