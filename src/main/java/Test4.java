import DBPediaGraph.DBPediaDataGraph;
import Summarize.summarizeGraph;

public class Test4 {

    public static void main(String args[]) {



        System.out.println("Test4");
        //DBPediaOntologyGraph oGraph = new DBPediaOntologyGraph(args[0]);

        String name="University";
        DBPediaDataGraph graph = new DBPediaDataGraph(
                "F:\\MorteZa\\Datasets\\Vicky\\datasets\\Merged\\Merged_Types_"+name+".ttl"
                , "F:\\MorteZa\\Datasets\\Vicky\\datasets\\Merged\\Merged_"+name+".ttl");

        summarizeGraph sum=new summarizeGraph(graph,0.3, "F:\\MorteZa\\Datasets\\Vicky\\datasets\\Merged\\Keys_"+name+".txt");


        //myTools.save(args[4], args[5], args[6]);



    }

}





