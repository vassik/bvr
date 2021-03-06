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
import java.util.List;

import no.sintef.bvr.tool.context.Context;
import bvr.BCLExpression;
import bvr.BVRModel;
import bvr.BooleanLiteralExp;
import bvr.BvrFactory;
import bvr.IntegerLiteralExp;
import bvr.PrimitiveTypeEnum;
import bvr.PrimitiveValueSpecification;
import bvr.PrimitveType;
import bvr.RealLiteralExp;
import bvr.StringLiteralExp;
import bvr.UnlimitedLiteralExp;
import bvr.VPackageable;
import bvr.ValueResolution;
import bvr.ValueSpecification;
import bvr.Variable;
import bvr.Variabletype;

public class PrimitiveTypeFacade {
	
	private static int valueResolutionCount = 0;
	
	
	private static PrimitiveTypeFacade instance = null;
	private PrimitiveTypeFacade(){
		
	}
	public static synchronized PrimitiveTypeFacade getInstance(){
		if(instance == null){
			instance = new PrimitiveTypeFacade();
		}
		return instance;
	}
	
	public List<PrimitveType> testModelsPrimitiveTypes(BVRModel model){
		List<PrimitveType> primitiveType = new ArrayList<PrimitveType>();
		for(PrimitiveTypeEnum x : PrimitiveTypeEnum.VALUES)
			primitiveType.add(testPrimitiveType(model, x));
		return primitiveType;
	}
	
	public PrimitveType testPrimitiveType(BVRModel model, PrimitiveTypeEnum type){
		PrimitveType vt = null;
		for(VPackageable x : model.getPackageElement()){
			if(x instanceof Variabletype){
				if(x instanceof PrimitveType){
					PrimitveType pt = (PrimitveType)x;
					if(pt.getType() == type)
						vt = pt;
				}
			}
		}
	
		// Create type
		if(vt == null){
			vt = BvrFactory.eINSTANCE.createPrimitveType();
			vt.setType(type);
			vt.setName(type.getLiteral());
			Context.eINSTANCE.getEditorCommands().addOwnedVariationType(model, vt);
		}
		return vt;
	}
	
	
	
	public PrimitiveValueSpecification makeValueSpecification(Variable variable) {
		PrimitiveValueSpecification value = BvrFactory.eINSTANCE.createPrimitiveValueSpecification();
		PrimitiveTypeEnum type = ((PrimitveType) variable.getType()).getType();

		if (type == PrimitiveTypeEnum.INTEGER) {
			IntegerLiteralExp ie = BvrFactory.eINSTANCE.createIntegerLiteralExp();
			ie.setInteger(0);
			value.setExpression(ie);

		} else if (type == PrimitiveTypeEnum.REAL) {
			RealLiteralExp ie = BvrFactory.eINSTANCE.createRealLiteralExp();
			ie.setReal("0.0");
			value.setExpression(ie);

		} else if (type == PrimitiveTypeEnum.BOOLEAN) {
			BooleanLiteralExp ie = BvrFactory.eINSTANCE.createBooleanLiteralExp();
			ie.setBool(false);
			value.setExpression(ie);

		} else if (type == PrimitiveTypeEnum.STRING) {
			StringLiteralExp ie = BvrFactory.eINSTANCE.createStringLiteralExp();
			ie.setString("");
			value.setExpression(ie);

		} else if (type == PrimitiveTypeEnum.UNLIMITED_NATURAL) {
			UnlimitedLiteralExp ie = BvrFactory.eINSTANCE.createUnlimitedLiteralExp();
			ie.setUnlimited(0);
			value.setExpression(ie);

		} else {
			throw new UnsupportedOperationException("Unsupported: " + type);
		}
		value.setType((PrimitveType) variable.getType());
		return value;
	}
	
	public PrimitiveValueSpecification makeValueSpecification(Variable variable, String valueStr) {
		PrimitiveValueSpecification value = BvrFactory.eINSTANCE.createPrimitiveValueSpecification();
		PrimitiveTypeEnum type = ((PrimitveType) variable.getType()).getType();
		
		if (type == PrimitiveTypeEnum.INTEGER) {
			IntegerLiteralExp ie = BvrFactory.eINSTANCE.createIntegerLiteralExp();
			ie.setInteger(Integer.parseInt(valueStr));
			value.setExpression(ie);
			
		} else if (type == PrimitiveTypeEnum.REAL) {
			RealLiteralExp ie = BvrFactory.eINSTANCE.createRealLiteralExp();
			ie.setReal(valueStr);
			value.setExpression(ie);
			
		} else if (type == PrimitiveTypeEnum.BOOLEAN) {
			BooleanLiteralExp ie = BvrFactory.eINSTANCE.createBooleanLiteralExp();
			ie.setBool(Boolean.parseBoolean(valueStr));
			value.setExpression(ie);
			
		} else if (type == PrimitiveTypeEnum.STRING) {
			StringLiteralExp ie = BvrFactory.eINSTANCE.createStringLiteralExp();
			ie.setString(valueStr);
			value.setExpression(ie);
			
		} else if (type == PrimitiveTypeEnum.UNLIMITED_NATURAL) {
			UnlimitedLiteralExp ie = BvrFactory.eINSTANCE.createUnlimitedLiteralExp();
			ie.setUnlimited(Integer.parseInt(valueStr));
			value.setExpression(ie);
			
		} else {
			throw new UnsupportedOperationException("Unsupported: " + type);
		}
		value.setType((PrimitveType) variable.getType());
		return value;
	}
	
	public String getValueAsString(ValueResolution elem) {
		String value = "";
		ValueSpecification vs = elem.getValue();
		if (vs instanceof PrimitiveValueSpecification) {
			PrimitiveValueSpecification pvs = (PrimitiveValueSpecification) vs;
			BCLExpression e = pvs.getExpression();
			if (e instanceof StringLiteralExp) {
				StringLiteralExp sle = (StringLiteralExp) e;
				value = sle.getString();
			} else if (e instanceof IntegerLiteralExp) {
				IntegerLiteralExp sle = (IntegerLiteralExp) e;
				value = "" + sle.getInteger();
			} else if (e instanceof RealLiteralExp) {
				RealLiteralExp sle = (RealLiteralExp) e;
				value = sle.getReal();
			} else if (e instanceof BooleanLiteralExp) {
				BooleanLiteralExp sle = (BooleanLiteralExp) e;
				value = "" + sle.isBool();
			} else if (e instanceof UnlimitedLiteralExp) {
				UnlimitedLiteralExp sle = (UnlimitedLiteralExp) e;
				value = "" + sle.getUnlimited();
			} else {
				throw new UnsupportedOperationException("Unsupported Expression for PrimitiveValueSpecification " + e);
			}
		} else {
			throw new UnsupportedOperationException("not PrimitiveValueSpecification is not suppoerted yet " + vs);
		}
		return value;
	}
	
	public ValueResolution createDefaultValueResolution(Variable variable){
		ValueResolution valueResolution = BvrFactory.eINSTANCE.createValueResolution();
		valueResolution.setName(variable.getName() + "_" + valueResolutionCount);
		valueResolution.setResolvedVariable(variable);
		valueResolution.setResolvedVSpec(variable);
		
		PrimitiveValueSpecification valueSpecification = makeValueSpecification(variable);
		valueResolution.setValue(valueSpecification);
		valueResolutionCount++;
		return valueResolution;
	}
	
	public void setPrimitiveValueSpecification(PrimitiveValueSpecification primitiveValue, String value){
		PrimitiveTypeEnum type = ((PrimitveType) primitiveValue.getType()).getType();
		try {
			if (type == PrimitiveTypeEnum.INTEGER) {
				IntegerLiteralExp ie = BvrFactory.eINSTANCE.createIntegerLiteralExp();
				ie.setInteger(Integer.parseInt(value));
				Context.eINSTANCE.getEditorCommands().setPrimitiveValueBCLExpression(primitiveValue, ie);
				
			} else if (type == PrimitiveTypeEnum.REAL) {
				RealLiteralExp ie = BvrFactory.eINSTANCE.createRealLiteralExp();
				ie.setReal(value);
				Context.eINSTANCE.getEditorCommands().setPrimitiveValueBCLExpression(primitiveValue, ie);
				
			} else if (type == PrimitiveTypeEnum.BOOLEAN) {
				BooleanLiteralExp ie = BvrFactory.eINSTANCE.createBooleanLiteralExp();
				ie.setBool(Boolean.parseBoolean(value));
				Context.eINSTANCE.getEditorCommands().setPrimitiveValueBCLExpression(primitiveValue, ie);
				
			} else if (type == PrimitiveTypeEnum.STRING) {
				StringLiteralExp ie = BvrFactory.eINSTANCE.createStringLiteralExp();
				ie.setString(value);
				Context.eINSTANCE.getEditorCommands().setPrimitiveValueBCLExpression(primitiveValue, ie);
				
			} else if (type == PrimitiveTypeEnum.UNLIMITED_NATURAL) {
				UnlimitedLiteralExp ie = BvrFactory.eINSTANCE.createUnlimitedLiteralExp();
				ie.setUnlimited(Integer.parseInt(value));
				Context.eINSTANCE.getEditorCommands().setPrimitiveValueBCLExpression(primitiveValue, ie);
				
			} else {
				throw new UnsupportedOperationException("unknown primitive type: " + type);
			}
		} catch (IllegalArgumentException ex){
			throw new UnsupportedOperationException("bad input the variable type : " + type);
		}
	}
	
	public void testPrimitiveValSpecValueResolution(ValueResolution valueResoultion, String value) {
		ValueSpecification curvalue = valueResoultion.getValue();
		if(curvalue == null){
			curvalue = makeValueSpecification(valueResoultion.getResolvedVariable());
			valueResoultion.setValue(curvalue);
		}
		setPrimitiveValueSpecification((PrimitiveValueSpecification) curvalue, value);
	}
}

