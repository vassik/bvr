package no.sintef.cvl.ui.editor.eclipselauncher.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import no.sintef.cvl.ui.loader.Main;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		//MessageDialog.openInformation(window.getShell(), "Test", "Hello, Eclipse world");
		
		// Redirect stdout
		
		
		// Run
		(new Thread(new HelloRunnable(window))).start();
		
	/*	
		MessageConsole console = new MessageConsole(�System Output�, null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
		MessageConsoleStream stream = console.newMessageStream();

		System.setOut(new PrintStream(stream));
		System.setErr(new PrintStream(stream));

		logger = LoggerFactory.getLogger(Application.class);
		*/
		
	}


	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}

class HelloRunnable implements Runnable {

    private IWorkbenchWindow window;

	public HelloRunnable(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run() {
		File logo = new File("C:/Users/mjoha/workspace-CVLTool2/cvl/no.sintef.cvl.ui.loader/log/logo.txt");
		File loge = new File("C:/Users/mjoha/workspace-CVLTool2/cvl/no.sintef.cvl.ui.loader/log/loge.txt");
		PrintStream po = null;
		PrintStream pe = null;
		try {
			po = new PrintStream(logo);
			pe = new PrintStream(loge);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.setOut(po);
		System.setErr(pe);
		
		System.out.println("test");
		System.err.println("test");
		
		new Main().main();
		
		po.close();
		pe.close();
    }

}