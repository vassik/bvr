package no.sintef.bvr.tool.controller;

import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import no.sintef.bvr.common.CommonUtility;
import no.sintef.bvr.common.command.SimpleExeCommandInterface;
import no.sintef.bvr.tool.controller.BVRNotifiableController;
import no.sintef.bvr.tool.controller.command.AddChoiceResolution;
import no.sintef.bvr.tool.controller.command.AddChoiceResolutionFromVClassifier;
import no.sintef.bvr.tool.controller.command.ShowInValidConstraintsResolution;
import no.sintef.bvr.tool.controller.command.ShowMultiplicityTriangleResolution;
import no.sintef.bvr.tool.controller.command.AddVariableResolution;
import no.sintef.bvr.tool.controller.command.Command;
import no.sintef.bvr.tool.controller.command.UpdateVInstance;
import no.sintef.bvr.tool.controller.command.UpdateVariableValueAssignment;
import no.sintef.bvr.tool.decorator.SimpleExeCommandBatchDecorator;
import no.sintef.bvr.tool.decorator.UpdateVInstanceBatchCmdDecorator;
import no.sintef.bvr.tool.decorator.UpdateVarValAssigBatchCmdDecorator;
import no.sintef.bvr.tool.exception.BVRModelException;
import no.sintef.bvr.tool.exception.UserInputError;
import no.sintef.bvr.tool.ui.context.StaticUICommands;
import no.sintef.bvr.tool.ui.dropdown.ResolutionDropdownListener;
import no.sintef.bvr.tool.ui.editor.BVRUIKernel;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.ui.loader.Pair;
import no.sintef.bvr.tool.ui.strategy.ResolutionLayoutStrategy;

import no.sintef.bvr.ui.framework.elements.EditableModelPanel;

import org.eclipse.emf.ecore.EObject;

import bvr.BCLConstraint;
import bvr.BVRModel;
import bvr.ChoiceResolution;
import bvr.CompoundResolution;
import bvr.Constraint;
import bvr.NamedElement;
import bvr.VClassifier;
import bvr.VSpec;
import bvr.VSpecResolution;
import bvr.ValueResolution;
import bvr.Variable;


public class SwingResolutionController<GUI_NODE extends JComponent, MODEL_OBJECT extends EObject, SERIALIZABLE extends Serializable> implements
		ResolutionControllerInterface<GUI_NODE, MODEL_OBJECT, SERIALIZABLE> {
	private BVRToolModel toolModel;

	public JTabbedPane modelPane;


	// Resolutions
	public JTabbedPane resPane;
	private List<JScrollPane> resolutionPanes;
	private List<EditableModelPanel> resolutionEpanels;
	private List<BVRUIKernel> resolutionkernels;
	private List<Map<JComponent, NamedElement>> resolutionvmMaps;
	private List<List<JComponent>> resolutionNodes;
	private List<List<Pair<JComponent, JComponent>>> resolutionBindings;

	// namecounters
	private int resolutionsCount = 0;

	private ResolutionLayoutStrategy strategy;
	BVRNotifiableController rootController;


	public SwingResolutionController(BVRToolModel model, BVRNotifiableController controller) {
		controller.setCommonControllerInterface(this);
		resolutionPanes = new ArrayList<JScrollPane>();
		resolutionEpanels = new ArrayList<EditableModelPanel>();
		resolutionkernels = new ArrayList<BVRUIKernel>();
		resolutionvmMaps = new ArrayList<Map<JComponent, NamedElement>>();
		resolutionNodes = new ArrayList<List<JComponent>>();
		resolutionBindings = new ArrayList<List<Pair<JComponent, JComponent>>>();
		strategy = new ResolutionLayoutStrategy(resolutionNodes, resolutionBindings, (ArrayList<JScrollPane>) resolutionPanes);
		toolModel = model;
		rootController = controller;
		resPane = new JTabbedPane();
	}

	private void loadBVRResolutionView(BVRModel bvrModel, List<BVRUIKernel> resolutionkernels) throws BVRModelException {
		resPane.addMouseListener(new ResolutionDropdownListener(rootController));

		if (bvrModel.getResolutionModels().size() == 0)
			return;

		for (VSpecResolution v : bvrModel.getResolutionModels()) {

			BVRUIKernel resKernel = new BVRUIKernel(null, rootController, resolutionvmMaps);
			resolutionkernels.add(resKernel);
			JScrollPane scrollPane = new JScrollPane(resKernel.getModelPanel(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.addMouseListener(new ResolutionDropdownListener(rootController));
			EditableModelPanel epanel = new EditableModelPanel(scrollPane);

			resolutionPanes.add(scrollPane);
			resolutionEpanels.add(epanel);
			Map<JComponent, NamedElement> vmMap = new HashMap<JComponent, NamedElement>();
			resolutionvmMaps.add(vmMap);
			List<JComponent> nodes = new ArrayList<JComponent>();
			resolutionNodes.add(nodes);
			List<Pair<JComponent, JComponent>> bindings = new ArrayList<Pair<JComponent, JComponent>>();
			resolutionBindings.add(bindings);

			loadBVRResolutionView(v, resKernel, null, bvrModel, vmMap, nodes, bindings);

			VSpec resolvedVSpec = CommonUtility.getResolvedVSpec(v);
			String tabtitle = (resolvedVSpec != null) ? resolvedVSpec.getName() + "[" + resolutionsCount +"]" : "null";
			resolutionsCount++;

			resPane.addTab(tabtitle, null, epanel, "");
		}
	}

	
	private void loadBVRResolutionView(VSpecResolution v, BVRUIKernel bvruikernel, JComponent parent, BVRModel bvrModel,
			Map<JComponent, NamedElement> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings) throws BVRModelException {

		JComponent nextParent = null;



		if (CommonUtility.isVSpecResolutionVClassifier(v)) {
			nextParent = new AddChoiceResolutionFromVClassifier(toolModel.isVSpecResolutionMinimized(v)).init(
					bvruikernel, v, parent, vmMap, nodes, bindings, rootController).execute();
			if(toolModel.showGrouping())
				nextParent = new ShowMultiplicityTriangleResolution().init(bvruikernel, v, nextParent,
					vmMap, nodes, bindings, rootController).execute();
			if(toolModel.showConstraints())
				new ShowInValidConstraintsResolution().init(bvruikernel, v, nextParent,
						vmMap, nodes, bindings, rootController).execute();
		} else if (v instanceof ChoiceResolution) {
			nextParent = new AddChoiceResolution(toolModel.isVSpecResolutionMinimized(v)).init(bvruikernel, v, parent,
					vmMap, nodes, bindings, rootController).execute();
			if(toolModel.showGrouping())
				nextParent = new ShowMultiplicityTriangleResolution().init(bvruikernel, v, nextParent,
					vmMap, nodes, bindings, rootController).execute();
			if(toolModel.showConstraints())
				new ShowInValidConstraintsResolution().init(bvruikernel, v, nextParent,
						vmMap, nodes, bindings, rootController).execute();
		} else if (v instanceof ValueResolution) {
			new AddVariableResolution().init(bvruikernel, v, parent,
					vmMap, nodes, bindings, rootController).execute();
		} else {
			throw new BVRModelException("Unknown element: " + v.getClass());
		}

		if (v instanceof CompoundResolution)
			for (VSpecResolution vs : ((CompoundResolution) v).getMembers()) {
				if (!toolModel.isVSpecResolutionMinimized(v)) {
					loadBVRResolutionView(vs, bvruikernel, nextParent, bvrModel, vmMap, nodes, bindings);
				}
			}
	}

	public void render() {
		loadBVRResolutionView(toolModel.getBVRModel(), resolutionkernels);
		for (BVRUIKernel resKernel : resolutionkernels) {
			strategy = new ResolutionLayoutStrategy(resolutionNodes, resolutionBindings, (ArrayList<JScrollPane>) resolutionPanes);
			resKernel.getModelPanel().layoutTreeNodes(strategy);
		}
	}

	public void notifyResolutionViewUpdate() {
		// Save
		boolean isEmpty = resPane.getTabCount() == 0;
		int resmodels = toolModel.getBVRModel().getResolutionModels().size();
		boolean modelIsEmpty = toolModel.getBVRModel().getResolutionModels().size() == 0;

		int selected = 0;
		Point pos = null;
		if (!isEmpty) {
			selected = resPane.getSelectedIndex();
			pos = resolutionPanes.get(selected).getViewport().getViewPosition();
		}

		// Clean up
		resPane.removeAll();
		
		resolutionPanes = new ArrayList<JScrollPane>();
		resolutionEpanels = new ArrayList<EditableModelPanel>();
		resolutionkernels = new ArrayList<BVRUIKernel>();
		resolutionvmMaps = new ArrayList<Map<JComponent, NamedElement>>();
		resolutionNodes = new ArrayList<List<JComponent>>();
		resolutionBindings = new ArrayList<List<Pair<JComponent, JComponent>>>();

		resolutionsCount = 0;
		render();

		// Restore positions
		if (!isEmpty && !modelIsEmpty && selected < resmodels) {
			resPane.setSelectedIndex(selected);
			resolutionPanes.get(selected).getViewport().setViewPosition(pos);
		}

	}

	@Override
	public boolean findGroupError(MODEL_OBJECT compoundResolution) {
		return toolModel.checkGroupError((CompoundResolution) compoundResolution);
	}

	public JTabbedPane getResolutionPane() {
		return resPane;
	}

	@Override
	public void addChoiceOrVClassifierResolution(GUI_NODE parent, MODEL_OBJECT resolvedVSpec) {
		NamedElement parentNamedElement = getElementInCurrentPane(parent);
		if (parentNamedElement != null) {
			VSpec vSpecToResolve = (VSpec) resolvedVSpec;
			toolModel.addChoiceOrVClassifierResolution(vSpecToResolve, (VSpecResolution) parentNamedElement);
		}
	}

	@Override
	public SimpleExeCommandInterface createResolutionModelCommand() {
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				CompoundResolution compoundResolution = toolModel.createResolution();
				toolModel.addResolutionModel(compoundResolution);
			}
		});
		return command;
	}

	@Override
	public SimpleExeCommandInterface createRemoveRootResolutionCommand() {
		final int resolutionIndex = resPane.getSelectedIndex();
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.removeRootResolution(resolutionIndex);
			}
		});
		return command;
	}

	@Override
	public SimpleExeCommandInterface createGenerateAllProductsCommand() {
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.removeAllResolutions();
				toolModel.generatAllProducts();
			}
		});
		return command;
	}

	@Override
	public SimpleExeCommandInterface createRemoveVSpecResolutionCommand(final GUI_NODE _toDelete) {
		final int resolutionIndex = resPane.getSelectedIndex();
		SimpleExeCommandInterface command = new SimpleExeCommandInterface() {
			NamedElement toDelete = null;

			@Override
			public void execute() {
				toDelete = resolutionvmMaps.get(resolutionIndex).get(_toDelete);
				toolModel.removeVSpecResolution(toDelete);

			}

		};
		return command;
	}

	public boolean performSATValidation() {
		return toolModel.performSATValidation();
	}

	@Override
	public List<String> getSATValidationMessage() {
		return toolModel.getSATValidationMessage();
	}

	@Override
	public Integer calculateCoverage(int t) {
		return toolModel.calculateCoverage(t);
	}

	@Override
	public SimpleExeCommandInterface createGenerateCoveringArrayCommand(int t) {
		final int xWise = t;

		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.generateCoveringArray(xWise);
			}
		});
		return command;
	}

	@Override
	public SimpleExeCommandInterface createToggleChoiceCommand(GUI_NODE _toToggle) {
		NamedElement toToggle = getElementInCurrentPane(_toToggle);
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.toggleChoice(toToggle);
			}
		});
		return command;
	}

	private NamedElement getElementInCurrentPane(JComponent toFind) {
		NamedElement foundNamedElement  = null;
		int index = resPane.getSelectedIndex();
		if(resPane.getSelectedIndex() >= 0){
			foundNamedElement = resolutionvmMaps.get(index).get(toFind);
		} else {
			for(Map<JComponent, NamedElement> map : resolutionvmMaps){
				foundNamedElement = map.get(toFind);
				if(foundNamedElement != null)
					break;
			}
		}
		return foundNamedElement;
	}
	
	@Override
	public SimpleExeCommandInterface createResolveSubtreeCommand(GUI_NODE _parent) {
		final VSpecResolution parent = (VSpecResolution) getElementInCurrentPane(_parent);
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.resolveSubtree(parent);
			}
		});
		return command;
	}

	@Override
	public void importResolution(SERIALIZABLE _file) {
		if(!(_file instanceof File))
			throw new UserInputError("Expect file to import");
		File file = (File) _file;
		toolModel.importResolutionFromFile(file);
	}

	@Override
	public String calculateCosts() {
		return toolModel.calculateCosts();
	}

	@Override
	public void exportAsPNGImage(SERIALIZABLE _file) {
		if(!(_file instanceof File))
			throw new UserInputError("Expect file to import");
		File file = (File) _file;
		EditableModelPanel draw = (EditableModelPanel) resPane.getSelectedComponent();
		JScrollPane draw2 = (JScrollPane) draw.modelPanel;
		JLayeredPane layeredPanel = (JLayeredPane) draw2.getViewport().getView();
		StaticUICommands.saveLayeredPaneAsPNGImage(layeredPanel, file);
	}

	@Override
	public String getModelFileLocation() {
		return (toolModel.getFile() != null) ? toolModel.getFile().getName() : null;
	}

	@Override
	public void toggleShowConstraints() {
		toolModel.toggleShowConstraints();
		notifyResolutionViewUpdate();
	}

	@Override
	public boolean isResolutionModelSet() {
		return toolModel.isResolutionModelSet();
	}

	@Override
	public void minimizeNode(GUI_NODE node) {
		NamedElement element = getElementInCurrentPane(node);
		toolModel.minimaizeVSpecResolution((VSpecResolution) element);
		notifyResolutionViewUpdate();
	}

	@Override
	public void maximizeNode(GUI_NODE node) {
		NamedElement element = getElementInCurrentPane(node);
		toolModel.maximizeVSpecResolution((VSpecResolution) element);
		notifyResolutionViewUpdate();
	}

	@Override
	public SimpleExeCommandInterface createVariableResolutionCommand(GUI_NODE parent,
			MODEL_OBJECT _variable) {
		CompoundResolution compountResolution = (CompoundResolution) getElementInCurrentPane(parent);
		Variable variable = (Variable) _variable;
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.resolveVariable(compountResolution, variable);
			}
		});
		return command;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MODEL_OBJECT getModelObjectByUINode(GUI_NODE node) {
		int resolutionIndex = resPane.getSelectedIndex();
		return (MODEL_OBJECT) resolutionvmMaps.get(resolutionIndex).get(node);
	}

	@Override
	public Command createUpdateVariableResolutionCommand(GUI_NODE elem) {
		Command command = new UpdateVarValAssigBatchCmdDecorator(new UpdateVariableValueAssignment());
		command.init(resolutionkernels.get(resPane.getSelectedIndex()),
				getElementInCurrentPane(elem), elem,
				resolutionvmMaps.get(resPane.getSelectedIndex()),
				resolutionNodes.get(resPane.getSelectedIndex()),
				resolutionBindings.get(resPane.getSelectedIndex()), rootController);
		return command;
	}

	@Override
	public void setValueResolutionValue(GUI_NODE parent, String value) {
		NamedElement namedElement = getElementInCurrentPane(parent);
		toolModel.setValueResolution((ValueResolution) namedElement, value);
	}

	@Override
	public void setValueResolutionName(GUI_NODE parent, String name) {
		NamedElement namedElement = getElementInCurrentPane(parent);
		toolModel.setValueResolutionName((ValueResolution) namedElement, name);
	}

	@Override
	public String getValueReolutionStringValue(GUI_NODE node) {
		NamedElement namedElement = getElementInCurrentPane(node);
		return toolModel.getValueResolutionAsString((ValueResolution) namedElement);
	}
	
	@Override
	public int getReslovedVClassifierCount(GUI_NODE node, MODEL_OBJECT vclassifier) {
		NamedElement namedElement = getElementInCurrentPane(node);
		return toolModel.getResolvedVClassifierCount((CompoundResolution) namedElement, (VClassifier) vclassifier);
	}

	@Override
	public SimpleExeCommandInterface createResolveNVSpecCommand(GUI_NODE panel,
			MODEL_OBJECT vspec, int instancesToResolve) {
		NamedElement parentNamedElement = getElementInCurrentPane(panel);
		VSpec vSpecToResolve = (VSpec) vspec;
		SimpleExeCommandInterface command = new SimpleExeCommandBatchDecorator(new SimpleExeCommandInterface() {
			@Override
			public void execute() {
				toolModel.addChoiceOrVClassifierResolution(vSpecToResolve, (VSpecResolution) parentNamedElement, instancesToResolve);
			}
		});
		return command;
	}

	@Override
	public Command createUpdateInstanceChoiceResolutionCommand(
			GUI_NODE vInstance) {
		Command command = new UpdateVInstanceBatchCmdDecorator(new UpdateVInstance());
		command.init(resolutionkernels.get(resPane.getSelectedIndex()),
				getElementInCurrentPane(vInstance), vInstance,
				resolutionvmMaps.get(resPane.getSelectedIndex()),
				resolutionNodes.get(resPane.getSelectedIndex()),
				resolutionBindings.get(resPane.getSelectedIndex()), rootController);
		return command;
	}
	
	@Override
	public void setNodeName(GUI_NODE node, String name) {
		NamedElement namedElement = getElementInCurrentPane(node);
		toolModel.updateName(namedElement, name);
	}

	@Override
	public void toggleShowGrouping() {
		toolModel.showGrouping(!toolModel.showGrouping());
		notifyResolutionViewUpdate();
	}

	@Override
	public List<Constraint> getInvalidConstraints() {
		return toolModel.getInvalidConstraints();
	}

	@Override
	public String getBCLConstraintString(GUI_NODE node) {
		BCLConstraint constraint = (BCLConstraint) getElementInCurrentPane(node);
		return toolModel.getBCLConstraintString(constraint);
	}

	@Override
	public List<String> validateResolutionNode(GUI_NODE component) {
		VSpecResolution vSpecResolution = (VSpecResolution)  getElementInCurrentPane(component);
		return toolModel.validateChoiceResolution(vSpecResolution);
	}

	@Override
	public void executeProduct(SERIALIZABLE destFile) {
		int index = resPane.getSelectedIndex();
		toolModel.executeResolution((File) destFile, index);
	}

}
