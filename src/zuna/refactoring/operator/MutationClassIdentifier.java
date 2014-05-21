package zuna.refactoring.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.refactoring.ProjectAnalyzer;

public class MutationClassIdentifier {

	Hashtable<String, MyClass> cohesiveClassList = new Hashtable<String, MyClass>();
	Hashtable<String, MyClass> classList = new Hashtable<String, MyClass>();
	Hashtable<String, MyClass> mutationClassList = new Hashtable<String, MyClass>();
	Hashtable<String, MyClass> removeList = new Hashtable<String, MyClass>();
	
	
	public void prepareExperiment(ArrayList<String> cohesiveClassList){
		this.buildCCList(cohesiveClassList);
		this.print();
		this.buildMCList();
		this.remove();
		this.print();
		this.mergeLists();
	}
	

	private void buildCCList(ArrayList<String> cohesiveClassList){
		for(String cohesiveClass: cohesiveClassList){
			MyClass c = ProjectAnalyzer.project.getClass(cohesiveClass);
			this.cohesiveClassList.put(cohesiveClass, c);
		}
	}
	
	private void buildMCList(){
		for(String key: this.cohesiveClassList.keySet()){
			
			MyClass c1 = this.cohesiveClassList.get(key);
			MyClass c2 = getRelatedClass(c1);
			
			if(!this.removeList.containsKey(c1.getID()) && c2!=null){
				MutationClass mc = new MutationClass(c1, c2);
				this.mutationClassList.put(mc.getMutationClass().getID(), mc.getMutationClass());
				this.removeList.put(c1.getID(), c1);
			}
		}
	}
	
	private void changePointerTo(MyClass mutationClass, MyClass original){
		
		for(String key: this.removeList.keySet()){
			MyClass c = ProjectAnalyzer.project.getClass(key);
			for(MyClass in : c.getUsedClasses()){
				if(in.getID().equals(original.getID())){
					c.getUsedClasses().remove(in);
					c.addUsedClasses(mutationClass);
					break;
				}
			}
			
			for(MyClass out : c.getUseClasses()){
				if(out.getID().equals(original.getID())){
					c.getUseClasses().remove(out);
					c.addUsesClasses(mutationClass);
					break;
				}
			}

			for(MyMethod m : c.getOwnedMethods()){
				ArrayList<MyMethod> fanin = m.getFanIn();
				for(int i = 0 ; i < fanin.size() ; i++){
					MyMethod in = fanin.get(i);
					if(in.getParent().getID().equals(original)){
						
					}
				}
			}
		}
	}
	
	private void remove(){
		for(String key: this.removeList.keySet()){
			this.cohesiveClassList.remove(key);
		}
	}
	
	private MyClass getRelatedClass(MyClass c){
		HashSet<MyClass> callee = c.getUseClasses();
		HashSet<MyClass> caller = c.getUsedClasses();
		
		for(String key: this.cohesiveClassList.keySet()){
			
			MyClass cand = this.cohesiveClassList.get(key);
			if((isRelated(callee, cand, c) || isRelated(caller, cand, c)) 
					&& !this.removeList.containsKey(cand.getID())){
				this.removeList.put(cand.getID(), cand);
				return cand;
			}
		}
		
		return null;
	}
	
	private boolean isRelated(HashSet<MyClass> relatedClasses, MyClass cand, MyClass c){
		for(MyClass rc: relatedClasses){
			if(rc.getID().equals(cand.getID()) && !rc.getID().equals(c.getID())){
				return true;
			}
		}
		return false;
	}
	

	private void mergeLists() {
		classList.putAll(this.cohesiveClassList);
		classList.putAll(this.mutationClassList);
	}
	
	public void doExperiment(Metric metric){
		for(String key: classList.keySet()){
			MyClass c = classList.get(key);
			double v = metric.getMetric(c);
		}
	}
	
	public void doExperiment(Metric metric, Metric metric2){
		
	}

	public void doExperiment(Metric metric, Metric metric2, Metric metric3){

	}

	private void print(){
		System.out.println("========= Cohesive Class List =======");
		for(String key: this.cohesiveClassList.keySet()){
			MyClass c = this.cohesiveClassList.get(key);
			System.out.println(c.getID()
					+ ":" + c.getUseClasses().size()
					 + ":" + c.getUsedClasses().size()
					 + ":" + c.getOwnedMethods().size()
					 + ":" + c.getOwendField().size());
			for(MyMethod m: c.getOwnedMethods()){
				System.out.println("                " + m.getID() + ":" + m.getFanIn().size() + ":" + m.getFanOut().size());
			}
		}
		
		System.out.println("========= Mutated Class List =======");
		for(String key: this.mutationClassList.keySet()){
			MyClass c = this.mutationClassList.get(key);
			System.out.println(c.getID()
					+ ":" + c.getUseClasses().size()
					 + ":" + c.getUsedClasses().size()
					 + ":" + c.getOwnedMethods().size()
					 + ":" + c.getOwendField().size());
			
			for(MyMethod m: c.getOwnedMethods()){
				System.out.println("                " + m.getID() + ":" + m.getFanIn().size() + ":" + m.getFanOut().size());
			}
			
		}
	}
}
