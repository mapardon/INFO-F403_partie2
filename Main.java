import jdk.internal.cmm.SystemResourcePressureImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


        String sourceFile = "", option="";
        ArrayList<String> parser = new ArrayList<>();
        ArrayList<String> variables = new ArrayList<>();
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

        String command = "java -jar Part1.jar "+sourceFile;

        Process proc = Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";

        // read the file from the parser and create two arrays, one whith the token and another with the variables

        //try (FileWriter fw = new FileWriter("parser.txt")){ // pour écrire dans un fichier texte
        while((line = reader.readLine()) != null && ! line.equals("") ) {
                //fw.write(Arrays.toString(line.split(" ")) + "\n");
            String[] temp = line.split(" ");
            temp = temp[1].split("\t");

            parser.add(temp[0]);
            //System.out.println(parser.get(parser.size()-1));


        }
        reader.readLine();
        while((line = reader.readLine()) != null){
            String[] temp = line.split("\t");
            variables.add(temp[0]);
        }
        System.out.println("res: "+parser);
        System.out.println("var: "+variables);

        /*
        }
        catch (IOException e) {
            e.printStackTrace();
        }

         */
        //System.out.println(parser);


        proc.waitFor();
    }
}
