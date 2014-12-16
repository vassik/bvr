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
package no.sintef.bvr.tool.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.sintef.bvr.common.CommonUtility;
import no.sintef.bvr.constraints.strategy.ContextConstraintFinderStrategy;
import no.sintef.bvr.tool.exception.RethrownException;
import no.sintef.ict.splcatool.BVRException;
import no.sintef.ict.splcatool.CALib;
import no.sintef.ict.splcatool.CNF;
import no.sintef.ict.splcatool.CSVException;
import no.sintef.ict.splcatool.CoveringArray;
import bvr.BvrFactory;
import bvr.Constraint;
import bvr.VNode;
import bvr.VSpecResolution;

public class Validator implements Validate {
	 
	private List<Constraint> invalidConstraints;
	private HashMap<Constraint, List<String>> results;
	private List<String> satValidationMessage;
	
	public Validator() {
		invalidConstraints = new ArrayList<Constraint>();
		results = new HashMap<Constraint, List<String>>();
	}

	@Override
	public void validate(BVRToolModel toolModel, VSpecResolution vsr) {
		CoveringArray ca;
		satValidationMessage = new ArrayList<String>();
		try {
			ca = toolModel.getBVRM().getCoveringArray();
		} catch (CSVException e) {
			throw new RethrownException("Getting CA failed:", e);
		} catch (BVRException e) {
			throw new RethrownException("Getting CA failed:", e);
		}
		CNF cnf;
		try {
			cnf = toolModel.getBVRM().getGUIDSL(new ContextConstraintFinderStrategy((VNode) CommonUtility.getResolvedVSpec(vsr))).getSXFM().getCNF();
			boolean valid = CALib.verifyCA(cnf, ca, true, satValidationMessage);
			//do this stab for now since we do not actually have mapping yet, between problem messages and constraints
			if(!valid)
				invalidConstraints.add(BvrFactory.eINSTANCE.createConstraint());
		} catch (Exception e) {
			throw new RethrownException("Validation failed:", e);
		}
	}

	@Override
	public List<String> getConstraintValidationResultMessage(Constraint constraint) {
		return satValidationMessage;
	}

	@Override
	public List<Constraint> getInvalidConstraints() {
		return invalidConstraints;
	}

}