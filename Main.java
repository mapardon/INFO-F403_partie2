import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


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

        String command = "java -jar Part1.jar "+sourceFile;

        Process proc = Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";

        try (FileWriter fw = new FileWriter("parser.txt")){
            while((line = reader.readLine()) != null) {
                fw.write(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        proc.waitFor();
    }
}
