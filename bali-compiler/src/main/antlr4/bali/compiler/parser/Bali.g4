// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> skip ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

EQ:                         '=';
QM:                         '?';
LT:                         '<';
GT:                         '>';
EX:                         '!';

OPERATOR:                   [\+\-£$%\^&\*#\~@/\\\|¬`¦:=\?<>\!]+ ;

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

typeIdentifier:             (IDENTIFIER '.')* IDENTIFIER ;

importDeclaration:          'import' typeIdentifier ;

constantDeclaration:        'constant' typeDeclaration IDENTIFIER EQ constantValue ;

interfaceDeclaration:       'interface' classDefinition ('extends' typeDeclarationList)? '{' (declarationDeclaration)* '}' ;

classDeclaration:           'class' classDefinition argumentDeclarationList? ( 'implements' typeDeclarationList )? '{' fieldDeclaration* methodDeclaration* '}' ;

beanDeclaration:            'bean' classDefinition ( 'extends' typeDeclaration )? '{' propertyDeclaration* '}' ;

fieldDeclaration:           'field' typeDeclaration IDENTIFIER (EQ expression )? ;

methodDeclaration:          'method' typeDeclaration? IDENTIFIER argumentDeclarationList? codeBlock ;

declarationDeclaration:     'declare' typeDeclaration? IDENTIFIER argumentDeclarationList? ;

propertyDeclaration:        'property' typeDeclaration IDENTIFIER ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

lineStatement:              variableDeclaration | assignment | returnStatement | throwStatement | breakStatement | continueStatement | expression;

controlStatement:           conditionalStatement | tryStatement | whileStatement | forStatement | switchStatement | runStatement ;

controlExpression:          codeBlock | controlStatement ;

conditionalStatement:       'if' '(' expression ')' controlExpression ('else' controlExpression)? ;

tryStatement:               'try' controlExpression catchStatement+ ;

catchStatement:             'catch' '(' argumentDeclaration ')' controlExpression ;

whileStatement:             'while' '(' expression ')' controlExpression ;

forStatement:               'for' '(' argumentDeclaration ':' expression ')' controlExpression ;

switchStatement:            'switch' '(' expression ')' '{' caseStatement+ defaultStatement? '}' ;

caseStatement:              'case' expression ':' controlExpression ;

defaultStatement:           'default' ':' controlExpression ;

runStatement:               'run' controlExpression ;

variableDeclaration:        typeDeclaration IDENTIFIER (EQ expression)? ;

assignment:                 reference EQ expression ;

call:                       IDENTIFIER argumentList ;

target:                     expressionBase ('.' memberName)* ;

invocation:                 (target '.')? call ;

reference:                  (target '.')? IDENTIFIER ;

operator:                   QM | LT | GT | EX | OPERATOR ;

unaryOperation:             operator expressionForOperation ;

operation:                  expressionForOperation (operator expressionForOperation)+ ;

construction:               'new' typeIdentifier argumentList ;

returnStatement:            'return' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

typeDeclaration:            typeIdentifier (LT  typeDeclarationList GT)? QM? EX? ;

typeDeclarationList:        typeDeclaration (',' typeDeclaration)* ;

classDefinition:            typeIdentifier (LT  typeVarDeclarationList GT)? ;

typeVarDeclaration:         typeIdentifier? IDENTIFIER ;

typeVarDeclarationList:     typeVarDeclaration (',' typeVarDeclaration)* ;

argumentDeclaration:        typeDeclaration IDENTIFIER ;

argumentDeclarationList:    '(' (argumentDeclaration ( ',' argumentDeclaration)*)? ')' ;

argumentList:               '(' ( expression ( ',' expression)*)? ')' ;

constantValue:              literal | construction ;

memberName:                 call | IDENTIFIER ;

expressionBase:             constantValue | '(' unaryOperation ')' | '(' operation ')' | memberName ;

expressionForOperation:     unaryOperation | invocation | reference | expressionBase ;

expression:                 operation | expressionForOperation ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:                '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;
