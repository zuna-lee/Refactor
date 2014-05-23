package zuna.util;

import java.util.ArrayList;

public class Tokenizer {

	public static ArrayList<String> tokenize(String raw){
		ArrayList<String> token = new ArrayList<String>();
		
		for (String w : raw.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
			if(w.contains("_")){
				String newWord = "";
				if(w.charAt(w.length()-1) == '_'){
					for(int i = 0 ; i < w.length()-1 ; i++){
						newWord += w.charAt(i);
					}
				}
				token.add(newWord);
			}else{
				token.add(w);
			}
	    }
		
		return token;
	}
}
