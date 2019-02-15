package Summarize;

import DBPediaGraph.DBPediaDataGraph;
import Infra.DataNode;
import Infra.NodeType;
import Infra.RelationshipEdge;
import Infra.candidateKey;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class summarizeGraph {

    private DBPediaDataGraph dataGraph;


    private HashMap<String, HashSet<Neighbor>> summarize = new HashMap<>();
    private HashMap<String, Integer> instanceCount = new HashMap<>();
    private HashMap<String, Integer> alreadyVisited = new HashMap<>();

    private HashMap<String,HashSet<candidateKey>> CandidateKeys=new HashMap<>();

    //public HashMap<String, HashMap<String, ArrayList<Classes>>> updateSummarized = new HashMap<>();
    //ArrayList<Classes> allClasses=new ArrayList<>();
    //public HashMap<String, ArrayList<String>> superTypes = new HashMap<String, ArrayList<String>>();


    public summarizeGraph(DBPediaDataGraph dataGraph, double minimumCoverage)
    {
        this.dataGraph=dataGraph;
        doSummarization();
        saveData("C:\\Users\\zahra\\Desktop\\ttttt.txt",minimumCoverage);

    }

    public HashMap<String,HashSet<candidateKey>> getCandidateKeys()
    {
        return CandidateKeys;
    }

    private void doSummarization() {

        for (DataNode node : dataGraph.getDataGraph().vertexSet())
        {

            alreadyVisited.clear();
            for (String sourceType : node.getTypes()) {
                if (!instanceCount.containsKey(sourceType))
                    instanceCount.put(sourceType, 1);
                else {
                    int cc = instanceCount.get(sourceType) + 1;
                    instanceCount.put(sourceType, cc);
                }
            }

            Set<RelationshipEdge> relationshipEdges = dataGraph.getDataGraph().edgesOf(node);
            for (RelationshipEdge edge : relationshipEdges)
            {
                DataNode dest=dataGraph.getDataGraph().getEdgeTarget(edge);
                if(dest.getNodeType()== NodeType.Literal)
                {
                    for (String sourceType : node.getTypes()) {
                        refineSummarizeGraph(node, sourceType, "*", edge.getLabel());
                    }
                }
                else if(dest.getNodeType()== NodeType.Entity)
                {
                    for (String sourceType : node.getTypes()) {
                        for (String destType : dest.getTypes()) {
                            refineSummarizeGraph(node, sourceType, destType, edge.getLabel());
                        }
                    }
                }
            }
        }
    }

    private void refineSummarizeGraph(DataNode source, String sourceType, String destType,
                                      String ont) {
        if (!summarize.containsKey(sourceType)) { // Key not exist in the graph
            addNewNeighbor(sourceType, destType, ont);
        } else {
            Neighbor neighbor = findNeighbor(summarize.get(sourceType), ont, destType);
            if (neighbor == null) {// the key exist in the graph but there is no such a neighbor
                addNewNeighbor(sourceType, destType, ont);
            }
            else if (!alreadyVisited.containsKey(ont + destType)) {
                int id = neighbor.increment(String.valueOf(source.getNodeName().hashCode()));
                alreadyVisited.put(ont + destType, id);
            }
            //else if (neighbor != null && alreadyVisited.containsKey(ont + destType)) {
            // neighbor.incrementInstance(alreadyVisited.get(ont + destType));
            //}
        }
    }

    private void addNewNeighbor(String sourceType, String destType, String ont) {
        Neighbor temp = new Neighbor(ont, destType);
        int id = temp.increment("");
        if (!summarize.containsKey(sourceType))
            summarize.put(sourceType, new HashSet<Neighbor>());
        summarize.get(sourceType).add(temp);
        alreadyVisited.put(ont + destType, id);
    }

    private Neighbor findNeighbor(HashSet<Neighbor> input, String ont, String type) {
        for (Neighbor neighbor : input) {
            if (neighbor.getType().equals(type)
                    && neighbor.getOnt().equals(ont))
                return neighbor;
        }
        return null;
    }

    private void saveData(String savingPath, double threshold) {
        try {

            FileWriter writer = new FileWriter(savingPath);
            writer.write("Number of types: " + summarize.keySet().size() + "\n");
            String line = "";
            HashSet<String> distinctTypes=new HashSet<>();
            for (String key : summarize.keySet()) {
                int cc = -1;
                if (instanceCount.containsKey(key))
                    cc = instanceCount.get(key);
                boolean written=false;
                int Max=-1;
                candidateKey atLeastOneKey=new candidateKey("","");
                for (Neighbor neighbor : summarize.get(key)) {
                    if(((double)neighbor.getCount()/(double)cc)>threshold)
                    {
                        line = key + "\t" + neighbor.getOnt() + "\t"
                                + neighbor.getType() + "\t" + neighbor.getCount()
                                + "\t" + cc;
                        writer.write(line + "\n");
                        written=true;
                        if(!distinctTypes.contains(key))
                            distinctTypes.add(key);

                        candidateKey cKey=new candidateKey(neighbor.getOnt(),neighbor.getType());
                        if(!CandidateKeys.containsKey(key))
                            CandidateKeys.put(key,new HashSet<candidateKey>());
                        CandidateKeys.get(key).add(cKey);
                    }
                    else if(!written && cc>Max)
                    {
                    	Max=cc;
                    	line = key + "\t" + neighbor.getOnt() + "\t"
                    			+ neighbor.getType() + "\t" + neighbor.getCount()
                    			+ "\t" + cc;
                    	atLeastOneKey.setPredicate(neighbor.getOnt());
                    	atLeastOneKey.setObject(neighbor.getType());
                    }
                }
                if(!written) {
                    writer.write(line + "\n");
                    if(!CandidateKeys.containsKey(key))
                        CandidateKeys.put(key,new HashSet<candidateKey>());
                    CandidateKeys.get(key).add(atLeastOneKey);
                }
            }
            writer.write("Number of distinct types: " + distinctTypes.size() + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private class Neighbor {

        private String type;
        private String ontology;
        private String signiture;
        private HashSet<String> sources=new HashSet<>();
        private int count=0;
        //private int instanceCount=0;
        //private HashMap<Integer, Integer> instances=new HashMap<Integer, Integer>();

        public Neighbor(String ont, String type)
        {
            this.type=type;
            this.ontology=ont;
            signiture=type+ont;
        }

        public String getOnt()
        {
            return ontology;
        }

        public String getType()
        {
            return type;
        }

        public String getSigniture()
        {
            return signiture;
        }

        public int increment(String source)
        {
            if(!sources.contains(source)) {
                count++;
                sources.add(source);
            }
            //instances.put(count, 1);
            return count;
        }

        public void incrementInstance(int countID)
        {
            //TODO
            // There is an error hear! take care of NULLException
            //instances.put(countID, instances.get(countID)+1);
        }

        public void incrementInstance()
        {
            //instanceCount++;
        }

        public int getCount()
        {
            return count;
        }
    }


}
