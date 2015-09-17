// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

// Grammar Definition

script:                     statement* ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement  ;

lineStatement:              expression
							 | variableDeclaration;

variableDeclaration:        'var' IDENTIFIER '=' expression ;

expression:                 '(' expression ')'
							 | literal;

literal:                    booleanLiteral;

booleanLiteral:             'true' | 'false' ;
