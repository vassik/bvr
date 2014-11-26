package no.sintef.bvr.tool.interfaces.controller;

import java.util.ArrayList;

import no.sintef.bvr.tool.interfaces.controller.command.SimpleExeCommandInterface;
import no.sintef.bvr.tool.interfaces.ui.editor.BVRRealizationUIKernelInterface;

public interface RealizationControllerInterface extends EditorsCommonControllerInterface {

	public BVRRealizationUIKernelInterface getUIKernel();
	
	public SimpleExeCommandInterface createPlacementFragmentCommand(boolean conatinment);
	
	public SimpleExeCommandInterface createReplacementFragmentCommand(boolean conatinment);
	
	public void deleteSubstitutionFragments();
	
	public SimpleExeCommandInterface createDeleteSubstitutionFragmentsCommand();

	public void deleteFragmentSubstitutions();
	
	public SimpleExeCommandInterface createDeleteFragmentSubstitutionsCommand();

	public void createFragmentSubstitution();

	public SimpleExeCommandInterface createGenerateBindingsCommand();
	
	public SimpleExeCommandInterface createUpdateFragmentSubstitutionCommand(int rowIndex, int columnIndex);

	public SimpleExeCommandInterface createUpdateSubstitutionFragmentCommand(int _rowIndex, int _columnIndex);
	
	public SimpleExeCommandInterface createHighlightFragmentElementsCommand(ArrayList<Integer> _selectedRows);
	
	public void fragmentSubstitutionRowSelected(int _selectedIndex);
	
	public void highlightBoundaryElements(int _selectedIndex);
	
	public SimpleExeCommandInterface createUpdateBindingCommand(int rowIndex, int columnIndex);

	public void clearSelection();
}