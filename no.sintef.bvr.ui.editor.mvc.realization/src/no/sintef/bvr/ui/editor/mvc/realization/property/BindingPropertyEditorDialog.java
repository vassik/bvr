package no.sintef.bvr.ui.editor.mvc.realization.property;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;

public class BindingPropertyEditorDialog extends Dialog {

	protected Object result;
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
		
		Group grpAsdasd = new Group(shell, SWT.BORDER);
		grpAsdasd.setText("Binding");
		
		GridLayout gl_grpAsdasd = new GridLayout(2, true);
		grpAsdasd.setLayout(gl_grpAsdasd);
		
		Label lblNewLabel = new Label(grpAsdasd, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Properties:");
		
		Label lblNewLabel_1 = new Label(grpAsdasd, SWT.NONE);
		lblNewLabel_1.setText("Objects:");
		
		List list = new List(grpAsdasd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_list.widthHint = 149;
		list.setLayoutData(gd_list);
		
		List list_1 = new List(grpAsdasd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list_1.widthHint = 121;
		list_1.setLayoutData(gd_list_1);
		new Label(grpAsdasd, SWT.NONE);
		
		Button btnNewButton = new Button(grpAsdasd, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Set");

	}
}
