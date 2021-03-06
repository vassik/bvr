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

import no.sintef.bvr.tool.context.Context;
import no.sintef.bvr.tool.controller.command.CommandBatchInterface;
import no.sintef.bvr.tool.exception.BatchRethrownException;
import no.sintef.bvr.tool.interfaces.controller.command.SimpleExeCommandInterface;

public class SimpleExeCommandBatchDecorator implements SimpleExeCommandInterface,
		CommandBatchInterface {

	private SimpleExeCommandInterface wrappedCommand;
	
	public SimpleExeCommandBatchDecorator(SimpleExeCommandInterface command){
		wrappedCommand = command;
	}
	
	@Override
	public void preExecute() {
		Context.eINSTANCE.getEditorCommands().enableBatchProcessing();
	}

	@Override
	public void postExecute() {
		Context.eINSTANCE.getEditorCommands().executeBatch();
	}

	@Override
	public void execute() {
		preExecute();
		try {
			wrappedCommand.execute();
			postExecute();
		} catch (Exception ex) {
			throw new BatchRethrownException(ex);
		} finally {
			reset();
		}
	}

	public void reset() {
		Context.eINSTANCE.getEditorCommands().disableBatchProcessing();
	}

}
