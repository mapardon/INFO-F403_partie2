
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String[] V = {"PLUS","MINUS","AAAA"};
        String[] T = {"a","b","c"};
        String[] P = {"règles", "r2", "r3", "r4"};
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

        //actionTable = runPython();

        //System.out.println(Arrays.toString(actionTable));

        //System.out.println(Arrays.toString(listToken));

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

        //try (FileWriter fw = new FileWriter("scaner.txt")){ // pour écrire dans un fichier texte
        while((line = reader.readLine()) != null && ! line.equals("") ) {
            //fw.write(Arrays.toString(line.split(" ")) + "\n");
            String[] temp = line.split(" ");
            //temp = temp[1].split("\t");
            scaner.add(temp[temp.length-1]);
            //System.out.println(scaner.get(scaner.size()-1));


        }
        reader.readLine();
        while((line = reader.readLine()) != null){
            String[] temp = line.split("\t");
            variables.add(temp[0]);
        }
        //System.out.println("res: "+scaner);
        //System.out.println("var: "+variables);

        /*
        }
        catch (IOException e) {
            e.printStackTrace();
        }

         */
        //System.out.println(scaner);


        proc.waitFor();

        res.add(scaner);
        res.add(variables);

        return res;
    }

    /**
     * Run the python script building the action table
     * Return an arraylist of string's array containing the action table
     */
    public static ArrayList<String[]> runPython() throws IOException, InterruptedException {

        String command = "python3 main.py";

        Process proc = Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line;

        // read the file from the parser and create two arrays, one whith the token and another with the variables

        FileWriter fw = new FileWriter("parser.txt"); // pour écrire dans un fichier texte
        ArrayList<String[]> parser = new ArrayList<>();
        while((line = reader.readLine()) != null && ! line.equals("") ) {
            //fw.write(Arrays.toString(line.split(" ")) + "\n");
            //fw.write(line);
            //System.out.println(line);
            //String[] temp = line.split(" ");
            //temp = temp[1].split("\t");
            //String[] temp = line.split(" ");
            //temp.filter(x -> !x.isEmpty()).toArray(String[]::new);
            parser.add(line.split((" ")));


        }
        /*
        for (String[] strings : parser) {
            System.out.println(Arrays.toString(strings));
        }

         */

        proc.waitFor();
        return parser;
    }
}
