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
package no.sintef.bvr.tool.ui.command.event;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import no.sintef.bvr.tool.interfaces.controller.BVRNotifiableController;


public class BindingRowSelectionEvent implements ListSelectionListener {


	private BVRNotifiableController controller;

	public BindingRowSelectionEvent(BVRNotifiableController _controller){
		controller = _controller;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {		
		int selectedIndex = ((ListSelectionModel) event.getSource()).getLeadSelectionIndex();
		if(!event.getValueIsAdjusting() && selectedIndex >= 0)
			controller.getRealizationControllerInterface().highlightBoundaryElements(selectedIndex);
	}
}
