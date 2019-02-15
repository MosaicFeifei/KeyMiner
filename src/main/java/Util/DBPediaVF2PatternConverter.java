package Util;

import Infra.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DBPediaVF2PatternConverter {

    private Graph<DataNode, RelationshipEdge> pattern;
    //private  HashMap<String, OGKQueryNode> nodeMap;
    //OGraph ontology;

    public DBPediaVF2PatternConverter(){

    }

    private void convertQueryToPattern() {



    }

    public Graph<DataNode, RelationshipEdge> getPattern() {
        return pattern;
    }

}
