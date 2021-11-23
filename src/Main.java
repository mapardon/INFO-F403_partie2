package src;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String S = "Program";
        ArrayList<ArrayList<String>> scanning;
        int[][] actionTable;
        int[][] matchTable;

        String sourceFile = "", option="";

        if(args.length == 0){
            System.out.println("Please enter at least a source file");
            System.exit(0);
        }

        else if (args.length == 1){
            sourceFile = args[0];
        }
        else{
            option = args[0];
            sourceFile = args[1];
        }

        scanning = runScanning(sourceFile);
        String[] listToken = scanning.get(0).toArray(new String[0]);
        String[] variables = scanning.get(1).toArray(new String[0]);

        initActionTable a = new initActionTable();
        actionTable = a.getActionTable()[0];
        matchTable = a.getActionTable()[1];

        Parser parser = new Parser(S, actionTable,matchTable, listToken);
        parser.runParser();

    }

    /**
     * Run the parser
     * parameter: sourcefile : alcol file
     * return Arraylist containing an arraylist of token and another arraylist of variables
     */
    public static ArrayList<ArrayList<String>> runScanning(String sourceFile) throws IOException, InterruptedException {
        ArrayList<String> scaner = new ArrayList<>();
        ArrayList<String> variables = new ArrayList<>();
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        String command = "java -jar Part1.jar "+sourceFile;

        Process proc = Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line;

        // read the file from the scaner and create two arrays, one whith the token and another with the variables

        while((line = reader.readLine()) != null && ! line.equals("") ) {

            String[] temp = line.split(" ");
            scaner.add(temp[temp.length-1]);

        }
        reader.readLine();
        while((line = reader.readLine()) != null){
            String[] temp = line.split("\t");
            variables.add(temp[0]);
        }

        proc.waitFor();

        res.add(scaner);
        res.add(variables);

        return res;
    }

}
