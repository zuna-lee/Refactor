package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class FieldVisitor extends ASTVisitor{

	private TypeDeclaration td;
	private ArrayList<String> fields = new ArrayList<String>();
	
	public FieldVisitor(TypeDeclaration fd){
		this.td = fd;
	}

	@Override
	public void endVisit(SimpleName node) {
		fields.add(td.getName() + "-" + node.getFullyQualifiedName());
	}

	public ArrayList<String> getFields() {
		return fields;
	}
	
	
} 