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
package no.sintef.bvr.tool.ui.dropdown;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.ui.editor.Pair;
import no.sintef.bvr.tool.ui.command.event.AddChoiceEvent;
import no.sintef.bvr.tool.ui.command.event.AddClassifierEvent;
import no.sintef.bvr.tool.ui.command.event.AddConstraintEvent;
import no.sintef.bvr.tool.ui.command.event.AddVariableEvent;
import no.sintef.bvr.tool.ui.command.event.CutEvent;
import no.sintef.bvr.tool.ui.command.event.PasteChildEvent;
import no.sintef.bvr.tool.ui.command.event.PasteSiblingEvent;
import no.sintef.bvr.tool.ui.command.event.RemoveVSpecEvent;
import no.sintef.bvr.tool.ui.command.event.SetGroupToAltEvent;
import no.sintef.bvr.tool.ui.command.event.SetGroupToNoneEvent;
import no.sintef.bvr.tool.ui.command.event.SetGroupToOrEvent;
import no.sintef.bvr.tool.ui.command.event.ToggleOptionalEvent;
import no.sintef.bvr.ui.framework.ParallelogramTitledPanel;
import no.sintef.bvr.ui.framework.elements.ChoicePanel;
import bvr.NamedElement;

public class BCLConstraintDropDownListener extends MouseAdapter {

	private ParallelogramTitledPanel constraint1;
	private Map<JComponent, NamedElement> vmMap;
	private BVRNotifiableController view;
	private List<JComponent> nodes;
	private List<Pair<JComponent, JComponent>> bindings;

	public BCLConstraintDropDownListener(ParallelogramTitledPanel constraint1,
			Map<JComponent, NamedElement> vmMap, List<JComponent> nodes,
			List<Pair<JComponent, JComponent>> bindings, BVRNotifiableController view) {
		this.constraint1 = constraint1;
		this.vmMap = vmMap;
		this.view = view;
		this.nodes = nodes;
		this.bindings = bindings;
	}

	@Override
	public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            doPop(e);
	}

   private void doPop(MouseEvent e){
        BCLConstraintDropdown menu = new BCLConstraintDropdown(constraint1, vmMap, nodes, bindings, view);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}

class BCLConstraintDropdown extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	JMenuItem anItem;
    public BCLConstraintDropdown(ParallelogramTitledPanel constraint1, Map<JComponent, NamedElement> vmMap, List<JComponent> nodes, List<Pair<JComponent, JComponent>> bindings, BVRNotifiableController view){
		
		// Remove
		JMenuItem removechoice = new JMenuItem("remove");
		removechoice.addActionListener(new RemoveVSpecEvent(constraint1, view));
		add(removechoice);
    }
}
