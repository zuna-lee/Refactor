package zuna.metric.cohesion.c3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

import tml.corpus.Corpus;
import tml.corpus.CorpusParameters.DimensionalityReduction;
import tml.corpus.CorpusParameters.TermSelection;
import tml.corpus.RepositoryCorpus;
import tml.sql.DbConnection;
import tml.vectorspace.TermWeighting.GlobalWeight;
import tml.vectorspace.TermWeighting.LocalWeight;
import tml.vectorspace.operations.PassagesSimilarity;
import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.MyParameter;
import zuna.model.Repo;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.Tokenizer;
import Jama.Matrix;

public class C3 extends Metric{

	protected static TMLRepository repository;
	protected static Properties prop;
	private static PassagesSimilarity distance;
	public static Hashtable<String, Double> msTable = new Hashtable<String, Double>();
	private ArrayList<MyMethod> methods = new ArrayList<MyMethod>();
	private String wordsForBody = "";
	private File file;
	
	public C3(Repo p){
		super(p);
		msTable.clear();
		try {
			prop = TMLConfiguration.getTmlProperties(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}


	@Override
	public double getMetric(MyClass c) {
		double values = 0.0;
//		this.getIdentifier(c);
		try {
			
			TMLRepository.cleanStorage(TMLConfiguration.getTmlFolder() + "/test/lucene");
			repository = new TMLRepository(TMLConfiguration.getTmlFolder() + "/test/lucene");
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
			new DbConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		this.deleteFiles();
		return values;
	}
	

	public double getMetric(MyMethod m1, MyMethod m2) {
		try{
			double v = msTable.get(m1.getID() + ":" + m2.getID());	
			return v;
		}catch(java.lang.NullPointerException e){
			return 0.0;
		}
	}
	
	private void getIdentifier(MyClass c) {
		methods = c.getOwnedMethods();
		String corpus = "";
		this.init();
		try {
			
			for(int i = 0 ; i < methods.size() ; i++)
			{
				BufferedReader brOfMethodName = getIdentifierFromMethodName(i);
				BufferedReader brOfMethodParam = getIdentifierFromParameters(i);
				BufferedReader brOfMethodBody = getIdentifierFromMethodBody(i);
				
				String terms = "";
				
				
					String termTmp="";
					while((termTmp = brOfMethodName.readLine())!=null)
					{
						terms += termTmp.trim().toLowerCase() + " ";
					}
					
					while((termTmp = brOfMethodParam.readLine())!=null)
					{
						terms += termTmp.trim().toLowerCase() + " ";
					}
					
					while((termTmp = brOfMethodBody.readLine())!=null)
					{
						terms += termTmp.trim().toLowerCase() + " ";
					}
					
				
				String id = methods.get(i).getID().replace(':', '_');
				id = id.replace('<', '[');
				id = id.replace('>', ']');
				
				corpus+= file.getAbsolutePath() + "/"+id + ".txt\n";
				this.saveFiles(id, "txt", terms);
			}
		} catch (IOException | java.lang.NullPointerException e) {
			e.printStackTrace();
		}
//		this.saveFiles("corpus", "txt", corpus);
	}

	private void init() {
		String workingSpacePath = ProjectAnalyzer.iproject.getWorkspace().getRoot().getLocation().toString();
		file = new File(workingSpacePath + "/sspace/");
	}

	private void saveFiles(String methodID, String extension, String terms) {
		try {
			
			if(!file.exists()) file.mkdir();
			
			
			File newFile = new File(file.getPath()+ "/" + methodID + "." + extension);
			if(!newFile.exists()) {
				newFile.createNewFile();
			}
			else{
				newFile.delete();
				newFile.createNewFile();
			}
			
			newFile.setWritable(true);
			FileWriter writer = new FileWriter(newFile);
			writer.write(terms);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFiles(){
		File[] files = this.file.listFiles();
		for(File f: files){
			f.delete();
		}
		this.file.delete();
	}

	private BufferedReader getIdentifierFromParameters(int i)  throws java.lang.NullPointerException{
		
		String words = "";
		for(MyParameter param: methods.get(i).getParameters()){
			ArrayList<String> tokens = Tokenizer.tokenize(param.getName());
			for(String t: tokens){
				words += t + " \n";
			}
		}
		
		return new BufferedReader(new StringReader(words));
	}

	private BufferedReader getIdentifierFromMethodBody(int i)  throws java.lang.NullPointerException{
		
		wordsForBody = "";
		methods.get(i).getMd().getBody().accept(new ASTVisitor(){
			
//			public boolean visit(VariableDeclarationFragment  var){
//				SimpleName name = var.getName();
//				String rawName = name.getIdentifier();
//				ArrayList<String> tokenizedName = Tokenizer.tokenize(rawName);
//				for(String token: tokenizedName){
//					wordsForBody += token + " \n";
//				}
//				
//				return true;
//			}
			
			public boolean visit(SimpleName name){
				String rawName = name.getIdentifier();
				ArrayList<String> tokenizedName = Tokenizer.tokenize(rawName);
				for(String token: tokenizedName){
					wordsForBody += token + " \n";
				}
				
				return true;
			}
		});
		
		return new BufferedReader(new StringReader(wordsForBody));
	}

	private BufferedReader getIdentifierFromMethodName(int i) throws java.lang.NullPointerException
	{
		
		ArrayList<String> tokenizedMethodName = 
				Tokenizer.tokenize(methods.get(i).getMd().getName().getIdentifier());
		String words = "";
		for(String token: tokenizedMethodName){
			words += token + " \n";
		}
		return new BufferedReader(new StringReader(words));
	}	

}
