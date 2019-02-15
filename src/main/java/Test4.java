import DBPediaGraph.DBPediaDataGraph;
import Summarize.summarizeGraph;

public class Test4 {

    public static void main(String args[]) {



        System.out.println("Test4");
        //DBPediaOntologyGraph oGraph = new DBPediaOntologyGraph(args[0]);


        DBPediaDataGraph graph = new DBPediaDataGraph(
                "C:\\MorteZa\\KeyMiner Source Code\\Dataset\\types.nt"
                , "C:\\MorteZa\\KeyMiner Source Code\\Dataset\\test.ttl");

        summarizeGraph sum=new summarizeGraph(graph,0.5);


        //myTools.save(args[4], args[5], args[6]);



    }

}





