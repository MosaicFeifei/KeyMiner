package tools;

import java.io.FileWriter;
import java.io.IOException;

public class myTools {


    private static StringBuilder sb=new StringBuilder();
    private static StringBuilder sb2=new StringBuilder();

    public static void append(String text)
    {
        sb.append(text + "\n");
    }
    public static void append2(String text)
    {
        sb2.append(text);
    }

    public static void save(String Key, String beta, String alpha)
    {
        FileWriter writer = null;
        //try {
        //    writer = new FileWriter("report.txt");
        //    writer.write(sb.toString());
        //    writer.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        try {
            writer = new FileWriter("report_" +Key +"_"+beta +"_" +alpha+".txt");
            writer.write(sb2.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
