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
package no.sintef.bvr.tool.controller.command;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.controller.command.Command;
import no.sintef.bvr.tool.interfaces.ui.editor.BVRUIKernelInterface;
import no.sintef.bvr.tool.interfaces.ui.editor.Pair;
import no.sintef.bvr.tool.ui.command.Helper;
import no.sintef.bvr.ui.framework.OptionalElement.OPTION_STATE;
import no.sintef.bvr.ui.framework.ParallelogramTitledPanel;
import no.sintef.bvr.ui.framework.elements.BVRModelPanel;
import bvr.BCLConstraint;
import bvr.NamedElement;



public class ShowBCLConstraintResolution<EDITOR_PANEL, MODEL_PANEL> implements Command<EDITOR_PANEL, MODEL_PANEL> {

	BVRUIKernelInterface<EDITOR_PANEL, MODEL_PANEL> rootPanel;
	BCLConstraint oc;
	JComponent parent;
	private List<JComponent> nodes;
	private List<Pair<JComponent, JComponent>> bindings;
	private BVRNotifiableController controller;
	private Map<JComponent, NamedElement> vmMap;
	
	public Command<EDITOR_PANEL, MODEL_PANEL> init(BVRUIKernelInterface<EDITOR_PANEL, MODEL_PANEL> rootPanel, Object p, JComponent parent, Map<JComponent, NamedElement> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings, BVRNotifiableController controller) {
		this.rootPanel = rootPanel;
		this.oc = (BCLConstraint) p;
		this.parent = parent;
		this.controller = controller;
		this.vmMap = vmMap;
		this.nodes = nodes;
		this.bindings = bindings;
		return this;
	}

	@SuppressWarnings("unchecked")
	public JComponent execute() {
		ParallelogramTitledPanel constraint = new ParallelogramTitledPanel();
		nodes.add(constraint);
		vmMap.put(constraint, oc);
		Helper.bind(parent, constraint, (BVRModelPanel) rootPanel.getModelPanel(), OPTION_STATE.MANDATORY, bindings);
		
		String s = controller.getResolutionControllerInterface().getBCLConstraintString(constraint);
		constraint.setTitle(s);
		((BVRModelPanel) rootPanel.getModelPanel()).addNode(constraint);
		return constraint;
	}
}
