package src;

import java.util.*;

/**
 * Class implementing a parser of alCOl language
 */
public class Parser {

    private final String firstVariable;
    Map<String, Map<String, Integer>> actionTable;
    String[] scannerSequence;
    private final Stack<String> stack = new Stack<>();
    Map<Integer, String[]> grammar = new HashMap<>();


    /**
     * startSymbol : first variable of any program
     * actionTable : action table without the matchTable (see below)
     * matchTable : part of the action table containing only terminals
     * scannerSequence : list of tokens built during scanning
     */
    public Parser(String startSymbol, Map<String, Map<String, Integer>> actionTable, String[] scannerSequence) {

        this.firstVariable = startSymbol;
        this.actionTable = actionTable;
        this.scannerSequence = scannerSequence;
        initGrammar();
    }

    /**
     * Run the parser
     * @return boolean evaluated to false if an error has been detected during the parsing
     */
    public boolean runParser() {

        String topOfStack;
        int j = 0;  // index of progression in the scanner sequence
        int ruleToApply;
        StringBuilder rulesApplied = new StringBuilder();
        stack.push(this.firstVariable);

        while (!stack.isEmpty()) {
            topOfStack = stack.peek();

            System.out.print("");
            
            // ToS is a terminal, check if there is a match
            if(actionTable.get(topOfStack).get(scannerSequence[j]) == -1) {
                j++;
                stack.pop();
            }

            // ToS is the end character, check if there is a match
            else if(actionTable.get(topOfStack).get(scannerSequence[j]) == -2) {
                rulesApplied.deleteCharAt(0);
                System.out.println(rulesApplied);
                return true;
            }

            // ToS is a variable, check if derivable in this situation
            else if (actionTable.get(topOfStack).get(scannerSequence[j]) != 0) {
                ruleToApply= actionTable.get(topOfStack).get(scannerSequence[j]);
                stack.pop();

                for (int k = grammar.get(ruleToApply).length - 1; k > -1 ; --k) {
                    if (!grammar.get(ruleToApply)[k].equals("")){
                        stack.push(grammar.get(ruleToApply)[k]);
                    }
                }
                rulesApplied.append(" ").append(ruleToApply);
            }

            // None of above cases, error detected
            else {
                return false;
            }
        }
        return true;
    }

    /**
     * Init a map that match rule number with the sentential form of its derivation.
     */
    private void initGrammar() {
        grammar.put(1, new String[]{"BEG", "Code", "END"});
        grammar.put(2, new String[]{"InstList"});
        grammar.put(3, new String[]{""});
        grammar.put(4, new String[]{"Instruction", "InstListEnd"});
        grammar.put(5, new String[]{"SEMICOLON","InstList"});
        grammar.put(6, new String[]{""});
        grammar.put(7, new String[]{"Assign"});
        grammar.put(8, new String[]{"If"});
        grammar.put(9, new String[]{"While"});
        grammar.put(10, new String[]{"For"});
        grammar.put(11, new String[]{"Print"});
        grammar.put(12, new String[]{"Read"});
        grammar.put(13, new String[]{"VARNAME","ASSIGN","ExprArith" });
        grammar.put(14, new String[]{"Prod", "ExprArith'"});
        grammar.put(15, new String[]{"PLUS", "Prod", "ExprArith'"});
        grammar.put(16, new String[]{"MINUS", "Prod", "ExprArith'"});
        grammar.put(17, new String[]{""});
        grammar.put(18, new String[]{"Atom","Prod'"});
        grammar.put(19, new String[]{"TIMES", "Atom", "Prod'"});
        grammar.put(20, new String[]{"DIVIDE", "Atom", "Prod'"});
        grammar.put(21, new String[]{""});
        grammar.put(22, new String[]{"MINUS", "Atom"});
        grammar.put(23, new String[]{"NUMBER"});
        grammar.put(24, new String[]{"VARNAME"});
        grammar.put(25, new String[]{"LPAREN","ExprArith", "RPAREN"});
        grammar.put(26, new String[]{"IF", "Cond", "THEN", "Code", "ElseClause", "ENDIF" });
        grammar.put(27, new String[]{"ELSE","Code" });
        grammar.put(28, new String[]{""});
        grammar.put(29, new String[]{"NOT", "Cond"});
        grammar.put(30, new String[]{"SimpleCond"});
        grammar.put(31, new String[]{"ExprArith", "Comp", "ExprArith"});
        grammar.put(32, new String[]{"EQUAL"});
        grammar.put(33, new String[]{"GREATER"});
        grammar.put(34, new String[]{"SMALLER"});
        grammar.put(35, new String[]{"WHILE", "Cond", "DO","Code", "ENDWHILE"});
        grammar.put(36, new String[]{"FOR","VARNAME", "FROM", "ExprArith", "BY", "ExprArith", "TO", "ExprArith", "DO", "Code", "ENDFOR" });
        grammar.put(37, new String[]{"PRINT", "LPAREN", "VARNAME", "RPAREN"});
        grammar.put(38, new String[]{"READ", "LPAREN", "VARNAME", "RPAREN"});
    }
}
