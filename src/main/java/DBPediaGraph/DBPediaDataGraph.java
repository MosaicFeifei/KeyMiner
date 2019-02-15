package DBPediaGraph;

import Infra.DataGraphBase;
import Infra.DataNode;
import Infra.NodeType;
import Infra.RelationshipEdge;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.rdf.model.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DBPediaDataGraph extends DataGraphBase {


    public DBPediaDataGraph(String nodeTypesFilePath, String dataGraphFilePath) {

        super();
        loadNodeMap(nodeTypesFilePath);
        addAllVertex();
        loadGraph(dataGraphFilePath);

    }

    private void loadNodeMap(String nodeTypesFilePath) {

        if (nodeTypesFilePath == null || nodeTypesFilePath.length() == 0) {
            System.out.println("No Input Node Types File Path!");
            return;
        }
        System.out.println("Start Loading DBPedia Node Map...");

        Model model = ModelFactory.createDefaultModel();
        System.out.println("Loading Node Types...");

        //MorteZa
        Path input= Paths.get(nodeTypesFilePath);
        model.read(input.toUri().toString());
        //MorteZa

        StmtIterator typeTriples = model.listStatements();

        while (typeTriples.hasNext()) {

            Statement stmt = typeTriples.nextStatement();
            String subject = stmt.getSubject().getURI();

            if (subject.length() > 28) {
                subject = subject.substring(28);
            }

            DataNode dataNode;
            String object = stmt.getObject().asResource().getLocalName();

            int nodeId = subject.hashCode();

            if (!nodeMap.containsKey(nodeId)) {
                dataNode = new DataNode(subject, NodeType.Entity, object);
                dataNode.addTypes(object);
                dataNode.setType(object);
                nodeMap.put(nodeId, dataNode);

            } else {

                dataNode = nodeMap.get(nodeId);
                dataNode.addTypes(object);


            }

        }

        System.out.println("Done Loading DBPedia Node Map!!!");
        System.out.println("DBPedia NodesMap Size: " + nodeMap.size());


    }

    private void loadGraph(String dataGraphFilePath) {

        if (dataGraphFilePath == null || dataGraphFilePath.length() == 0) {
            System.out.println("No Input Graph Data File Path!");
            return;
        }

        Model model = ModelFactory.createDefaultModel();
        System.out.println("Loading DBPedia Graph...");

        //MorteZa
        Path input= Paths.get(dataGraphFilePath);
        model.read(input.toUri().toString());
        //MorteZa

        StmtIterator dataTriples = model.listStatements();

        int loseCount = 0;
        int loopCount = 0;

        while (dataTriples.hasNext()) {

            Statement stmt = dataTriples.nextStatement();
            String subject = stmt.getSubject().getURI();

            if (subject.length() > 28) {
                subject = subject.substring(28);
            }

            String predicate = stmt.getPredicate().getLocalName();
            RDFNode object = stmt.getObject();
            String objectString;

            try {

                if (object.isLiteral()) {
                    objectString = object.asLiteral().getString();
                } else {
                    objectString = object.asResource().getLocalName();
                }

            } catch (DatatypeFormatException e) {
                System.out.println("Invalid DataType Skipped!");
                e.printStackTrace();
                continue;
            }

            int subjectNodeId = subject.hashCode();
            int objectNodeId = objectString.hashCode();

            if (!nodeMap.containsKey(subjectNodeId)
                    || (object.isURIResource() && !nodeMap.containsKey(objectNodeId))) {
                loseCount++;
                continue;
            }

            DataNode currentSubject = nodeMap.get(subjectNodeId);
            DataNode currentObject;

            if (object.isURIResource()) {

                if (subjectNodeId == objectNodeId) {
                    //System.out.println("Loop Founded!");
                    loopCount++;
                    continue;
                }
                currentObject = nodeMap.get(objectNodeId);

            } else if (object.isLiteral()) {

                if (!literalNodeMap.containsKey(objectNodeId)) {
                    DataNode newLiteralNode = new DataNode(objectString, NodeType.Literal, "Literal");
                    literalNodeMap.put(objectNodeId, newLiteralNode);
                    this.dataGraph.addVertex(newLiteralNode);
                }

                currentObject = literalNodeMap.get(objectNodeId);

            } else {

                //System.out.println("Skip this triple since invalid node type!");
                loseCount++;
                continue;

            }

            dataGraph.addEdge(currentSubject, currentObject, new RelationshipEdge(predicate));


        }

        System.out.println(loopCount + "Loops!");
        System.out.println(loseCount + "Loses!");
        System.out.println("Number of Edges: " + dataGraph.edgeSet().size());
        System.out.println("Number of Nodes: " + dataGraph.vertexSet().size());
        System.out.println("Done Loading DBPedia Graph!!!");

    }

}
