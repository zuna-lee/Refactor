package zuna.refactoring;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import zuna.model.EntityAnalyzerProgress;
import zuna.model.MyPackage;
import zuna.model.Repo;
import zuna.parser.visitor.AbstractTypeDeclarationVisitor;

public class EntityAnalyzer implements Runnable{

	private Repo iRepo;
	private IPackageFragment mypackage;
	public EntityAnalyzer(Repo iRepo, IPackageFragment mypackage) {
		this.iRepo = iRepo;
		this.mypackage = mypackage;
	}

	public void run() {
		
		try {
			
			for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
				// Now create the AST for the ICompilationUnits
				CompilationUnit parser = EntityAnalyzerProgress.parse(unit);
				
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
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
