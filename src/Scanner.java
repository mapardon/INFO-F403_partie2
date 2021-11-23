package src;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Scanner {



    private String[] V;
    private String[] T;
    private String[] P;
    private String S;
    int[][] M;
    int[][] matchTable;
    String[] parser;
    private Stack<String> stack = new Stack<>();
    Map<String, Integer> tableToInt = new HashMap<String, Integer>();


    /**
     * V : finite set of variables
     * T : finite set of terminals
     * P : finite set of production rules
     * S : S ∈ V is a variable called the start symbol.
     * M : action table
     * parser : list of tokens from parser
     */
    public Scanner(String[]V, String[]T, String[]P, String S, int[][] M, int[][] matchTable, String[] parser){
        this.V = V;
        this.T = T;
        this.P = P;
        this.S = S;
        this.M = M;
        this.matchTable = matchTable;
        this.parser = parser;
        initMap();
        runScanner();
    }

    private boolean runScanner(){
        System.out.println("in scanner ! ");
        String x;
        int j = 1;
        int i;
        ArrayList<Integer> seq = new ArrayList<>();
        this.stack.push(parser[0]);
        while (! stack.isEmpty()){
            x = stack.peek();

            if (this.M[tableToInt.get(x)][tableToInt.get(parser[j])] != 0){
                i= this.M[tableToInt.get(x)][tableToInt.get(parser[j])];
                this.stack.pop();
                this.stack.push(parser[j]);
                seq.add(i);
            }

            else if(this.matchTable[tableToInt.get(x)][tableToInt.get(parser[j])] == 1){
                j++;
            }
            else if(this.matchTable[tableToInt.get(x)][tableToInt.get(parser[j])] == 2){
                return true;
            }
            else{
                return false;
            }

        }
        return true;

    }

    /**
     * init a map that match name of tokens with their place in the action table
     */
    private void initMap(){
        this.tableToInt.put("begin", 1);
        this.tableToInt.put("plus", 2);
    }

}
