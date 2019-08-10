// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

IDENTIFIER:                 [a-zA-Z_]+ ;

TEXT_LITERAL:               '"' ~[^"]* '"' ;

INTEGER_LITERAL:            [0] | [1-9][0-9]*;

SYMBOL:                     [\+\-$%\^&\*#\~/\\\|=<>¬¦`!]+ ;

QUERY:                      [?];

// Grammar Definition

script:                     statement* ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

conditionalStatement:       'if' '(' expression ')' controlStatement ('else' controlStatement )? ;

loopStatement:              'while' '(' expression ')' controlStatement ;

iterationStatement:         'for' '(' IDENTIFIER ':' expression ')' controlStatement ;

throwStatement:             'throw' expression ;

catchStatement:             catchableStatement 'catch' '(' type IDENTIFIER ')' controlStatement ;

lineStatement:              expression
							 | variableDeclaration
							 | assignment ;

catchableStatement:         codeBlock | conditionalStatement | loopStatement | iterationStatement | throwStatement;

controlStatement:           catchableStatement | catchStatement;

variableDeclaration:        'var' type? IDENTIFIER ('=' expression)? ;

assignment:                 reference '=' expression ;

operator:                   QUERY | SYMBOL ;

expression:                 '(' expression ')'
							 | literal
							 | invocation
							 | reference
							 | expression '.' invocation
							 | operator expression
							 | <assoc=right> expression operator expression;

invocation:				    IDENTIFIER '(' ( argument ( ',' argument )* )? ')' ;

type:                       IDENTIFIER ( '['  type ( ',' type)* ']' )? QUERY ? ;

argument:                   expression ;

literal:                    logicLiteral | textLiteral | integerLiteral | arrayLiteral;

logicLiteral:               'true' | 'false' ;

textLiteral:                TEXT_LITERAL;

integerLiteral:             INTEGER_LITERAL;

arrayLiteral:               '['  ( expression ( ',' expression )* )? ']';

reference:                  IDENTIFIER ;

