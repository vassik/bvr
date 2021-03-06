/*******************************************************************************
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June
 * 2007; you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package no.sintef.bvr.constraints.strategy;

import no.sintef.bvr.constraints.interfaces.strategy.IBCLBuilderStrategy;
import bvr.BvrFactory;
import bvr.Target;
import bvr.VSpec;

public class DefaultTestBCLBuilderStartegy implements IBCLBuilderStrategy {

	@Override
	public Target getVSpecTarget(VSpec node) {
		Target vspecTarget = BvrFactory.eINSTANCE.createTarget();
		vspecTarget.setName(node.getName());
		node.setTarget(vspecTarget);
		return vspecTarget;
	}

	@Override
	public VSpec testVSpecName(VSpec node, String name) {
		if (node.getName() != null && node.getName().equals(name))
			return node;
		return null;
	}

}
