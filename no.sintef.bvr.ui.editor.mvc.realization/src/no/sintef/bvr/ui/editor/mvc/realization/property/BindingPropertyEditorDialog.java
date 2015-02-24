package no.sintef.bvr.ui.editor.mvc.realization.property;

import java.util.ArrayList;

import no.sintef.bvr.ui.editor.mvc.realization.property.model.BoundaryLabelProvider;
import no.sintef.bvr.ui.editor.mvc.realization.property.model.BoundaryListContentProvider;
import no.sintef.bvr.ui.editor.mvc.realization.property.model.BoundaryListViewerModel;
import no.sintef.bvr.ui.editor.mvc.realization.property.model.IBoundaryListViewerModel;
import no.sintef.bvr.ui.editor.mvc.realization.property.model.ISelectedBoundary;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;

import bvr.NamedElement;

public class BindingPropertyEditorDialog extends Dialog {

	protected ISelectedBoundary result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BindingPropertyEditorDialog(Shell parent, int style) {
		super(parent, style);
		setText("Edit Binding");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		System.out.println("Result!!!!" + result);
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(550, 387);
		shell.setText(getText());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
        final Monitor primary = shell.getDisplay().getPrimaryMonitor();
        final Rectangle bounds = primary.getBounds();
        final Rectangle rect = shell.getBounds();
        final int x = bounds.x + (bounds.width - rect.width) / 2;
        final int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);

		
		
		Group grpAsdasd = new Group(shell, SWT.BORDER);
		grpAsdasd.setText("Binding");
		
		GridLayout gl_grpAsdasd = new GridLayout(2, true);
		grpAsdasd.setLayout(gl_grpAsdasd);
		
		Label lblNewLabel = new Label(grpAsdasd, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Properties:");
		
		Label lblNewLabel_1 = new Label(grpAsdasd, SWT.NONE);
		lblNewLabel_1.setText("Objects:");
		
		ListViewer list = new ListViewer(grpAsdasd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		List list_2 = list.getList();
		list_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list.widthHint = 149;
		//list.setLayoutData(gd_list);
		
		final IBoundaryListViewerModel boundaryList = new BoundaryListViewerModel(new ArrayList<NamedElement>());
		
		list.setContentProvider(new BoundaryListContentProvider());
		list.setLabelProvider(new BoundaryLabelProvider());
		list.setInput(new BoundaryListViewerModel(null));
		
		final ListViewer list_1 = new ListViewer(grpAsdasd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		
		list.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object slectedObject = selection.getFirstElement();
				System.out.println(slectedObject);
				boundaryList.addBoundary((NamedElement)slectedObject);
				/*ArrayList<NamedElement> l = new ArrayList<NamedElement>();
				l.add((NamedElement) slectedObject);
				list_1.setInput(new BoundaryListViewerModel(l));*/
				list_1.refresh();
				
			}
		});
		
		
		//ListViewer list_1 = new ListViewer(grpAsdasd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		List list_3 = list_1.getList();
		list_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list_1.widthHint = 121;
		//list_1.setLayoutData(gd_list_1);
		new Label(grpAsdasd, SWT.NONE);
		
		list_1.setContentProvider(new BoundaryListContentProvider());
		list_1.setLabelProvider(new BoundaryLabelProvider());
		list_1.setInput(boundaryList);
		
		Button btnNewButton = new Button(grpAsdasd, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Set");

	}
}
