// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> skip ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;

STANDARD_IDENTIFIER:        [a-z][a-zA-Z]* ;
CONSTANT_IDENTIFIER:        [A-Z][A-Z_]* ;
TYPE_IDENTIFIER:            ([a-z]+ '.')*[A-Z][a-zA-Z]* ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

OPERATOR:                   [<>\+\-!£$%\^&\*#\~@\?/\\\|¬`¦:=]+ ;

// Grammar Definition

packageDeclaration:
	importDeclaration*
	constantDeclaration*
	(
		beanDeclaration |
		interfaceDeclaration |
		classDeclaration
	)*
	EOF
;

importDeclaration:          'import' TYPE_IDENTIFIER ;

constantDeclaration:        'constant' typeDeclaration CONSTANT_IDENTIFIER '=' constantValue ;

// functionDeclaration:     'function' typeDeclaration? STANDARD_IDENTIFIER argumentDeclarationList codeBlock ;

interfaceDeclaration:       'interface' typeDeclaration ('extends' typeDeclarationList)? '{' (declarationDeclaration)* '}' ;

classDeclaration:           'class' typeDeclaration argumentDeclarationList ( 'implements' typeDeclarationList )? '{' fieldDeclaration* methodDeclaration* '}' ;

beanDeclaration:            'bean' typeDeclaration ( 'extends' typeDeclaration )? '{' propertyDeclaration* '}' ;

fieldDeclaration:           'field' typeDeclaration STANDARD_IDENTIFIER ('=' expression )? ;

methodDeclaration:          'method' typeDeclaration? STANDARD_IDENTIFIER argumentDeclarationList codeBlock ;

declarationDeclaration:     'declare' typeDeclaration? STANDARD_IDENTIFIER argumentDeclarationList ;

propertyDeclaration:        'property' typeDeclaration? STANDARD_IDENTIFIER ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

lineStatement:              variableDeclaration | assignment | returnStatement | assignStatement | throwStatement | breakStatement | continueStatement | expression;

controlStatement:           conditionalStatement | tryStatement | whileStatement | forStatement | switchStatement | runStatement ;

controlExpression:          codeBlock | controlStatement

conditionalStatement:       'if' '(' expression ')' controlExpression ('else' controlStatement)* ;

tryStatement:               'try' controlExpression catchStatement+ ;

catchStatement:             'catch' '(' argumentDeclaration ')' controlExpression ;

whileStatement:             'while' '(' expression ')' controlExpression ;

forStatement:               'for' '(' argumentDeclaration ':' expression ')' controlExpression ;

switchStatement:            'switch' '(' expression ')' '{' caseStatement+ defaultStatement? '}' ;

caseStatement:              'case' expression ':' controlExpression ;

defaultStatement:           'default' ':' controlExpression ;

runStatement:               'run' controlStatement ;

variableDeclaration:        typeDeclaration identifier ('=' (expression | controlStatement))? ;

assignment:                 reference '=' (expression | controlStatement) ;

identifier:                 STANDARD_IDENTIFIER | CONSTANT_IDENTIFIER ;

call:                       identifier argumentList;

target:                     expressionBase ('.' memberName)* ;

invocation:                 (target '.')? call ;

reference:                  (target '.')? identifier ;

unaryOperation:             OPERATOR expressionForOperation ;

operation:                  expressionForOperation OPERATOR expression ;

construction:               'new' TYPE_IDENTIFIER argumentList ;

returnStatement:            'return' expression? ;

assignStatement:            'assign' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

typeDeclaration:            TYPE_IDENTIFIER ('['  typeDeclarationList ']')? '?'? '*'? ;

typeDeclarationList:        typeDeclaration (',' typeDeclaration)* ;

argumentDeclaration:        typeDeclaration STANDARD_IDENTIFIER ;

argumentDeclarationList:    '(' (argumentDeclaration ( ',' argumentDeclaration)*)? ')' ;

argumentList:               '(' ( expression ( ',' expression)*)? ')' ;

constantValue:              literal | construction ;

memberName:                 call | identifier;

expressionBase:             constantValue | '(' unaryOperation ')' | '(' operation ')' | memberName ;

expressionForOperation:     unaryOperation | invocation | reference | expressionBase ;

expression:                 operation | expressionForOperation ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:                '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;
