// Grammar of the Bali Language

grammar Bali;

@members {
 	public static class ParserRuleContext extends org.antlr.v4.runtime.ParserRuleContext {
 	    ParserRuleContext(ParserRuleContext parent, int invokingState) {
    			super(parent, invokingState);
    	}

 	    int count = 0;
 	}
}

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> channel(HIDDEN) ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN) ;

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
		objectDeclaration+
	)*
	EOF
;

importDeclaration:          'import' qualifiedName ( 'as' IDENTIFIER )? ;

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

controlStatement:           ( codeBlock | conditionalStatement | whileStatement | forStatement | switchStatement | runStatement ) catchBlock+ ;

conditionalStatement:       'if' '(' expression ')' controlStatement ('else' controlStatement)? ;

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

qualifiedName:              (IDENTIFIER '.')* IDENTIFIER;