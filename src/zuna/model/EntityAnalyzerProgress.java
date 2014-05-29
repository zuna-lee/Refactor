package zuna.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.operation.IRunnableWithProgress;

import zuna.parser.visitor.AbstractTypeDeclarationVisitor;
import zuna.refactoring.ProjectAnalyzer;

public class EntityAnalyzerProgress implements IRunnableWithProgress{

	private Repo iRepo;
	private IPackageFragment[] packages;
	
	public EntityAnalyzerProgress(Repo iRepo, IPackageFragment[] packages){
		this.iRepo = iRepo;
		this.packages = packages;
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 *
	 * @param unit
	 * @return
	 */
	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		
		return (CompilationUnit) parser.createAST(null);
	}


	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException{
		
		try{
			int total = packages.length + 
					this.iRepo.getMethodList().keySet().size()*2 + (this.iRepo.getClassList().keySet().size() * 4) +100;
			
			monitor.beginTask(this.iRepo.getName() + " is being analyzed, which may takes several minutes", total);
			int prog = 0;
			this.analysisEntities(monitor, prog);
			iRepo.getClass("org.jhotdraw.draw.Dimension2DDouble");
			this.makeFanout(monitor, prog);
			iRepo.getClass("org.jhotdraw.draw.LocatorLayouter");
			this.makeFanin(monitor, prog);
			this.makeInheritance(monitor, prog);
			this.makeIDFTable(monitor, prog);
			this.addMethods(monitor, prog);
			monitor.done();
			
			ProjectAnalyzer.project = iRepo;
		}catch(JavaModelException e){
			
		}
		
		
		// TODO Auto-generated method stub
		
	}
	
	private void analysisEntities(IProgressMonitor monitor, int prog) throws JavaModelException {
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE){
				monitor.subTask(mypackage.getElementName() + "package is being analyzed");
				
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					// Now create the AST for the ICompilationUnits
					CompilationUnit parser = parse(unit);
					
					AbstractTypeDeclarationVisitor typeVisitor = new AbstractTypeDeclarationVisitor();
					
					parser.accept(typeVisitor);
					
					ArrayList<TypeDeclaration> classType = typeVisitor.getTypes();
					
					MyPackage pack = iRepo.getPackage(mypackage.getElementName());
	
					for (TypeDeclaration typeDeclaration : classType) {
						
						try {
							
							iRepo.makeClassNode(pack, typeDeclaration, parser, mypackage);
						}
						catch (Exception e) {
							
							System.out.println("packages [" + mypackage.getElementName() + "." + mypackage.getKind() + "]");
							System.out.println("Class [" + unit.getElementName() + "]");
							
							e.printStackTrace();
						}
					}
					
					for (EnumDeclaration enumDeclaration : typeVisitor.getEnums()) {
						
						try {
							
							iRepo.makeEnumNode(iRepo, pack, enumDeclaration, mypackage);
						}
						catch (Exception e) {
							
							System.out.println("packages [" + mypackage.getElementName() + "." + mypackage.getKind() + "]");
							System.out.println("Enum [" + unit.getElementName() + "]");
							
							e.printStackTrace();
						}
					}
				}
				monitor.worked(++prog);
			}
		}
	}


	private void makeFanout(IProgressMonitor monitor, int prog) {
		
		ArrayList<MyMethod> add = new ArrayList<MyMethod>();
		for(String key:this.iRepo.getMethodList().keySet()){
			monitor.subTask("Outgoing relationships of " +key+" are being analyzing");
			monitor.worked(++prog);
			MyMethod m = this.iRepo.getMethodList().get(key);
			add.addAll(this.iRepo.makeFanOutList(this.iRepo, m, m.getMd()));
		}
		
		for(MyMethod m: add){
			this.iRepo.getMethodList().put(m.getID(), m);
		}
		
	}
	
	private void makeFanin(IProgressMonitor monitor, int prog) {
		
		for (MyMethod md : this.iRepo.getMethodList().values()) {
			monitor.subTask("Incoming relationships of " +md.getID()+" are being analyzing");
			monitor.worked(++prog);
			for (MyMethod fanOutMethod : md.getFanOut()) {
				if(fanOutMethod != null) {
					fanOutMethod.addFanInMethod(md);
					fanOutMethod.getParent().addUsedClasses(md.getParent());
				}
			}
		}
	}
	
	private void makeInheritance(IProgressMonitor monitor, int prog){
		
		MyClass virtualNode=null;
		if(this.iRepo.getClassList().containsKey("ROOT")){
			virtualNode = this.iRepo.getClass("ROOT");
		}else{
			virtualNode = new MyClass("ROOT", true);
			this.iRepo.getClassList().put("ROOT", virtualNode);
		}
		if(!this.iRepo.getClassList().containsKey("java.lang.Object"))
			this.iRepo.getClassList().put("java.lang.Object", new MyClass("java.lang.Object", true));
		
		for(String key: this.iRepo.getClassList().keySet()){
			monitor.subTask("Class hierarchy of "+key+" is being organizing");
			monitor.worked(prog++);
			
			MyClass c = this.iRepo.getClassList().get(key);
			
			if(!c.isEnumuration()) {
				try{
					if(c.getDeclaration() != null && c.getDeclaration().resolveBinding().getSuperclass()!=null && !c.isInterface()){
						ITypeBinding itb = c.getDeclaration().resolveBinding().getSuperclass();
						MyClass superClass = this.iRepo.getClassList().get(itb.getQualifiedName());
						if(superClass==null || !this.iRepo.getClassList().containsKey(superClass.getID()))
							superClass = this.iRepo.getClassList().get("java.lang.Object");
						c.setSuperClass(superClass);
						superClass.addChildClasses(c);
						
						ITypeBinding[] its = c.getDeclaration().resolveBinding().getTypeDeclaration().getInterfaces();
						
						for(ITypeBinding t: its){
							MyClass itfc = this.iRepo.getClass(t.getQualifiedName());
							if(itfc!=null){
								c.addInterface(itfc);
								itfc.addImplementedClasses(c);
							}
						}
					}else{
						if(!c.getID().equals("ROOT")){
							c.setSuperClass(virtualNode);
							virtualNode.addImplementedClasses(c);
						}
					}

				}catch(NullPointerException e){
//					System.out.println("Null Pointer Exception in Project Analyuzer" + " : " + deg);
				}
			
			}
		}
		
		virtualNode.addChildClasses(this.iRepo.getClass("java.lang.Object"));
		this.iRepo.getClass("java.lang.Object").setSuperClass(virtualNode);
	}
	
	
	private void makeIDFTable(IProgressMonitor monitor, int prog){
		
		
		for(String key: iRepo.getClassList().keySet()){
			monitor.subTask("DF of " + key + " is being calculated");
			
			monitor.worked(++prog);
			MyClass c = iRepo.getClassList().get(key);
			ArrayList<MyMethod> methods = c.getOwnedMethods();
			for(MyMethod m: methods){
				ArrayList<MyMethod> outs = m.getFanOut();
				for(MyMethod out: outs){
					if(!out.getParent().getID().equals(c.getID())){
						out.getParent().addNoOfCalls();
					}
				}
			}
		}
		
		double i[]= new double[iRepo.getClassList().keySet().size()];
		int cnt=0;
		for(String key: iRepo.getClassList().keySet()){
			monitor.worked(++prog);
			MyClass c = iRepo.getClassList().get(key);
			i[cnt] = c.getNoOfCalls();
			cnt++;
		}
	}
	
	private void addMethods(IProgressMonitor monitor, int prog){
		for(String key: iRepo.getClassList().keySet()){
			monitor.worked(++prog);
			MyClass c = iRepo.getClassList().get(key);
			ArrayList<MyMethod> methods = c.getOwnedMethods();
			for(MyMethod m: methods){
				if(!iRepo.getMethodList().containsKey(m.getID())){
					iRepo.getMethodList().put(m.getID(), m);
				}
			}
		}
	}
	
}

