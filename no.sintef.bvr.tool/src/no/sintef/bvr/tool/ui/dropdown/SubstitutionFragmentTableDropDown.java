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

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import no.sintef.bvr.tool.common.Constants;
import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;
import no.sintef.bvr.tool.interfaces.ui.editor.BVRRealizationUIKernelInterface;
import no.sintef.bvr.tool.ui.command.event.DeletePlacementReplacementFragmentEvent;
import no.sintef.bvr.tool.ui.editor.SubstitutionFragmentJTable;

public class SubstitutionFragmentTableDropDown extends
		SubstitutionFragmentDropDown {

	private static final long serialVersionUID = 4816222671270004432L;
	
	public SubstitutionFragmentTableDropDown(BVRNotifiableController _controller) {
		super(_controller);
		BVRRealizationUIKernelInterface kenrel = controller.getRealizationControllerInterface().getUIKernel();
		SubstitutionFragmentJTable jtable = (SubstitutionFragmentJTable) kenrel.getSubsitutionFragmentTable();
		if(jtable.getSelectedRows().length != 0){
			add(new JSeparator());
		
			JMenuItem deletePlacementReplacement = new JMenuItem(Constants.REALIZATION_DL_PLCMT_RPLCMT_NAME);
			deletePlacementReplacement.addActionListener(new DeletePlacementReplacementFragmentEvent(controller));
			add(deletePlacementReplacement);
		}
	}
}
