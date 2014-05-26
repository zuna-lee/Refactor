/*******************************************************************************
 *  Copyright 2007, 2009 Jorge Villalon (jorge.villalon@uai.cl)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0 
 *  	
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 *******************************************************************************/

package tml.storage.importers;

/**
 * TextImporter implements the simples importer of plain text, therefore
 * it just returns the content as it is.
 * 
 * @author Jorge Villalon
 *
 */
public class TextImporter extends AbstractImporter implements Importer {

	@Override
	public String getCleanContent(String content) {
		return content;
	}

	@Override
	protected String[] getFileExtensions() {
		String[] extensions = { "txt" };
		return extensions;
	}
}
