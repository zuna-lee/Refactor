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
		this.buildMCList();
		this.remove();
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
	
	private void remove(){
		
		for(String key: this.removeList.keySet()){
			ProjectAnalyzer.project.getClassList().remove(key);
			this.cohesiveClassList.remove(key);
		}
		
		for(String key: this.mutationClassList.keySet()){
			MyClass mc = this.mutationClassList.get(key);
			adjustPointerOfCallerClass(mc);
			adjustPointerOfCallerMethods(mc);
			ProjectAnalyzer.project.getClassList().put(mc.getID(), mc);
		}
		
	}


	private void adjustPointerOfCallerClass(MyClass mc) {
		HashSet<MyClass> callerClasses = mc.getUsedClasses();
		String[] cohesiveClassID = mc.getID().split(":");
		
		for(MyClass callerClass: callerClasses){
			HashSet<MyClass> usesClassesOfCallerClass = callerClass.getUseClasses();
			for(String remove: cohesiveClassID){
				MyClass removeClass = this.removeList.get(remove);
				if(usesClassesOfCallerClass.contains(removeClass)){
					usesClassesOfCallerClass.remove(removeClass);
					usesClassesOfCallerClass.add(mc);
				}
			}
		}
	}
	
	private void adjustPointerOfCallerMethods(MyClass mc){
		ArrayList<MyMethod> methods = mc.getOwnedMethods();
		String[] cohesiveClassID = mc.getID().split(":");
		
		for(MyMethod m: methods){
			ArrayList<MyMethod> fanin = m.getFanIn();
			for(MyMethod in : fanin){
				ArrayList<MyMethod> fanout = in.getFanOut();
				ArrayList<Integer> idx = new ArrayList<Integer>();
				for(int i = 0 ; i < fanout.size() ; i++){
					for(String remove: cohesiveClassID){
						if(fanout.get(i).getParent().getID().equals(remove)){
							idx.add(i);
						}
					}	
				}
				
				for(int id: idx){
					fanout.remove(id);
					fanout.add(m);
				}
				
			}
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
			System.out.println(c.getID() + ":" + v);
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
			
			ArrayList<MyMethod> methods = c.getOwnedMethods();
			for(MyMethod m: methods){
				ArrayList<MyMethod> fanins = m.getFanIn();
				for(MyMethod in: fanins){
					ArrayList<MyMethod> fanouts = in.getFanOut();
					int cnt = 0;
					for(MyMethod out: fanouts){
						System.out.println("                 " + out.getParent().getID());
						
					}
					if(cnt==0){
						System.out.println(in.getID());
					}
					System.out.println();
				}
			}
			
//			for(MyClass used: c.getUsedClasses()){
//				HashSet<MyClass> uses = used.getUseClasses();
//				for(MyClass use: uses){
//					System.out.println("                " + used.getID() + "     :     " + use.getID());
//				}
//			}
		}
	}
}
