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

lineStatement:              variableDeclaration | assignment | returnStatement | throwStatement | breakStatement | continueStatement | expression;

controlStatement:           conditionalStatement | tryStatement | whileStatement | forStatement | switchStatement | runStatement ;

conditionalStatement:       'if' '(' expression ')' codeBlock ('else' 'if' '(' expression ')' codeBlock)* ('else' codeBlock)? ;

tryStatement:               'try' codeBlock catchStatement+ ;

catchStatement:             'catch' '(' argumentDeclaration ')' codeBlock ;

whileStatement:             'while' '(' expression ')' codeBlock ;

forStatement:               'for' '(' argumentDeclaration ':' expression ')' codeBlock ;

switchStatement:            'switch' '(' expression ')' '{' caseStatement+ defaultStatement? '}' ;

caseStatement:              'case' expression ':' codeBlock ;

defaultStatement:           'default' ':' codeBlock ;

runStatement:               'run' codeBlock ;

variableDeclaration:        typeDeclaration identifier ('=' expression)? ;

assignment:                 reference '=' expression ;

identifier:                 STANDARD_IDENTIFIER | CONSTANT_IDENTIFIER ;

call:                       identifier argumentList;

invocation:                 call |
							expressionBase '.' call |
							reference '.' call |
							invocation '.' call ;

reference:                  identifier |
							expressionBase '.' identifier |
							reference '.' identifier ;

unaryOperation:             OPERATOR expression ;

// Changed due to left recursion
operation:                  expressionForOperation OPERATOR expressionForOperation ;

construction:               'new' TYPE_IDENTIFIER argumentList ;

returnStatement:            'return' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

argumentDeclarationList:    '(' (argumentDeclaration ( ',' argumentDeclaration)*)? ')' ;

typeDeclaration:            TYPE_IDENTIFIER ('['  typeDeclarationList ']')? ;

typeDeclarationList:        typeDeclaration (',' typeDeclaration)* ;

argumentDeclaration:        typeDeclaration STANDARD_IDENTIFIER ;

argumentList:               '(' ( expression ( ',' expression)*)? ')' ;

constantValue:              literal | construction ;

expressionBase:             constantValue | '(' unaryOperation ')' | '(' operation ')' ;

expressionForOperation:     expressionBase | reference | invocation | unaryOperation;

expression:                 operation | expressionForOperation ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:                '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;
