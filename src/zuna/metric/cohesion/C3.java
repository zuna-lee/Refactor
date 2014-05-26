package zuna.metric.cohesion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import tml.Configuration;
import tml.DbConnection;
import tml.corpus.Corpus;
import tml.corpus.CorpusParameters.DimensionalityReduction;
import tml.corpus.CorpusParameters.TermSelection;
import tml.corpus.RepositoryCorpus;
import tml.storage.Repository;
import tml.vectorspace.TermWeighting.GlobalWeight;
import tml.vectorspace.TermWeighting.LocalWeight;
import tml.vectorspace.operations.PassagesSimilarity;
import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;
import Jama.Matrix;

public class C3 extends Metric{

	protected static Repository repository;
	protected static Properties prop;
	private static PassagesSimilarity distance;
	public static Hashtable<String, Double> msTable = new Hashtable<String, Double>();
	
	public C3(Repo p){
		super(p);
		msTable.clear();
		try {
			prop = Configuration.getTmlProperties(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
	}

	public C3(Repo p, MyClass c){
		super(p);
		msTable.clear();
		this.getMetric(c);
		try {
			prop = Configuration.getTmlProperties(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
	}
	
	@Override
	public double getMetric(MyClass c) {
		double values = 0.0;
		try {
				Repository.cleanStorage(Configuration.getTmlFolder() + "/test/lucene");
				repository = new Repository(Configuration.getTmlFolder() + "/test/lucene");
				repository.addMethods(c);
				
				Corpus corpus = new RepositoryCorpus();
				corpus.getParameters().setTermSelectionCriterion(TermSelection.DF);
				corpus.getParameters().setTermSelectionThreshold(0);
				corpus.getParameters().setTermWeightLocal(LocalWeight.TF);
				corpus.getParameters().setTermWeightGlobal(GlobalWeight.Idf);
				corpus.getParameters().setDimensionalityReduction(DimensionalityReduction.NO);
				corpus.getParameters().setNormalizeDocuments(true);
				corpus.load(repository);
				
				distance = new PassagesSimilarity();
				distance.setCorpus(corpus);
				distance.start();
				
				final Vector<String> Observations = new Vector<String>();
				
				for (String doc : corpus.getPassages())
					Observations.add(doc);
				
				Matrix m = distance.getSimilarities();
				
				for(int i = 0 ; i < m.getRowDimension()-1; i++){
					for(int j = i+1 ; j < m.getColumnDimension(); j++){
						double v = m.get(i, j);
						values+= v;
						msTable.put(Observations.get(i) + ":" + Observations.get(j), v);
					}
				}
				values = values/((m.getRowDimension() * (m.getRowDimension()-1))/2);
				DbConnection.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} 
		
		return values;
	}
	
	
	public static void getMethodText(MyClass c, String url){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		for(MyMethod m: methods){
			String id = m.getID().replace("(", " ");
			id.replace(")", "");
			id.replace("<", "");
			id.replace(">", "");
			id.replace(",", " ");
			id.replace("[", "");
			id.replace("]", "");
			
			try {
				FileWriter fw = new FileWriter(new File(url + "/" + id + ".java"));
				fw.write(m.getID() + "\n" + m.getMd().toString());
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public double getMetric(MyMethod m1, MyMethod m2) {
		try{
			double v = msTable.get(m1.getID() + ":" + m2.getID());	
			return v;
		}catch(java.lang.NullPointerException e){
			return 0.0;
		}
		
		
	}

}
