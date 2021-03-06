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
package no.sintef.bvr.tool.interfaces.controller;

import java.util.List;

import javax.swing.JComponent;

import bvr.Constraint;
import no.sintef.bvr.tool.interfaces.controller.command.SimpleExeCommandInterface;
import no.sintef.bvr.tool.interfaces.controller.command.Command;


public interface ResolutionControllerInterface<GUI_NODE, MODEL_OBJECT, SERIALIZABLE> extends EditorsCommonControllerInterface<SERIALIZABLE, GUI_NODE, MODEL_OBJECT> {

	public void addChoiceOrVClassifierResolution(GUI_NODE parent,
			MODEL_OBJECT resolvedVSpec);

	public SimpleExeCommandInterface createResolutionModelCommand();

	public SimpleExeCommandInterface createRemoveRootResolutionCommand();

	public SimpleExeCommandInterface createGenerateAllProductsCommand();

	public SimpleExeCommandInterface createRemoveVSpecResolutionCommand(GUI_NODE toDelete);

	public boolean performSATValidation();

	public List<String> getSATValidationMessage();

	public Integer calculateCoverage(int t);

	public SimpleExeCommandInterface createGenerateCoveringArrayCommand(int t);

	public SimpleExeCommandInterface createToggleChoiceCommand(GUI_NODE _toToggle);

	public SimpleExeCommandInterface createResolveSubtreeCommand(GUI_NODE parent);

	public void importResolution(SERIALIZABLE file);

	public String calculateCosts();
	
	public void toggleShowConstraints();
	
	public boolean isResolutionModelSet();

	public void minimizeNode(GUI_NODE node);
	
	public void maximizeNode(GUI_NODE node);
	
	public SimpleExeCommandInterface createVariableResolutionCommand(GUI_NODE parent, MODEL_OBJECT variable);

	public Command createUpdateVariableResolutionCommand(GUI_NODE elem);

	public void setValueResolutionValue(GUI_NODE parent, String value);

	public void setValueResolutionName(GUI_NODE parent, String name);

	public String getValueReolutionStringValue(GUI_NODE node);

	public int getReslovedVClassifierCount(GUI_NODE panel, MODEL_OBJECT vclassifier);

	public SimpleExeCommandInterface createResolveNVSpecCommand(
			GUI_NODE panel, MODEL_OBJECT vspec, int instancesToResolve);

	public Command createUpdateInstanceChoiceResolutionCommand(
			GUI_NODE vInstance);

	public boolean findGroupError(MODEL_OBJECT compoundResolution);

	public void toggleShowGrouping();

	public List<Constraint> getInvalidConstraints();

	public String getBCLConstraintString(GUI_NODE constraint);

	public List<String> validateResolutionNode(
			GUI_NODE component);

	public void executeProduct(SERIALIZABLE destFile);

	public SimpleExeCommandInterface createRemoveAllResolutionsCommand();

	public void removeUncontainedResolutions(GUI_NODE p);

	public boolean performSATValidationSingleResolution();

	public SimpleExeCommandInterface createRenameResolutionCommand(String name);
}
