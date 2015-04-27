package it.unimib.disco.summarization.utility;

import it.unimib.disco.summarization.datatype.Concepts;

import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

public class TypeGraph{
	
	private DirectedAcyclicGraph<String, DefaultEdge> graph;

	public TypeGraph(Concepts concepts, InputFile subClassesPath) throws Exception{
		List<String> relations = new ArrayList<String>();
		while(subClassesPath.hasNextLine()){
			relations.add(subClassesPath.nextLine());
		}
		this.graph = subTypeGraphFrom(concepts, relations);
	}
	
	public TypeGraph(Concepts concepts, List<String> subClasses) throws Exception{
		this.graph = subTypeGraphFrom(concepts, subClasses);
	}
	
	public List<String> roots(){
		ArrayList<String> roots = new ArrayList<String>();
		for(String concept : graph.vertexSet()){
			if(isRoot(concept)) roots.add(concept);
		}
		return roots;
	}
	
	public List<String> leaves(){
		ArrayList<String> leaves = new ArrayList<String>();
		for(String concept : graph.vertexSet()){
			if(isLeaf(concept)) leaves.add(concept);
		}
		return leaves;
	}
	
	public List<List<String>> pathsBetween(String leaf, String root){
		ArrayList<List<String>> paths = new ArrayList<List<String>>();
		if(graph.containsVertex(leaf) && graph.containsVertex(root)){
			inOrderTraversal(leaf, root, new ArrayList<String>(), paths);
		}
		return paths;
	}
	
	private void inOrderTraversal(String leaf, String root, List<String> currentPath, List<List<String>> paths){
		ArrayList<String> path = new ArrayList<String>(currentPath);
		path.add(leaf);
		if(leaf.equals(root)){
			paths.add(path);
		}
		for(DefaultEdge edgeToSuperType : graph.outgoingEdgesOf(leaf)){
			String superType = graph.getEdgeTarget(edgeToSuperType);
			inOrderTraversal(superType, root, path, paths);
		}
	}
	
	private boolean isLeaf(String concept){
		return graph.incomingEdgesOf(concept).isEmpty();
	}
	
	private boolean isRoot(String concept){
		return graph.outgoingEdgesOf(concept).isEmpty();
	}
	
	private DirectedAcyclicGraph<String, DefaultEdge> subTypeGraphFrom(Concepts concepts, List<String> subclassRelations) throws Exception {
		DirectedAcyclicGraph<String, DefaultEdge> typeGraph = new DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge.class);
		
		for(String concept : concepts.getConcepts().keySet()){
			typeGraph.addVertex(concept);
		}
		
		for(String line : subclassRelations){
			String[] relation = line.split("##");
			String subtype = relation[0];
			String supertype = relation[1];
			
			if(!typeGraph.containsVertex(subtype)) typeGraph.addVertex(subtype);
			if(!typeGraph.containsVertex(supertype)) typeGraph.addVertex(supertype);
			
			typeGraph.addEdge(subtype, supertype);
		}
		
		return typeGraph;
	}
}