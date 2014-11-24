// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> skip ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

// Grammar Definition

packageDeclaration:
	(
		importDeclaration |
		constantDeclaration |
		beanDeclaration |
		interfaceDeclaration |
		objectDeclaration
	)*
	EOF
;

importDeclaration:          'import' (IDENTIFIER '.')* IDENTIFIER ( 'as' IDENTIFIER )? ;

constantDeclaration:        'constant' type? IDENTIFIER '=' expression ;

interfaceDeclaration:       'interface' type ('extends' typeList)? '{' (declarationDeclaration)* '}' ;

objectDeclaration:          'object' type parameterList? ( 'implements' typeList )? '{' fieldDeclaration* methodDeclaration* '}' ;

beanDeclaration:            'bean' type ( 'extends' type )? '{' propertyDeclaration* '}' ;

fieldDeclaration:           'field' type? IDENTIFIER ('=' expression )? ;

methodDeclaration:          'method' type? IDENTIFIER parameterList? controlStatement ;

declarationDeclaration:     'method' type? IDENTIFIER parameterList? ;

propertyDeclaration:        'property' type? IDENTIFIER ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

lineStatement:              variableDeclaration | returnStatement | throwStatement | breakStatement | continueStatement | expression ;

controlStatement:           codeBlock | conditionalStatement | tryStatement | whileStatement | forStatement | switchStatement | runStatement ;

conditionalStatement:       'if' '(' expression ')' controlStatement ('else' controlStatement)? ;

tryStatement:               'try' controlStatement catchBlock+ ;

catchBlock:                 'catch' '(' parameter ')' controlStatement ;

whileStatement:             'while' '(' expression ')' controlStatement ;

forStatement:               'for' '(' parameter ':' expression ')' controlStatement ;

switchStatement:            'switch' '(' expression ')' caseBlock+ defaultBlock? ;

caseBlock:                  'case' expression ':' controlStatement ;

defaultBlock:               'default' ':' controlStatement ;

runStatement:               'run' controlStatement ;

variableDeclaration:        'var' type? IDENTIFIER ('=' expression)? ;

construction:               'new' IDENTIFIER argumentList ;

returnStatement:            'return' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

type:                       IDENTIFIER ( '<'  typeList '>' )? '?'? ;

typeList:                   type (',' type)* ;

parameter:                  type IDENTIFIER ;

parameterList:              '(' (parameter ( ',' parameter)*)? ')' ;

argument:                   (IDENTIFIER ':')? expression ;

argumentList:               '(' ( argument ( ',' argument)*)? ')' ;

expression:                 '(' expression ')'
							 | literal
							 | construction
							 | invocation
							 | reference
							 | operator expression
							 | expression operator expression
							 | expression '.' ( invocation | reference )
							;

invocation:					IDENTIFIER argumentList ;

reference:					IDENTIFIER ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:               '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;

operator:                   ( '+' | '-' | '$' | '%' | '^' | '&' | '*' | '#' | '~' | '?' | '\\' | '|' | '=' | '<' | '>' | '¬' | '¦' | '`' | '!' | '@' )+ ;
