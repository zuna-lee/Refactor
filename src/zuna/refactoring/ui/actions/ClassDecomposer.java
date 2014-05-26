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

import zuna.metric.classDS.ArchitectureBasedDS;
import zuna.metric.classDS.InformationContents4System;
import zuna.metric.cohesion.C3;
import zuna.metric.cohesion.FCM_Distance;
import zuna.metric.cohesion.LSCC;
import zuna.model.Element;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.MyPackage;
import zuna.refactoring.ProjectAnalyzer;
import zuna.refactoring.operator.Experimentor;
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
				IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
		        Object firstElement = selection.getFirstElement();
        		init();
	            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
	            ProjectAnalyzer.firstElement = (IAdaptable)firstElement;
	            ProjectAnalyzer.analyze(project);
	            
	            //****************************************************************************************************
	            
	            Experimentor experiment = new MutationClassIdentifier();
	            experiment.prepareExperiment();
	            
	            experiment.doExperiment(new FCM_Distance(ProjectAnalyzer.project));
	            experiment.doExperiment(new LSCC(ProjectAnalyzer.project));
	            experiment.doExperiment(new C3(ProjectAnalyzer.project));
	            
			}catch(java.lang.NullPointerException e){
				e.printStackTrace();
			}catch (java.lang.ClassCastException e2){
				e2.printStackTrace();
			}
		}
	}



	private void test() {
		FCM_Distance metric = new FCM_Distance(ProjectAnalyzer.project);
		InformationContents4System icCalcul = new InformationContents4System();
		icCalcul.calculateIC();
		new ArchitectureBasedDS();
		
		MyClass c = ProjectAnalyzer.project.getClass("org.gjt.sp.jedit.indent.OpenBracketIndentRule");
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		MyMethod m1 = methods.get(1);
		ArrayList<MyMethod> out1 = m1.getFanOut();
		
		MyMethod m2 = methods.get(2);
		ArrayList<MyMethod> out2 = m2.getFanOut();
		
		for(MyMethod o1: out1){
			for(MyMethod o2: out2){
				MyClass c1 = o1.getParent();
				MyClass c2 = o2.getParent();
				if(!o1.isLibrary() && !o2.isLibrary()){
					System.out.print(o1.getMd().getName().getIdentifier()
							+ "///" +o2.getMd().getName().getIdentifier());
					this.getDistance(c1, c2);
					System.out.println();
				}
				
			}
		}
	}

	private double getDistance(MyClass c1, MyClass c2){
		ArrayList<Element> parents4M1 = new ArrayList<Element>();
		ArrayList<Element> parents4M2 = new ArrayList<Element>();
		
		this.getParents(c1, parents4M1);
		this.getParents(c2, parents4M2);
		
		Element so = this.getSubOrdinate(parents4M1, parents4M2);
		if(so!=null){
			System.out.print("////" + so.getID() + "////" + so.getIc() + "////" + so.getSe() + "////");
		}
		if(so==null || so.getID().equals("ROOT")) return 0.0;
		else return so.getIc();
	}
	
	protected ArrayList<Element> getParents(Element m1, ArrayList<Element> parents){
		Element e = null;

		if(m1 instanceof MyClass){
			e = ((MyClass) m1).getParent();
		}else if(m1 instanceof MyPackage){
			e = ((MyPackage) m1).getParent();
		}
		
		if(e!=null){
			parents.add(e);
			this.getParents(e, parents);
		}
		
		return parents;
	}
	
	protected Element getSubOrdinate(ArrayList<Element> parents4M1, ArrayList<Element> parents4M2){
		Element so = null;
		for(int i = 0 ; i < parents4M1.size(); i++){
			String uri4M1 = parents4M1.get(i).getID();
			for(int k = 0 ; k < parents4M2.size(); k++){
				String uri4M2 = parents4M2.get(k).getID();
				if(uri4M1.equals(uri4M2)){
					so = parents4M2.get(k);
					return so;
				}
			}
		}
		return null;
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
