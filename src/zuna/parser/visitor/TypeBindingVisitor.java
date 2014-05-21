package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeBindingVisitor extends ASTVisitor {
	private ArrayList<ITypeBinding> types = new ArrayList<ITypeBinding>();
	
	public ArrayList<ITypeBinding> getTypeBinding() {
		return types;
	}

	public boolean visit(ITypeBinding type) {
		this.types.add(type);
		return true;
	}
}
