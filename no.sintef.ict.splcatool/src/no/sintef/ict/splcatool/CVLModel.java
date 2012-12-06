package no.sintef.ict.splcatool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.prop4j.Node;
import org.prop4j.NodeReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cvl.CVSpec;
import cvl.Choice;
import cvl.ChoiceResolutuion;
import cvl.ConfigurableUnit;
import cvl.MultiplicityInterval;
import cvl.OpaqueConstraint;
import cvl.VClassifier;
import cvl.VSpec;
import cvl.cvlFactory;
import cvl.cvlPackage;
import cvl.Constraint;
import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

public class CVLModel {
	private ConfigurableUnit cu;

	public CVLModel(){
		cvlPackage.eINSTANCE.eClass();
		cvlFactory factory = cvlFactory.eINSTANCE;
		cu = factory.createConfigurableUnit();
		cu.setName("Configurable Unit 1");
	}

	public CVLModel(File f) {
		cvlPackage.eINSTANCE.eClass();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createURI("file:///" + f.getAbsolutePath()), true);
		cu = (ConfigurableUnit)resource.getContents().get(0);
	}

	public CVLModel(ConfigurableUnit cu) {
		this.cu = cu;
	}

	public CVLModel(String cvlfile) {
		this(new File(cvlfile));
	}

	public void writeToFile(String filename) throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	    ResourceSet resSet = new ResourceSetImpl();
	    Resource resource = resSet.createResource(URI.createURI("file:///" + filename));
	    resource.getContents().add(cu);
	    resource.save(Collections.EMPTY_MAP);
	}

	public ConfigurableUnit getCU() {
		return cu;
	}

	public GUIDSL getGUIDSL() throws IOException, UnsupportedModelException {
		FeatureModel fm = new FeatureModel();
		Feature root = recursiveConvert(fm, (Choice)cu.getOwnedVSpec().get(0)); // This is an assumption
		fm.setRoot(root);
		
		// Add constraints
		//System.out.println(fm.getFeatureNames());
		for(Constraint c : cu.getOwnedConstraint()){
			OpaqueConstraint oc = (OpaqueConstraint)c;
			NodeReader nr = new NodeReader();
			Node n = nr.stringToNode(oc.getConstraint(), new ArrayList<String>(fm.getFeatureNames()));
			fm.addPropositionalNode(n);
			//System.out.println(n.toString(NodeWriter.textualSymbols));
			//System.out.println(oc.getConstraint() + " became " + NodeWriter.nodeToString(n));
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
		for(VSpec vc : vs.getChild()){
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

	public void injectConfigurations(GraphMLFM gfm) {
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
		
		int i = 0;
		for(Map<String, Boolean> conf : confs){
			//System.out.println(i++ + ": " + x);
			ChoiceResolutuion cr = recursivelyResolve(conf, (Choice)cu.getOwnedVSpec().get(0));
			cu.getOwnedVSpecResolution().add(cr);
		}
	}
	
	private ChoiceResolutuion recursivelyResolve(Map<String, Boolean> conf, Choice choice) {
		// Add node
		ChoiceResolutuion cr = cvlFactory.eINSTANCE.createChoiceResolutuion();
		cr.setResolvedChoice(choice);
		cr.setResolvedVSpec(choice);
		cr.setDecision(conf.get(choice.getName()));
		
		// Add children
		for(VSpec x : choice.getChild()){
			ChoiceResolutuion crc = recursivelyResolve(conf, (Choice) x);
			cr.getChild().add(crc);
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
}
