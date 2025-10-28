grammar DL;

/*===============================
 *          Parser Rules
 *===============================*/

parse:
    (instruction | declaration)* EOF
;

//----------------------------------
// Declarations
//----------------------------------

declaration:
    POSITION ID EQUALS expression SEMICOLON
    | VECTOR ID EQUALS expression SEMICOLON
    | LINEARVELOCITY ID EQUALS expression SEMICOLON
    | ANGULARVELOCITY ID EQUALS expression SEMICOLON
    | DISTANCE ID EQUALS expression SEMICOLON
    | TIME ID EQUALS intExpression SEMICOLON
;

//----------------------------------
// Instructions
//----------------------------------

instruction:
    takeoff
    | land
    | move
    | movePath
    | hoover
    | lightsOn
    | lightsOff
    | blink
;

takeoff:
    'takeOff' LP expression COMMA expression RP SEMICOLON
;

land:
    'land' LP expression RP SEMICOLON
;

move:
    'move' LP expression COMMA expression RP SEMICOLON
    | 'move' LP expression COMMA expression COMMA intExpression RP SEMICOLON
;

movePath:
    'movePath' LP expression COMMA expression RP SEMICOLON
;

hoover:
    'hoover' LP intExpression RP SEMICOLON
;

lightsOn:
    'lightsOn' LP RP SEMICOLON
;

lightsOff:
    'lightsOff' LP RP SEMICOLON
;

blink:
    'blink' LP intExpression RP SEMICOLON
;

// Expressions (Float)
expression:
    addSubExpr
;

addSubExpr:
    multDivExpr ( (PLUS | MINUS) multDivExpr )*
;

multDivExpr:
    atom ( (MULTIPLY | DIVIDE) atom )*
;

atom:
    MINUS atom                           #UnaryMinus
    | vector                             #VectorLiteral
    | arrayOfPositions                   #ArrayLiteral
    | PI                                 #PiConst
    | FLOAT                              #FloatLiteral
    | NUM                                #IntLiteral
    | ID                                 #Variable
    | LP expression RP                   #ParenExpr
;

// Integer-only Expressions (e.g., time/duration)
intExpression:
    intTerm ( (PLUS | MINUS) intTerm )*
;

intTerm:
    intAtom ( (MULTIPLY | DIVIDE) intAtom )*
;

intAtom:
    MINUS intAtom
    | NUM
    | ID
    | LP intExpression RP
;

// Vector & Array Expressions
vector:
    LP expression COMMA expression COMMA expression RP
;

arrayOfPositions:
    LP vector (COMMA vector)* RP
;

/*===============================
 *          Lexer Rules
 *===============================*/

// Types
POSITION: 'Position';
VECTOR: 'Vector';
LINEARVELOCITY: 'LinearVelocity';
ANGULARVELOCITY: 'AngularVelocity';
DISTANCE: 'Distance';
TIME: 'Time';

// Symbols
EQUALS: '=';
COMMA: ',';
SEMICOLON: ';';
LP: '(';
RP: ')';

// Operators
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
OPERATOR: PLUS | MINUS | MULTIPLY | DIVIDE;
SIGN: PLUS | MINUS;

// Numbers
PI: 'PI';
FLOAT: [0-9]+ '.' [0-9]+;
NUM: [0-9]+;

// Identifiers
ID: [a-zA-Z_][a-zA-Z0-9_]*;

// Whitespace
WS: [ \t\r\n]+ -> skip;
