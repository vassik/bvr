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
package no.sintef.bvr.tool.environment;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

import no.sintef.bvr.common.logging.Logger;
import no.sintef.bvr.common.logging.ResetableLogger;
import no.sintef.bvr.engine.interfaces.common.IResourceContentCopier;
import no.sintef.bvr.tool.exception.UnimplementedBVRException;
import no.sintef.bvr.tool.model.BVRToolModel;
import no.sintef.bvr.tool.primitive.SymbolVSpec;
import no.sintef.bvr.ui.editor.commands.EditorCommands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class AbstractEnvironment implements Environment {
	
	@Override
	public BVRToolModel loadModelFromFile(File file) {
		throw new UnimplementedBVRException("loadModelFromFile is not implemented");
	}

	@Override
	public void writeModelToFile(BVRToolModel model, File file) {
		throw new UnimplementedBVRException("writeModelToFile is not implemented");
	}

	@Override
	public void reloadModel(BVRToolModel model) {
		throw new UnimplementedBVRException("reloadModel is not implemented");
	}

	@Override
	public EObject getEObject(Object object) {
		throw new UnimplementedBVRException("getEObject is not implemented");
	}

	@Override
	public List<Object> getSelections() {
		throw new UnimplementedBVRException("getSelections is not implemented");
	}

	@Override
	public void highlightObjects(
			EList<HashMap<EObject, Integer>> objectsToHighlightList) {
		throw new UnimplementedBVRException("highlightObjects is not implemented");

	}

	@Override
	public void clearHighlights() {
		throw new UnimplementedBVRException("clearHighlights is not implemented");
	}

	@Override
	public JFileChooser getFileChooser() {
		throw new UnimplementedBVRException("getFileChooser is not implemented");
	}

	@Override
	public void writeProductsToFiles(
			HashMap<Resource, IResourceContentCopier> baseProductMap, File file) {
		throw new UnimplementedBVRException("writeProductsToFiles is not implemented");
	}

	@Override
	public void performSubstitutions(List<SymbolVSpec> symbols) {
		throw new UnimplementedBVRException("performSubstitutions is not implemented");
	}

	@Override
	public Logger getLogger() {
		throw new UnimplementedBVRException("getLogger is not implemented");
	}
	
	@Override
	public ResetableLogger getProblemLogger() {
		throw new UnimplementedBVRException("getProblemLogger is not implemented");
	}

	@Override
	public ConfigHelper getConfig() {
		throw new UnimplementedBVRException("getConfig is not implemented");
	}

	@Override
	public EditorCommands getEditorCommands() {
		throw new UnimplementedBVRException("Commands are not implemented for this environment");
	}

	@Override
	public void disposeModel(BVRToolModel model) {
		throw new UnimplementedBVRException("disposeModel is not implemented for this environment");
	}
}
