package it.unimib.disco.summarization.export;

import it.unimib.disco.summarization.dataset.FileSystemConnector;
import it.unimib.disco.summarization.dataset.TextInput;
import it.unimib.disco.summarization.ontology.RDFResource;

import java.io.File;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class IndexResources
{
	public static void main (String[] args) throws Exception
	{
		Events.summarization();
		
		try{
			String host = args[0];
			String port = args[1];
			String pathFile = args[2];
			String dataset = args[3];
			String type = args[4];
			
			String serverUrl = "http://"+host+":"+port+"/solr/indexing";
			HttpSolrServer client = new HttpSolrServer(serverUrl);
			
			TextInput input = new TextInput(new FileSystemConnector(new File(pathFile)));
			
			while(input.hasNextLine()){
				String[] line = input.nextLine().split("##");
				String resource = line[0];
				String localName = new RDFResource(resource).localName();
				Long occurrences = Long.parseLong(line[1]);
				String subtype = line[2];
				
				SolrInputDocument doc = new SolrInputDocument();
				doc.setField("URI", resource);
				doc.setField("type", dataset);
				doc.setField("dataset", type);
				doc.setField("subtype", subtype);
				doc.setField("fullTextSearchField", localName);
				doc.setField("occurrence", occurrences);
				client.add(doc);
			}
			
			client.commit(true, true);
		}
		catch(Exception e){
			Events.summarization().error("", e);
		}
	}
}
