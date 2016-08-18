// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

IDENTIFIER:                 [a-zA-Z_]+ ;

TEXT_LITERAL:               '"' ~[^"]* '"' ;

INTEGER_LITERAL:            [0] | [1-9][0-9]*;

OPERATOR:                   [\+\-$%\^&\*#\~/\\\|=<>¬¦`!]+ ;

// Grammar Definition

script:                     statement* ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

controlExpression:          codeBlock | controlStatement ;

conditionalStatement:       'if' '(' expression ')' controlExpression ('else' controlExpression )? ;

loopStatement:              'while' '(' expression ')' controlExpression ;

lineStatement:              expression
							 | variableDeclaration
							 | assignment ;

controlStatement:           conditionalStatement | loopStatement;

variableDeclaration:        'var' type? IDENTIFIER ('=' expression)? ;

assignment:                 reference '=' expression ;

expression:                 '(' expression ')'
							 | literal
							 | invocation
							 | reference
							 | OPERATOR expression
							 | expression OPERATOR expression
							 | expression '.' invocation;

invocation:				    IDENTIFIER '(' ( argument ( ',' argument )* )? ')' ;

type:                       IDENTIFIER ( '['  type ( ',' type)* ']' )? '?' ? ;

argument:                   expression ;

literal:                    logicLiteral | textLiteral | integerLiteral | arrayLiteral;

logicLiteral:               'true' | 'false' ;

textLiteral:                TEXT_LITERAL;

integerLiteral:             INTEGER_LITERAL;

arrayLiteral:               '['  ( expression ( ',' expression )* )? ']';

reference:                  IDENTIFIER ;

