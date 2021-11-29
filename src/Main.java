package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String S = "Program";
        ArrayList<ArrayList<String>> scanning;
        Map<String, Map<String, Integer>> actionTable;

        String sourceFile = "../test/easy.co";

        // TODO discomment this
        /*
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
        }*/

        scanning = runScanning(sourceFile);
        String[] scannerSequence = scanning.get(0).toArray(new String[0]);
        scannerSequence = new String[]{"BEG", "IF", "VARNAME", "GREATER", "VARNAME", "THEN", "PRINT", "LPAREN", "VARNAME", "RPAREN", "ENDIF", "END"};
        actionTable = initActionTable.initTable();

        System.out.println(Arrays.toString(scannerSequence));

        Parser parser = new Parser(S, actionTable, scannerSequence);
        if (parser.runParser()) {
            System.out.println("Program accepted by the parser");
        } else {
            System.out.println("Program rejected by the parser");
        }
    }

    /**
     * Run the Scanner
     * parameter: sourcefile : alcol file
     * return Arraylist containing an arraylist of token and another arraylist of variables
     */
    // TODO: remove jar, use the java class directly (for the final jar generation)
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
