package it.unimib.disco.summarization.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class InternalExternalDatatypeProperty
{
	public static void main(String[] args) throws Exception
	{
		Events.summarization();
				
		try{
			String pathFile = args[0];
			String dataset = args[1];
			String payLevelDomain = args[2];
			datatypePropertiesInternalExternal(pathFile, dataset , payLevelDomain);
		}
		catch(Exception e){
			Events.summarization().error("", e);
		}
	}
	
	private static void datatypePropertiesInternalExternal(String pathFile, String dataset, String payLevelDomain) throws Exception
	{
		String fileDatatypePropertiesPath = pathFile;
		BufferedReader brDatatypeProperties = new BufferedReader(new FileReader(fileDatatypePropertiesPath));
		
		FileWriter fwDatatypeProperties = new FileWriter("../data/summaries/"+dataset+"/patterns/count-datatype-properties-new.txt");
		BufferedWriter bwDatatypeProperties = new BufferedWriter(fwDatatypeProperties);
		
		boolean trovatoPrimoCancelletto = false;
		String lineRead = null;
		String datatypeProperty = "";
		String numberOfInstances = "";
		String typeOfDatatypeProperty = "";
		lineRead = brDatatypeProperties.readLine();
		
		while (lineRead != null)
		{
			for (int i = 0; i < lineRead.length(); i++)
			{
				if (trovatoPrimoCancelletto == false)
				{					
					if ((lineRead.charAt(i) != '#') && (lineRead.charAt(i+1) != '#'))
					{
						datatypeProperty += lineRead.charAt(i);
					}
					if ((lineRead.charAt(i) != '#') && (lineRead.charAt(i+1) == '#'))
					{
						datatypeProperty += lineRead.charAt(i);
					}
					if ((lineRead.charAt(i) == '#') && (lineRead.charAt(i+1) != '#'))
					{
						datatypeProperty += lineRead.charAt(i);
					}
					if ((lineRead.charAt(i) == '#') && (lineRead.charAt(i+1) == '#'))
					{
						datatypeProperty += "";
						trovatoPrimoCancelletto = true;
					}
				}
				
				if (trovatoPrimoCancelletto == true)
				{
					if (lineRead.charAt(i) != '#')
					{
						numberOfInstances += lineRead.charAt(i);
					}
				}
			}
						
			if ((datatypeProperty.contains("wikidata")) && (datatypeProperty.contains(payLevelDomain)))
			{
				typeOfDatatypeProperty = "external";
			}
			else
			{
				if ((!(datatypeProperty.contains("wikidata"))) && (datatypeProperty.contains(payLevelDomain)))
				{
					typeOfDatatypeProperty = "internal";
				}
				else
				{
					if (!(datatypeProperty.contains(payLevelDomain)))
					{
						typeOfDatatypeProperty = "external";
					}
				}
			}
			
			bwDatatypeProperties.write(datatypeProperty);
			bwDatatypeProperties.write("##");
			bwDatatypeProperties.write(numberOfInstances);
			bwDatatypeProperties.write("##");
			bwDatatypeProperties.write(typeOfDatatypeProperty);
			bwDatatypeProperties.write("\n");
			
			datatypeProperty = "";
			numberOfInstances = "";
			typeOfDatatypeProperty = "";
			trovatoPrimoCancelletto = false;
			
			lineRead = brDatatypeProperties.readLine();
		}
		
		brDatatypeProperties.close();
		bwDatatypeProperties.close();
		fwDatatypeProperties.close();
	}
}