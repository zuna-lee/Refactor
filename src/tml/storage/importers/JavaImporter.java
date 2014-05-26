package tml.storage.importers;

import java.util.StringTokenizer;

public class JavaImporter extends AbstractImporter implements Importer {

	@Override
	public String getCleanContent(String content) {
		String result = "";
		StringTokenizer tokenizer = new StringTokenizer(content, "\n"); 
		
		while(tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			
			if(token.matches("import.*"))
				continue;
			else if(token.matches("package.*"))
				continue;
			else
			{
				//Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
				token = token.replaceAll("\\p{Punct}", " ");
				token = token.replaceAll("([a-z])([A-Z])","$1 $2");
				result += (token+"\n");
			}
		}
		return result;
	}

	@Override
	protected String[] getFileExtensions() {
		String[] extensions = { "java" };
		return extensions;
	}
}