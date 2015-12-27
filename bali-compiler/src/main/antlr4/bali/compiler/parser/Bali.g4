// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

IDENTIFIER:                 [a-zA-Z_]+ ;

TEXT_LITERAL:               '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

// Grammar Definition

script:                     statement* ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

controlExpression:          codeBlock | controlStatement ;

conditionalStatement:       'if' '(' expression ')' controlExpression ;

loopStatement:              'while' '(' expression ')' controlExpression ;

lineStatement:              expression
							 | variableDeclaration
							 | assignment ;

controlStatement:           conditionalStatement | loopStatement;

variableDeclaration:        'var' type? IDENTIFIER '=' expression ;

assignment:                 reference '=' expression ;

expression:                 '(' expression ')'
							 | literal
							 | reference ;

type:                       IDENTIFIER ( '<'  typeList '>' )? ;

typeList:                   type (',' type)* ;

literal:                    booleanLiteral | textLiteral;

booleanLiteral:             'true' | 'false' ;

textLiteral:                TEXT_LITERAL;

reference:                  IDENTIFIER ;
