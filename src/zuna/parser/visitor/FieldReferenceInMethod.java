package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;

import zuna.model.MyField;

public class FieldReferenceInMethod extends ASTVisitor{
	ArrayList<Expression> simpleNames = new ArrayList<Expression>();

	ArrayList<MyField> fields = new ArrayList<MyField>();
	
	public FieldReferenceInMethod(ArrayList<MyField> fields){
		this.fields = fields;
	}
	
	
	public ArrayList<Expression> getSimpleNames() {
		return simpleNames;
	}


	public boolean visit(SimpleName ref){
		if(isFieldName(ref)){
			simpleNames.add(ref);
		}
		return true;
	}
	
	
	private boolean isFieldName(SimpleName ref){
		for(MyField f: this.fields){
			String[] tokens = f.getID().split("\\.");
			String fieldName = tokens[tokens.length-1];
			
			if(fieldName.equals(ref.getIdentifier())){
				return true;
			}
		}
		return false;
	}
	
	private String getFieldName(String qualifiedFieldName){
		String[] tokens = qualifiedFieldName.split("\\.");
		return tokens[tokens.length-1];
	}

}
