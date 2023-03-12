grammar CollapseLang;

program
    : entry*
    ;

entry
    : expr                  # entryExpr
    | IDENTIFIER '=' expr   # entryAssign
    ;

expr
    : expr OP_2_P0 term    # exprOp
    | term                 # exprTerm
    ;

term
    : term OP_2_P1 factor   # termOp
    | factor                # termFactor
    ;

factor
    : '(' expr ')' # factorParen
    | value        # factorValue
    ;

value
    : OP_1 '(' expr ')' # valOp1
    | '-' value         # valNeg
    | NUMBER            # valNumber
    | CONSTANT          # valConstant
    | IDENTIFIER        # valIdentifier
    ;

OP_1
    : 'sin'
    | 'cos'
    | 'tan'
    | 'asin'
    | 'acos'
    | 'atan'
    | 'sqrt'
    | 'log'
    | 'ln'
    | 'exp'
    ;

OP_2_P0
    : '+'
    | '-'
    ;

OP_2_P1
    : '*'
    | '/'
    | '^'
    | '%'
    ;

NUMBER
    : INT ('.' [0-9] +)?
    | '0x' [0-9A-Fa-f]+
    | '0b' [01]+
    ;

fragment INT
    : '0'
    | [1-9] [0-9]*
    ;

CONSTANT
    : CONSTANT_FIRST_CHAR CONSTANT_CHAR*
    ;

fragment CONSTANT_FIRST_CHAR
    : [A-Z]
    ;

fragment CONSTANT_CHAR
    : ~[\r\n\u2028\u2029\\" ()=]
    ;

IDENTIFIER
    : IDENTIFIER_FIRST_CHAR IDENTIFIER_CHAR*
    ;

fragment IDENTIFIER_FIRST_CHAR
    : [a-z]
    ;

fragment IDENTIFIER_CHAR
    : ~[\r\n\u2028\u2029\\" ()=]
    ;

WS
    : [ \r\n\t]+ -> skip
    ;

COMMENT
    : '#' ~[\r\n]* -> skip
    ;
