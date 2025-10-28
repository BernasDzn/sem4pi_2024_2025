grammar DSL;

/*
* Parser Rules
*/

code: version declarations instructions EOF;

//Version
version: 'DSL' 'version' VERSION SEMICOLON;
versionNumber: NUM
| versionNumber DOT versionNumber
;

//Declarations
declarations: (declaration)+;
declaration: CLASS VARNAME ((EQUALS expression)|constructor)? SEMICOLON;
//declaration_figure: CLASS VARNAME constructor';';

constructor: LP param RP;
param: expression (COMMA expression)*;


//EXPRESSIONS

atom: MINUS atom
| NUM
| vector
| CONST
| VARNAME
| LP expression RP
;

/*
    A math expression should be defined by a number, a variable, a vector or a math expression between parentheses.
    A math expression can be a sum, subtraction, multiplication or division of two math expressions.
    Percedence must be respected:
    1. Multiplication and division
    2. Addition and subtraction
    3. Parentheses
*/
expression: addSubExpr;
addSubExpr: multDivExpr (ADD multDivExpr|MINUS multDivExpr)*;
multDivExpr: atom (MUL atom|DIV atom)*;

//Vector
// A vector should be defined by 3 numbers or math expressions between parentheses separated by commas
vector: LP expression COMMA expression COMMA expression RP;

//INSTRUCTIONS
/*
    There are three types of instructions:
    1. Function call
    2. Group of instructions
    3. Before and after instructions
*/
instructions: (instruction|division)*;
instruction: ((objFunctionCall | dslfunction)SEMICOLON);

// A function call should be defined by a name and a list of parameters between parentheses separated by commas
objFunctionCall: VARNAME DOT function;
dslfunction: function;
function:FNAME (LP param RP)?;

/*
    A group of instructions should be defined by the keyword 'group' and 'endgroup'.
    A before instruction should be defined by the keyword 'before' and 'endbefore'.
    An after instruction should be defined by the keyword 'after' and 'endafter'.
*/
division: GROUP instructions ENDGROUP
| BEFORE instructions ENDBEFORE
| AFTER instructions ENDAFTER
;


/*
* Lexer Rules
*/

LETTER: [a-zA-Z];
NUM: [0-9]+ ('.' [0-9]+)?;
MUL: '*';
DIV: '/';
ADD: '+';
MINUS: '-';
EQUALS: '=';
LP: '(';
RP: ')';
COMMA: ',';
SEMICOLON: ';';
DOT: '.';
WS: [ \t\r\n]+ -> skip;
//STRING: '"'(LETTER|NUM)*'"'; //adicionar o resto dos caracteres
//BOOL: (('t'|'T')('r'|'R')('u'|'U')('e'|'E')) | (('f'|'F')('a'|'A')('l'|'L')('s'|'S')('e'|'E'));
//VECTOR3: ('('' '?NUM' '?','' '?NUM' '?','' '?NUM' '?')')+;

//CLASS: [A-Z]+LETTER*;
CLASS: 'DroneType'|'Position'|'Velocity'|'Distance'|'Line'|'Rectangle'|'Circle'|'Circumference';
FNAME: 'lightsOn'|'lightsOff'|'move'|'rotate'|'pause'|'movePos';

GROUP: 'group';
ENDGROUP: 'endgroup';
BEFORE: 'before';
ENDBEFORE: 'endbefore';
AFTER: 'after';
ENDAFTER: 'endafter';

CONST: [A-Z]([A-Z]|NUM)*;
VARNAME: ([a-z](NUM|LETTER)*);

VERSION: [0-9]+('.'[0-9]+)*;