
TEST = {"<S>": [["<Exp>", "END"]],
        "<Exp>": [["<Prod>", "<Exp'>"]],
        "<Exp'>": [["PLUS", "<Prod>", "<Exp'>"],
                   ["MINUS", "<Prod>", "<Exp'>"],
                   ["epsilon"]],
        "<Prod>": [["<Atom>", "<Prod'>"]],
        "<Prod'>": [["TIMES", "<Atom>", "<Prod'>"],
                    ["DIVIDE", "<Atom>", "<Prod'>"],
                    ["epsilon"]],
        "<Atom>": [["MINUS", "<Atom>"],
                   ["NUMBER"],
                   ["VARNAME"],
                   ["LPAREN", "<Exp>", "RPAREN"]]}

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
    "<Assign>": [["VARNAME", "ASSIGN", "<ExprArith>"]],
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
    for variable in grammar.keys():
        first_loop(grammar, first_values, variable)

    # Main loop
    """ In case I would need it later
    updated = False
    while updated:
        updated = False
        for variable in grammar.keys():
            for ind, rule in enumerate(grammar[variable]):
                add = [element for element in rule]
                updated = is_variable(add[0])  # will have to change
                first_values[variable].append(add)

        # Test:
        updated = False
        for firsts in first_values.values():
            for f in firsts:
                if f == list() or is_variable(f[0]):
                    updated = True
                    continue
    """

    return first_values


def first_loop(grammar, first_values, current):
    if not is_variable(current):
        return first_values[current][0]
    else:
        add = list()
        for derivation in grammar[current]:
            element = derivation[0]
            if is_variable(element):
                add.extend(first_loop(grammar, first_values, element))
            else:  # Already a terminal
                add.append(element if element != "epsilon" else "epsilon")
            # not the most efficient but I ran out of time
            for e in add:
                if e not in first_values[current]:
                    first_values[current].append(e)
    return add


def follow(grammar, initial_symbol):
    """Computes the follow 1 algorithm on each derivation rule of the given grammar.
    :return Dictionary object with variables as keys and list of possible follow tokens as items."""
    # Initialization (variables)
    follow_values = {k: list() for k in grammar.keys()}
    follow_values[initial_symbol] = ["epsilon"]

     # Main loop
    firsts = first(grammar)

    updated = True
    while updated:
        updated = False
        for variable in grammar.keys():
            for rule in grammar[variable]:
                for element in rule:
                    if is_variable(element):  # no care about terminals
                        if rule.index(element) != len(rule) - 1:  # first(beta)
                            add = list()
                            for e in firsts[rule[rule.index(element) + 1]]:
                                add.append(e)

                            # must manage case we got an epsilon
                            shift = 2
                            while "epsilon" in add:
                                ind = rule.index(element) + shift
                                if ind == len(rule):  # must check in the follow of the cur variable
                                    add.remove("epsilon")
                                    for e in follow_values[variable]:
                                        add.append(e)
                                else:
                                    add.remove("epsilon")
                                    for e in firsts[rule[ind]]:
                                        add.append(e)
                                shift += 1

                            # finally add elements
                            for a in add:
                                if a not in follow_values[element]:
                                    follow_values[element].append(a)
                                    updated = True

                        else:  # follow(alpha)
                            for e in follow_values[variable]:
                                if e not in follow_values[element]:
                                    follow_values[element].append(e)
                                    updated = True

    return follow_values


def is_LL_1(grammar):
    """Check if the given grammar is LL(1) by verifying if every pair of rules A→α1 and A→α2 in P (with α1 ̸= α2) have
    disjoint sets First(α1Follow(A)) and First(α2Follow(A))"""
    return


def build_action_table(grammar, tokens_list, initial_symbol, endchar):
    """ initial symbol = first variable ; endchar = symbol of end of program

    Action table represented by 2-dimensions dictionary of lists containing rules to apply when first index is
    followed by second index on input. Empty lists implies error at grammar level """
    atable = {k: {t: list() for t in tokens_list} for k in [k for k in grammar.keys()] + tokens_list}

    # Terminals
    for k in tokens_list:
        atable[k][k].append("M" if k != endchar else "A")

    # Variables
    firsts = first(grammar)
    follows = follow(grammar, initial_symbol)
    i = 0
    for variable in grammar.keys():
        for rule in grammar[variable]:
            i += 1
            fafA = rule + follows[variable]  # elements of First(α·Follow(A))
            element = fafA[0]
            add = list()
            if not is_variable(element) and element != "epsilon":
                add.append((element, i))
            elif element == "epsilon":
                next = 1
                add.append("epsilon")
                while "epsilon" in add:
                    add.remove("epsilon")
                    if not is_variable(fafA[next]):
                        add.append((fafA[next], i))
                    elif fafA[next] == "epsilon":
                        add.append("epsilon")
                    else:  # variable
                        for e in firsts[fafA[next]]:
                            add.append((e, i))
                    next += 1
            else:  # is a variable
                for e in firsts[element]:
                    add.append((e, [k for k in grammar.keys()].index(element)))
                next = 1
                while "epsilon" in add:
                    add.remove("epsilon")
                    for e in firsts[fafA[next]]:
                        add.append((e, i))
                    next += 1
            # finally add elements
            for a in add:
                print(a)
                atable[variable][a[0]].append(a[1])

    return atable


if __name__ == '__main__':

    x = False
    if x:
        res = follow(TEST, "<S>")
        for k in res:
            print(k, end=': ')
            for v in res[k]:
                print(v, end=' ')
            print()

    with open("tokens.txt", 'r', encoding='utf-8') as f:
        tokens_list = f.readlines()
    tokens_list = [t[:-1] for t in tokens_list]
    tokens_list = ["END", "PLUS", "MINUS", "TIMES", "DIVIDE", "NUMBER", "VARNAME", "LPAREN", "RPAREN"]
    table = build_action_table(TEST, tokens_list, "<S>", "END")

    print(end="      ")
    for t in tokens_list:
        print(t, end='   ')
    print()
    for k in table.keys():
        print("{}: ".format(k), end=' ')
        for k2 in table[k].keys():
            print(table[k][k2], end="  ")
        print()
