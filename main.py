
# TODO: Tokenize

GRAMMAR = {
    "<Program>": [["BEG", "<Code>", "END"]],
    "<Code>": [["epsilon"],
               ["<InstList>"]],
    "<InstList>": [["<Instruction>"],
                   ["<Instruction>", "SEMICOLON", "<InstList>"]],
    "<Instruction>": [["<Assign>"],
                      ["<If>"],
                      ["<While>"],
                      ["<For>"],
                      ["<Print>"],
                      ["<Read>"]],
    "<Assign>": [["VARNAME" "ASSIGN" "<ExprArith>"]],
    "<ExprArith>": [["<Prod>", "<ExprArith'>"]],
    "<ExprArith'>": [["PLUS", "<Prod>", "<ExprArith'>"],
                     ["MINUS", "<Prod>", "<ExprArith'>"],
                     ["epsilon"]],
    "<Prod>": [["<Atom>", "<Prod'>"]],
    "<Prod'>": [["TIMES", "<Atom>", "<Prod'>"],
                ["DIVIDE", "<Atom>", "<Prod'>"],
                ["epsilon"]],
    "<Atom>": [["MINUS", "<Atom>"],
               ["NUMBER"],
               ["VARNAME"],
               ["LPAREN", "<ExprArith>", "RPAREN"]],
    "<If>": [["IF", "<Cond>", "THEN", "<Code>", "<ElseClause>", "ENDIF"]],
    "<ElseClause>": [["ELSE", "<Code>"],
                     ["epsilon"]],
    "<Cond>": [["NOT", "<Cond>"],
               ["<SimpleCond>"]],
    "<SimpleCond>": [["<ExprArith>", "<Comp>", "<ExprArith>"]],
    "<Comp>": [["EQUAL"],
               ["GREATER"],
               ["SMALLER"]],
    "<While>": [["WHILE", "<Cond>", "THEN", "<Code>", "ENDWHILE"]],
    "<For>": [["FOR", "VARNAME", "FROM", "<ExprArith>", "BY", "<ExprArith>", "TO", "<ExprArith>", "DO", "<Code>",
              "ENDFOR"]],
    "<Print>": [["PRINT", "LPAREN", "VARNAME", "RPAREN"]],
    "<Read>": [["READ", "LPAREN", "VARNAME", "RPAREN"]]
}


def is_variable(string):
    return '>' in string and '<' in string


def first(grammar):
    """Computes the first 1 algorithm on each derivation rule of the given grammar.
    :return Dictionary object with variables as keys and list of possible first tokens as items."""
    # Initialization (variables)
    first_values = {k: list() for k in grammar.keys()}

    # Initialization (terminals)
    first_values.update({k[:-1]: [k[:-1]] for k in tuple(open("tokens.txt", 'r', encoding='utf-8'))})

    # Main loop
    for rule in grammar:
        for derivation in grammar[rule]:
            for element in derivation:
                

    return first


def follow(grammar):
    """Computes the follow 1 algorithm on each derivation rule of the given grammar.
    :return Dictionary object with variables as keys and list of possible follow tokens as items."""
    return


def is_LL_1(grammar):
    """Check if the given grammar is LL(1) by verifying if every pair of rules A→α1 and A→α2 in P (with α1 ̸= α2) have
    disjoint sets First(α1Follow(A)) and First(α2Follow(A))"""
    return


if __name__ == '__main__':
    print('Bonjour UpyLaB!')
    first(GRAMMAR)
