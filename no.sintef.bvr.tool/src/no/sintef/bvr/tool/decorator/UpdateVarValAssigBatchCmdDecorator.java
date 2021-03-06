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
package no.sintef.bvr.tool.decorator;

import javax.swing.JComponent;

import no.sintef.bvr.tool.controller.command.CommandBatchInterface;
import no.sintef.bvr.tool.controller.command.UpdateVariableValueAssignment;

public class UpdateVarValAssigBatchCmdDecorator<EDITOR_PANEL, MODEL_PANEL> extends
		UpdateVariableValueAssignmentDecorator<EDITOR_PANEL, MODEL_PANEL> implements CommandBatchInterface {

	public UpdateVarValAssigBatchCmdDecorator(
			UpdateVariableValueAssignment<EDITOR_PANEL, MODEL_PANEL> _wrapped) {
		super(_wrapped);
	}

	@Override
	public JComponent execute() {
		preExecute();
		JComponent component = super.execute();
		postExecute();
		return component;
	}

	@Override
	public void preExecute() {
		controller.getCommonControllerInterface().enableBatchCommandProcessing();
	}

	@Override
	public void postExecute() {
		controller.getCommonControllerInterface().executeCommandBatch();
		controller.getCommonControllerInterface().disableBatchCommandProcessing();
	}
}
