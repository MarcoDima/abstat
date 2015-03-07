package it.unimib.disco.summarization.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.unimib.disco.summarization.output.WriteConceptsToRDF;

import java.io.File;

import org.junit.Test;

public class WriteConceptsToRDFTest extends UnitTest{
	
	@Test
	public void shouldExportAsRDF() throws Exception {
		
		File inputFile = temporary.newFile("http://dbpedia.org/ontology/Artist##24##0.367985");
		File outputFile = temporary.newFile();
		
		WriteConceptsToRDF.main(new String[]{
				inputFile.getAbsolutePath(),
				outputFile.getAbsolutePath()
		});
		
		assertThat(outputFile.exists(), is(true));
	}
}
