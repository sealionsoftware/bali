// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

// Grammar Definition

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement  ;

lineStatement:              variableDeclaration ;

variableDeclaration:        'var' type? IDENTIFIER ('=' expression)? ;

type:                       IDENTIFIER ( '<'  typeList '>' )? '?'? ;

typeList:                   type (',' type)* ;

expression:                 '(' expression ')'
							 | literal
							;

literal:                    STRING_LITERAL ;
