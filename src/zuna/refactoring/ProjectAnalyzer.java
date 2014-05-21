package zuna.refactoring;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import zuna.model.EntityAnalyzerProgress;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.MyPackage;
import zuna.model.Repo;

public class ProjectAnalyzer{
	public static IProject iproject;
	public static Hashtable<String, Metric> metrics = new Hashtable<String, Metric>();
	public static Repo project;
	public static String prjName;
	public static Repo mutatedProject;
	public static ArrayList<String> mutatedPair = new ArrayList<String>();
	public static String url;
	public static ArrayList<String> left = new ArrayList<String>();
	public static ArrayList<String> right = new ArrayList<String>();
	public static IAdaptable firstElement;
	
	public static int avgIDF= 0;
	
	public static void analyze(IProject iproject) {

		ProjectAnalyzer.iproject = iproject;
		ProjectAnalyzer.prjName = iproject.getName();
		
		Repo iRepo = new Repo(ProjectAnalyzer.iproject.getName());
		ProjectAnalyzer.url = ProjectAnalyzer.iproject.getLocationURI().getPath().toString().substring(1);
 		MyPackage root = new MyPackage("ROOT", true); 
 		
 		try {
 			IPackageFragment[] packages = JavaCore.create(ProjectAnalyzer.iproject).getPackageFragments();
 			
 			
 			for (int i = 0; i < packages.length; i++) {
 				IPackageFragment mypackage = packages[i];
 				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
 					iRepo.createProjectPackage(root, mypackage.getElementName(), mypackage);
 				}
 			}
 			
            IRunnableWithProgress entity = new EntityAnalyzerProgress(iRepo, packages);
            new ProgressMonitorDialog(new Shell()).run(true, true, entity);
 			
 		} catch (CoreException e) {
 		    e.printStackTrace();
 		} catch (InvocationTargetException ex) {
             ex.printStackTrace();
        } catch (InterruptedException ex) {
             ex.printStackTrace();
        }
	}
	
	
	private static boolean hasInternalRefer(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		
		for(MyMethod m: methods){
			ArrayList<MyField> out = m.getReferencedField();
			for(MyField o: out){
				if(o.getParent().getID().equals(c.getID())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	

	private static boolean hasInternalCallAndRefer(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		boolean flg = false;
		
		for(MyMethod m: methods){
			ArrayList<MyMethod> out = m.getFanOut();
			for(MyMethod o: out){
				if(o.getParent().getID().equals(c.getID())){
					flg = true;
				}
			}
		}
		
		if(!flg){
			for(MyMethod m: methods){
				if(m.getReferencedField().size()>0){
					flg=true;
				}
			}	
		}
		
		return flg;
	}
	

	private static boolean hasInternalCall(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		
		for(MyMethod m: methods){
			ArrayList<MyMethod> out = m.getFanOut();
			for(MyMethod o: out){
				if(o.getParent().getID().equals(c.getID())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
}
