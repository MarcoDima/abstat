package it.unimib.disco.summarization.tests;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import it.unimib.disco.summarization.datatype.Concept;
import it.unimib.disco.summarization.extraction.ConceptExtractor;
import it.unimib.disco.summarization.relation.OntologySubclassOfExtractor;
import it.unimib.disco.summarization.utility.ComputeLongestPathHierarchy;
import it.unimib.disco.summarization.utility.LongestPath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntClass;

public class LongestPathTest extends TestWithTemporaryData{

	@Test
	public void shouldHandlesAnEmptyOntology() throws Exception {
		
		ToyOntology ontology = new ToyOntology().owl();
		Concept concepts = getConceptsFrom(ontology);
		File subClasses = writeSubClassRelationsFrom(ontology);
		
		File savedPaths = longestPaths(concepts, subClasses);
		
		assertThat(FileUtils.readLines(savedPaths), empty());
	}
	
	@Test
	@Ignore
	public void shouldComputeTheSamePathsThanLegacyCode() throws Exception {
		
		String root = "http://root";
		String person = "http://person";
		String agent = "http://agent";
		String player = "http://player";
		String place = "http://place";
		String building = "http://building";
		
		ToyOntology ontology = new ToyOntology()
				.owl()
				.definingConcept(root)
				.definingConcept(person)
					.aSubconceptOf(agent)
				.definingConcept(player)
					.aSubconceptOf(person)
				.definingConcept(place)
					.aSubconceptOf(root)
				.definingConcept(building)
					.aSubconceptOf(root)
					.aSubconceptOf(place);
		
		Concept concepts = getConceptsFrom(ontology);
		File subClasses = writeSubClassRelationsFrom(ontology);
		
		File legacyResults = temporary.newFile();
		new ComputeLongestPathHierarchy(concepts, subClasses.getAbsolutePath()).computeLonghestPathHierarchy(legacyResults.getAbsolutePath());
		
		assertAreEquivalent(legacyResults, longestPaths(concepts, subClasses));
	}

	private File longestPaths(Concept concepts, File subClasses) throws Exception {
		File results = temporary.newFile();
		new LongestPath(concepts, subClasses.getAbsolutePath()).compute(results.getAbsolutePath());
		return results;
	}
	
	private void assertAreEquivalent(File legacyResults, File results) throws Exception {
		Collection<String> legacyPaths = FileUtils.readLines(legacyResults);
		Collection<String> paths = FileUtils.readLines(results);
		
		assertThat(paths, hasSize(legacyPaths.size()));
		assertThat(paths, containsInAnyOrder(legacyPaths.toArray()));
	}

	private Concept getConceptsFrom(ToyOntology ontology){
		
		ConceptExtractor conceptExtractor = new ConceptExtractor();
		conceptExtractor.setConcepts(ontology.build());
		
		Concept concepts = new Concept();
		concepts.setConcepts(conceptExtractor.getConcepts());
		concepts.setExtractedConcepts(conceptExtractor.getExtractedConcepts());
		concepts.setObtainedBy(conceptExtractor.getObtainedBy());
		
		return concepts;
	}
	
	private File writeSubClassRelationsFrom(ToyOntology ontology) throws Exception{
		
		OntologySubclassOfExtractor extractor = new OntologySubclassOfExtractor();
		extractor.setConceptsSubclassOf(getConceptsFrom(ontology), ontology.build());
		
		List<String> result = new ArrayList<String>();
		for(List<OntClass> subClasses : extractor.getConceptsSubclassOf().getConceptsSubclassOf()){
			result.add(subClasses.get(0) + "##" + subClasses.get(1));
		}
		
		return temporary.newFile(StringUtils.join(result, "\n"));
	}
}
