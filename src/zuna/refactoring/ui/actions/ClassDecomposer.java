package zuna.refactoring.ui.actions;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import zuna.metric.classDS.InformationContents4System;
import zuna.metric.cohesion.c3.C3;
import zuna.refactoring.ProjectAnalyzer;
import zuna.refactoring.operator.MutationClassIdentifier;

@SuppressWarnings("restriction")
public class ClassDecomposer implements IWorkbenchWindowActionDelegate {
	private static IWorkbenchWindow window;
	public static double th=21;
	/**
	 * The constructor.
	 */
	public ClassDecomposer() {
		
	}

	
	
	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	
	@Override
	public void run(IAction action) {
	
		window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
		{
			try {
	            // 10 is the workload, so in your case the number of files to copy
				IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
				
		        Object firstElement = selection.getFirstElement();
        		init();
	            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
	            ProjectAnalyzer.firstElement = (IAdaptable)firstElement;
	            
	            ProjectAnalyzer.analyze(project);
	            InformationContents4System icCalcul = new InformationContents4System();
	            icCalcul.calculateIC();
	            
	            
	            ArrayList<String> cc = new ArrayList<String>();
	            cc.add("org.jhotdraw.draw.AbstractConnector");
	            cc.add("org.jhotdraw.geom.Geom");
	            
	            MutationClassIdentifier mutation = new MutationClassIdentifier();
	            mutation.prepareExperiment(cc);
//	            mutation.doExperiment(new FCM_Distance(ProjectAnalyzer.project));
	            mutation.doExperiment(new C3(ProjectAnalyzer.project));
	            
//	            for(String key: ProjectAnalyzer.project.getClassList().keySet()){
//	            	MyClass c= ProjectAnalyzer.project.getClassList().get(key);
//	            	System.out.println(c.getID());
//	            	ArrayList<MyMethod> methods = c.getOwnedMethods();
//	            	
//	            	for(MyMethod m: methods){
//	            		if(m.getFanOut().size()>1){
//	            			if(m.getFanOut().get(0).getParent().getID().startsWith("org.") && !m.getFanOut().get(0).getParent().getID().equals(c.getID())
//	            					&& !m.getFanOut().get(0).getParent().isInterface()){
//	            				System.out.println(m.getFanOut().get(0).getParent().getID() + ":"
//	            						+ m.getFanOut().get(0).getParent().isLibrary());
//		            			return;
//	            			}
//	            			
//	            		}
//	            	}
//	            }
	            
			}catch(java.lang.NullPointerException e){
				e.printStackTrace();
			}catch (java.lang.ClassCastException e2){
				e2.printStackTrace();
			}
		}
	}



    private void init(){
		ProjectAnalyzer.project = null;
		ProjectAnalyzer.firstElement=null;
	}
    
    
	
	
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose() {
		
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	
	
//	private HashMap<String, MyMethod> getRefactoredMode(Repo p1, Repo p2){
//		ArrayList<MyMethod> diff = ASTParserUtil.findDifferences(p1, p2);
//		HashMap<String, MyMethod> changedMethods = new HashMap<String, MyMethod>(); 
//		for(MyMethod m: diff){
//			changedMethods.put(m.getID(), m);
//		}
//		
//		return changedMethods;
//	}

}
