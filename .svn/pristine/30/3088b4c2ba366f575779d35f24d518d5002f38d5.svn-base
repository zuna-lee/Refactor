package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldAccess;

import zuna.model.MyClass;

public class MethodRefereceVisitor extends ASTVisitor{
	private MyClass c;
	private ArrayList<String> refField = new ArrayList<String>();
	
	public MethodRefereceVisitor(MyClass c){
		this.c = c;
	}
	
	@Override
	public void endVisit(FieldAccess node){
		refField.add(c.getID() + "-" + c.getID() + "."+node.getName() + "-" + node.resolveTypeBinding().getQualifiedName()) ;
	}
	
	public ArrayList<String> getRefField(){
		return refField;
	}
	
}