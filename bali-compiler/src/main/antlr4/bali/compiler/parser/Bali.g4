// Grammar of the Bali Language

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> skip ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

QM:                         '?';
EX:                         '!';

OPERATOR:                   [\+\-$%\^&\*#\~@/\\\|=<>]+ ;

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

constantDeclaration:        'constant' siteDefinition IDENTIFIER '=' constantValue ;

interfaceDeclaration:       'interface' typeDefinition ('extends' siteDefinitionList)? '{' (declarationDeclaration)* '}' ;

classDeclaration:           'object' typeDefinition argumentDeclarationList? ( 'implements' siteDefinitionList )? '{' fieldDeclaration* methodDeclaration* '}' ;

beanDeclaration:            'bean' typeDefinition ( 'extends' siteDefinition )? '{' propertyDeclaration* '}' ;

fieldDeclaration:           'field' siteDefinition IDENTIFIER ('=' expression )? ;

methodDeclaration:          'method' siteDefinition? IDENTIFIER argumentDeclarationList? codeBlock ;

declarationDeclaration:     'declare' siteDefinition? IDENTIFIER argumentDeclarationList? ;

propertyDeclaration:        'property' siteDefinition IDENTIFIER ;

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

variableDeclaration:        siteDefinition IDENTIFIER ('=' expression)? ;

assignment:                 reference '=' expression ;

call:                       IDENTIFIER argumentList ;

target:                     expressionBase ('.' memberName)* ;

invocation:                 (target '.')? call ;

reference:                  (target '.')? IDENTIFIER ;

nullCheck:                  QM expressionForOperation ;

unaryOperation:             OPERATOR expressionForOperation ;

operation:                  expressionForOperation OPERATOR expression ;

construction:               'new' typeIdentifier argumentList ;

returnStatement:            'return' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

siteDefinition:             typeIdentifier ('['  siteDefinitionList ']')? EX? QM? ;

siteDefinitionList:         siteDefinition (',' siteDefinition)* ;

typeDefinition:             typeIdentifier ('['  typeVarDeclarationList ']')? ;

typeVarDeclaration:         typeIdentifier? IDENTIFIER ;

typeVarDeclarationList:     typeVarDeclaration (',' typeVarDeclaration)* ;

argumentDeclaration:        siteDefinition IDENTIFIER ;

argumentDeclarationList:    '(' (argumentDeclaration ( ',' argumentDeclaration)*)? ')' ;

argumentList:               '(' ( expression ( ',' expression)*)? ')' ;

constantValue:              literal | construction ;

memberName:                 call | IDENTIFIER ;

expressionBase:             constantValue | '(' unaryOperation ')' | '(' operation ')' | memberName ;

expressionForOperation:     unaryOperation | nullCheck | invocation | reference | expressionBase ;

expression:                 operation | expressionForOperation ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:               '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;
