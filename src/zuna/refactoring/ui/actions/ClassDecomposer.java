package zuna.refactoring.ui.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import zuna.metric.classDS.ArchitectureBasedDS;
import zuna.metric.cohesion.C3;
import zuna.metric.cohesion.FCM_Distance;
import zuna.metric.cohesion.LSCC;
import zuna.metric.coupling.CBO;
import zuna.model.MyClass;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.Logger2File;

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
	            new ArchitectureBasedDS();
	            
	            HashMap<String, MyClass> classList = ProjectAnalyzer.project.getClassList();
	            
	            ArrayList<String> metric = new ArrayList<String>();
	            FCM_Distance fcm = new FCM_Distance(ProjectAnalyzer.project);
            	LSCC lscc = new LSCC(ProjectAnalyzer.project);
            	C3 c3 = new C3(ProjectAnalyzer.project);
            	CBO cbo = new CBO(ProjectAnalyzer.project);
            	
	            for(String key: classList.keySet()){
	            	MyClass c = classList.get(key);
	            	metric.add(c.getID() + ":" +  c.getOwnedMethods().size() + ":" + c.getOwendField().size() + ":" +
	            			fcm.getMetric(c) + ":" +  lscc.getMetric(c) + ":" + c3.getMetric(c) + ":" + cbo.getMetric(c));
	            }
	            
	            Logger2File.print2CSVFile(metric, project.getName());
	            
	            
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
    
    
	private String getClassID(CompilationUnit cu) {
		String classID = cu.getPath().toString().replace(cu.getPackageFragmentRoot().getPath().toString() + "/", "");
		classID = classID.replace("/", ".");
		classID = classID.substring(0, classID.length()-5);
		return classID;
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
