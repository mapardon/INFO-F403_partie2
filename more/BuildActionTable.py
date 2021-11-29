GRAMMAR = {
    "<Program>": [["BEG", "<Code>", "END"]],
    "<Code>": [["<InstList>"],
               ["epsilon"]],
    "<InstList>": [["<Instruction>", "<InstListEnd>"]],
    "<InstListEnd>": [["SEMICOLON", "<InstList>"],
                      ["epsilon"]],
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
    "<While>": [["WHILE", "<Cond>", "DO", "<Code>", "ENDWHILE"]],
    "<For>": [["FOR", "VARNAME", "FROM", "<ExprArith>", "BY", "<ExprArith>", "TO", "<ExprArith>", "DO", "<Code>",
               "ENDFOR"]],
    "<Print>": [["PRINT", "LPAREN", "VARNAME", "RPAREN"]],
    "<Read>": [["READ", "LPAREN", "VARNAME", "RPAREN"]]
}


def is_variable(string):
    return '>' in string and '<' in string and string != "<>"


def first(grammar, tokens_list):
    """Computes the first 1 algorithm on each derivation rule of the given grammar.
    :return Dictionary object with variables as keys and list of possible first tokens as items."""
    # Initialization (variables)
    first_values = {k: list() for k in grammar.keys()}

    # Initialization (terminals)
    first_values.update({k: [k] for k in tokens_list})

    # Main loop
    for variable in grammar.keys():
        first_loop(grammar, first_values, variable)

    return first_values


def first_loop(grammar, first_values, current):
    add = list()
    for derivation in grammar[current]:
        element = derivation[0]
        if is_variable(element):
            add.extend(first_loop(grammar, first_values, element))
        else:  # Already a terminal or epsilon
            add.append(element)
        # not the most efficient but I ran out of time
        for e in add:
            if e not in first_values[current]:
                first_values[current].append(e)
    return add


def follow(grammar, initial_variable, tokens_list):
    """Computes the follow 1 algorithm on each derivation rule of the given grammar.
    :return Dictionary object with variables as keys and list of possible follow tokens as items."""
    # Initialization (variables)
    follow_values = {k: list() for k in grammar.keys()}
    follow_values[initial_variable] = ["epsilon"]

    # Main loop
    firsts = first(grammar, tokens_list)

    updated = True
    while updated:
        updated = False
        for variable in grammar.keys():
            for rule in grammar[variable]:
                for pos, rule_element in enumerate(rule):
                    if is_variable(rule_element):  # no care about terminals
                        if pos != len(rule) - 1:  # first(beta)
                            add = list()
                            for e in firsts[rule[pos + 1]]:
                                add.append(e)

                            # must manage case we got an epsilon
                            pos += 2
                            while "epsilon" in add:
                                if pos == len(rule):  # must check in the follow of the current variable
                                    add.remove("epsilon")
                                    for e in follow_values[variable]:
                                        add.append(e)
                                else:
                                    add.remove("epsilon")
                                    for e in firsts[rule[pos]]:
                                        add.append(e)
                                pos += 1

                            # finally add elements
                            for a in add:
                                if a not in follow_values[rule_element]:
                                    follow_values[rule_element].append(a)
                                    updated = True

                        else:  # follow(alpha)
                            # add elements
                            for e in follow_values[variable]:
                                if e not in follow_values[rule_element]:
                                    follow_values[rule_element].append(e)
                                    updated = True

    return follow_values


def build_action_table(grammar, tokens_list, initial_variable, end_token):
    """ initial_variable = first variable ; end_token = symbol of end of program

    Action table represented by 2-dimensions dictionary of lists containing rules to apply when first index is
    followed by second index on input. Empty lists implies error at grammar level """
    atable = {k: {t: list() for t in tokens_list} for k in [k for k in grammar.keys()] + tokens_list}

    # Terminals
    for k in tokens_list:
        atable[k][k].append("M" if k != end_token else "A")

    # Variables
    firsts = first(grammar, tokens_list)
    follows = follow(grammar, initial_variable, tokens_list)

    n_rule = 0
    for variable in grammar.keys():
        for rule in grammar[variable]:
            n_rule += 1
            fafA = rule  # elements of α from First(α·Follow(A)), just considering sentential form and go in follows
            # if needed
            element = fafA[0]
            add = list()

            if not is_variable(element) and element != "epsilon":
                add.append((element, n_rule))

            elif element == "epsilon":
                next = 1
                add.append(("epsilon", n_rule))
                while ("epsilon", n_rule) in add:
                    add.remove(("epsilon", n_rule))
                    if next < len(fafA):
                        if not is_variable(fafA[next]) and fafA[next] != "epsilon":
                            add.append((fafA[next], n_rule))
                        elif fafA[next] == "epsilon":
                            add.append(("epsilon", n_rule))
                        else:  # variable
                            for e in firsts[fafA[next]]:
                                add.append((e, n_rule))
                    else:
                        for e in follows[variable]:
                            add.append((e, n_rule))
                    next += 1

            else:  # is a variable
                for e in firsts[element]:  # reminder: element = fafA[0]
                    add.append((e, n_rule))

                # test if epsilon has been added
                next = 1
                while ("epsilon", n_rule) in add:
                    add.remove(("epsilon", n_rule))
                    if next < len(fafA):
                        for e in firsts[fafA[next]]:
                            add.append((e, n_rule))
                    else:  # must continue search in the follows (with every element sigh)
                        for e in follows[variable]:
                            add.append((e, n_rule))
                    next += 1

            # finally add elements
            for a in add:
                atable[variable][a[0]].append(a[1])

    return atable


def pretty_print_action_table(table):
    global k, k2, v
    for k in table.keys():
        for k2 in table[k].keys():
            v = table[k][k2][0] if len(table[k][k2]) else 0
            print(" {} ".format(str(v)) if len(str(v)) < 2 else "{} ".format(str(v)), end="  ")
        print()


def java_format(pretty_print):
    prod = "{" + pretty_print.replace("  \n", "},\n{")
    prod = prod.replace("    ", ", ")
    prod = prod.replace("   ", ", ")
    prod = prod.replace("{ ", "{")
    prod = prod.replace(" }", "}")
    return prod


if __name__ == '__main__':

    # Tokens of our grammar
    with open("tokens-base/tokens.txt", 'r', encoding='utf-8') as f:
        tokens_list = f.readlines()
    tokens_list = [t[:-1] for t in tokens_list]

    # Run first/follow
    for res in (first(GRAMMAR, tokens_list), follow(GRAMMAR, "Program", tokens_list)):
        for k in res:
            print(k, end=': ')
            for v in res[k]:
                print(v, end=' ')
            print()
        print('\n')

    # Run action table builder
    table = build_action_table(GRAMMAR, tokens_list, "<Program>", "END")

    # Display action table
    print(end="      ")
    for t in tokens_list:
        print(t, end='   ')
    print()
    for k in table.keys():
        print("{}: ".format(k), end=' ')
        for k2 in table[k].keys():
            print(table[k][k2], end="  ")
        print()

    # Other display
    pretty_print_action_table(table)
    print(java_format(open("built.txt", 'r', encoding='utf-8').read()))

    # Void column test
    print("Unused test")
    for t in tokens_list:
        if not sum((r for k in table if is_variable(k) for r in table[k][t])):
            print("UNUSED: {}".format(t))

    # Ambiguous derivation
    print("\nAmbiguity test")
    for var in table.keys():
        for e in table[var]:
            if len(table[var][e]) > 1:
                print({"AMBIGUOUS: [{}][{}] ({})".format(var, e, table[var][e])})
