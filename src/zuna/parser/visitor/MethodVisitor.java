package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

public class MethodVisitor extends ASTVisitor {
	private ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

	@Override
	public boolean visit(MethodDeclaration md) {   
		methods.add(md);
		return true;
	}
	
	@Override
	public boolean visit(FieldAccess node) {
//		System.out.println(node.getName());
		return true;
	}
	
	@Override
	public void endVisit(TypeParameter node){
		
	}
	
	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}
	
}