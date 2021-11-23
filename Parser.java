import java.util.*;

/**
 * Class implementing a parser of alCOl language
 */
public class Parser {

    private String S;
    int[][] M;
    int[][] matchTable;
    String[] parser;
    private Stack<String> stack = new Stack<>();
    Map<String, Integer> derivToInt = new HashMap<>();
    Map<String, Integer> varToInt = new HashMap<>();
    Map<Integer, String[]> gram = new HashMap<>();


    /**
     * V : finite set of variables
     * T : finite set of terminals
     * P : finite set of production rules
     * S : S ∈ V is a variable called the start symbol.
     * M : action table
     * parser : list of tokens from parser
     */
    public Parser(String S, int[][] M, int[][] matchTable, String[] parser){

        this.S = S;
        this.M = M;
        this.matchTable = matchTable;
        this.parser = parser;
        initMap();
    }

    /**
     * run the parser
     * @return a boolean: true if the code is correct, false if not
     */
    public boolean runParser(){

        String x;
        int j = 0;
        int i;
        StringBuilder seq= new StringBuilder();
        this.stack.push(this.S);
        while (! stack.isEmpty()){
            x = stack.peek();

            if (varToInt.containsKey(x) && this.M[varToInt.get(x)][derivToInt.get(parser[j])] != 0){
                i= this.M[varToInt.get(x)][derivToInt.get(parser[j])];
                this.stack.pop();

                for (int k=this.gram.get(i).length-1; k>-1 ; --k){
                    if (!this.gram.get(i)[k].equals("")){
                        this.stack.push(this.gram.get(i)[k]);
                    }
                }
                seq.append(" ").append(i);
            }

            else if(derivToInt.containsKey(x) && this.matchTable[derivToInt.get(x)][derivToInt.get(parser[j])] == 1){
                j++;
                this.stack.pop();
            }
            else if(derivToInt.containsKey(x) && this.matchTable[derivToInt.get(x)][derivToInt.get(parser[j])]  == 2){
                seq.deleteCharAt(0);
                System.out.println(seq);
                return true;
            }
            else{
                return false;
            }

        }
        return true;

    }

    /**
     * init 3 maps that match name of tokens with their column's number in the action table, rules number with
     * their derivations, and variables with their column's number
     */
    private void initMap(){
        this.derivToInt.put("VARNAME", 0);
        this.derivToInt.put("NUMBER", 1);
        this.derivToInt.put("BEG", 2);
        this.derivToInt.put("END", 3);
        this.derivToInt.put("SEMICOLON", 4);
        this.derivToInt.put("ASSIGN", 5);
        this.derivToInt.put("LPAREN", 6);
        this.derivToInt.put("RPAREN", 7);
        this.derivToInt.put("MINUS", 8);
        this.derivToInt.put("PLUS", 9);
        this.derivToInt.put("TIMES", 10);
        this.derivToInt.put("DIVIDE", 11);
        this.derivToInt.put("IF", 12);
        this.derivToInt.put("THEN", 13);
        this.derivToInt.put("ENDIF", 14);
        this.derivToInt.put("ELSE", 15);
        this.derivToInt.put("NOT", 16);
        this.derivToInt.put("EQUAL", 17);
        this.derivToInt.put("GREATER", 18);
        this.derivToInt.put("SMALLER", 19);
        this.derivToInt.put("WHILE", 20);
        this.derivToInt.put("DO", 21);
        this.derivToInt.put("ENDWHILE", 22);
        this.derivToInt.put("FOR", 23);
        this.derivToInt.put("FROM", 24);
        this.derivToInt.put("BY", 25);
        this.derivToInt.put("TO", 26);
        this.derivToInt.put("ENDFOR", 27);
        this.derivToInt.put("PRINT", 28);
        this.derivToInt.put("READ", 29);
        this.derivToInt.put("END_OF_STREAM", 30);

        this.varToInt.put("Program",0);
        this.varToInt.put("Code",1);
        this.varToInt.put("InstList",2);
        this.varToInt.put("InstListEnd",3);
        this.varToInt.put("Instruction",4);
        this.varToInt.put("Assign",5);
        this.varToInt.put("ExprArith",6);
        this.varToInt.put("ExprArith'",7);
        this.varToInt.put("Prod",8);
        this.varToInt.put("Prod'",9);
        this.varToInt.put("Atom",10);
        this.varToInt.put("If",11);
        this.varToInt.put("ElseClause",12);
        this.varToInt.put("Cond",13);
        this.varToInt.put("SimpleCond",14);
        this.varToInt.put("Comp",15);
        this.varToInt.put("While",16);
        this.varToInt.put("For",17);
        this.varToInt.put("Print",18);
        this.varToInt.put("Read",19);

        this.gram.put(1, new String[]{"BEG", "Code", "END"});
        this.gram.put(2, new String[]{""});
        this.gram.put(3, new String[]{"InstList"});
        this.gram.put(4, new String[]{"Instruction", "InstListEnd"});
        this.gram.put(5, new String[]{""});
        this.gram.put(6, new String[]{"SEMICOLON","InstList"});
        this.gram.put(7, new String[]{"Assign"});
        this.gram.put(8, new String[]{"If"});
        this.gram.put(9, new String[]{"While"});
        this.gram.put(10, new String[]{"For"});
        this.gram.put(11, new String[]{"Print"});
        this.gram.put(12, new String[]{"Read"});
        this.gram.put(13, new String[]{"VARNAME","ASSIGN","ExprArith" });
        this.gram.put(14, new String[]{"Prod", "ExprArith'"});
        this.gram.put(15, new String[]{"PLUS", "Prod", "ExprArith'"});
        this.gram.put(16, new String[]{"MINUS", "Prod", "ExprArith'"});
        this.gram.put(17, new String[]{"" });
        this.gram.put(18, new String[]{"Atom","Prod'"});
        this.gram.put(19, new String[]{"TIMES", "Atom", "Prod'"});
        this.gram.put(20, new String[]{"DIVIDE", "Atom", "Prod'"});
        this.gram.put(21, new String[]{""});
        this.gram.put(22, new String[]{"MINUS", "Atom"});
        this.gram.put(23, new String[]{"NUMBER"});
        this.gram.put(24, new String[]{"VARNAME"});
        this.gram.put(25, new String[]{"LPAREN","ExprArith", "RPAREN"});
        this.gram.put(26, new String[]{"IF", "Cond","THEN","Code", "ElseClause", "ENDIF" });
        this.gram.put(27, new String[]{"ELSE","Code" });
        this.gram.put(28, new String[]{""});
        this.gram.put(29, new String[]{"NOT", "Cond"});
        this.gram.put(30, new String[]{"SimpleCond"});
        this.gram.put(31, new String[]{"ExprArith", "Comp", "ExprArith"});
        this.gram.put(32, new String[]{"EQUAL"});
        this.gram.put(33, new String[]{"GREATER"});
        this.gram.put(34, new String[]{"SMALLER"});
        this.gram.put(35, new String[]{"WHILE", "Cond", "DO","Code", "ENDWHILE"});
        this.gram.put(36, new String[]{"FOR","VARNAME", "FROM", "ExprArith", "BY", "ExprArith", "TO", "ExprArith", "DO", "Code", "ENDFOR" });
        this.gram.put(37, new String[]{"PRINT", "LPAREN", "VARNAME", "RPAREN"});
        this.gram.put(38, new String[]{"READ", "LPAREN", "VARNAME", "RPAREN"});
    }
}
