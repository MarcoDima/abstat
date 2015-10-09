package it.unimib.disco.summarization.web.tests;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.unimib.disco.summarization.web.ConceptsApi;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ConceptsApiTest {
	
	@Test
	public void getAutocompleteSuggestion() throws Exception {
		RequestTestDouble request = new RequestTestDouble()
											.withParameter("q", "ci")
											.withParameter("dataset", "system-test");
		
		assertThat(IOUtils.toString(new ConceptsApi(new ConnectorTestDouble()).getResponseFromConnector(request)), containsString("http://dbpedia.org/ontology/City"));
	}
}
