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
	            
	            System.out.println(ProjectAnalyzer.project.getClassList().size());
	            System.out.println(ProjectAnalyzer.project.getMethodList().size());
	            System.out.println(ProjectAnalyzer.project.getPackageList().size());
	            
	            HashMap<String, MyClass> classList = ProjectAnalyzer.project.getClassList();
	            int cnt = 0;
	            int tot = 0;
	            ArrayList<String> info = new ArrayList<String>();
	            
	            for(String key: classList.keySet()){
	            	MyClass c = classList.get(key);
	            	
	            	String id =c.getID();
	            	String fsize = String.valueOf(c.getOwendField().size());
	            	String msize = String.valueOf(c.getOwnedMethods().size());
	            	String intf = String.valueOf(c.isInterface());
	            	String abstrct = String.valueOf(c.isAbstract());
	            	String enm = String.valueOf(c.isEnumuration());
	            	
	            	String v = id + ":" + fsize + ":" + msize + ":" + intf + ":" + abstrct + ":" + enm;
	            	info.add(v);
	            	if(!c.isInterface()){
	            		tot++;
	            		if(c.getOwendField().size()==0){
	            			
	            			cnt++;
	            		}
	            	}
	            }
	            Logger2File.print2CSVFile(info, "agv034");
	            
	            System.out.println(cnt + ":" + tot + ":" + (double) cnt / (double) tot);
	            
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
