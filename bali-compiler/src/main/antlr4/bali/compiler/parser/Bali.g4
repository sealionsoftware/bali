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

// Grammar Definition

packageDeclaration:
	importDeclaration*
	constantDeclaration*
	// functionDeclaration*
	interfaceDeclaration*
	classDeclaration*
	EOF
;

importDeclaration:   'import' TYPE_IDENTIFIER ';' ;

constantDeclaration: 'constant' TYPE_IDENTIFIER CONSTANT_IDENTIFIER '=' constantValue ';' ;

// functionDeclaration: 'function' TYPE_IDENTIFIER? STANDARD_IDENTIFIER argumentDeclarationList codeBlock ;

interfaceDeclaration: 'interface' TYPE_IDENTIFIER ('extends' typeDeclarationList)? '{' (declarationDeclaration)* '}' ;

classDeclaration: 'class' TYPE_IDENTIFIER argumentDeclarationList ( 'implements' typeDeclarationList )? '{' fieldDeclaration* methodDeclaration* '}' ;

fieldDeclaration: 'field' TYPE_IDENTIFIER STANDARD_IDENTIFIER ('=' value )? ';' ;

methodDeclaration: 'method' TYPE_IDENTIFIER? STANDARD_IDENTIFIER argumentDeclarationList codeBlock ;

declarationDeclaration: 'declare' TYPE_IDENTIFIER? STANDARD_IDENTIFIER argumentDeclarationList ';' ;

codeBlock:              '{' statement* '}' ;

statement:              lineStatement | controlStatement ;

lineStatement:          (variableDeclaration | invocation | assignment | returnStatement) ';' ;

controlStatement:       conditionalStatement | tryStatement | whileStatement | forStatement ;

conditionalStatement:   'if' '(' value ')' codeBlock ('else if' '(' value ')' codeBlock)* ('else' codeBlock)? ;

tryStatement:           'try' codeBlock ('catch' '(' argumentDeclaration ')' codeBlock)+  ;

whileStatement:         'while' '(' value ')' codeBlock;

forStatement:           'for' '(' argumentDeclaration ':' value ')' codeBlock;

variableDeclaration:    TYPE_IDENTIFIER assignment ;

assignment:             STANDARD_IDENTIFIER '=' value ;

identifier:             STANDARD_IDENTIFIER | CONSTANT_IDENTIFIER;

invocation:             (identifier '.')? identifier argumentList  ;

construction:           'new' TYPE_IDENTIFIER argumentList ;

returnStatement:        'return' value?;

argumentDeclarationList: '(' (argumentDeclaration ( ',' argumentDeclaration)*)? ')' ;

typeDeclarationList:    TYPE_IDENTIFIER (',' TYPE_IDENTIFIER)*  ;

argumentDeclaration:    TYPE_IDENTIFIER STANDARD_IDENTIFIER;

argumentList:           '(' ( value ( ',' value)*)? ')' ;

constantValue:          literal | construction ;

value:                  constantValue |
						identifier |
						invocation;

literal:                STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | listLiteral ;

booleanLiteral:         'true' | 'false' ;

listLiteral:            '[' value (',' value)+ ']' ;
