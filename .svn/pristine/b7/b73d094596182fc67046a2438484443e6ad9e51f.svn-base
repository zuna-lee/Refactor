package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {
	private ArrayList<MethodInvocation> methods = new ArrayList<MethodInvocation>();

	@Override
	public boolean visit(MethodInvocation md) {   
		methods.add(md); 
		return true;
	}
	
	public ArrayList<MethodInvocation> getMethods() {
		return methods;
	}
}