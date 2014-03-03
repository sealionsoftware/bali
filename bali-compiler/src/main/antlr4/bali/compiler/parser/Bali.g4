// Grammar of the Bali Language
// TODO: clean up references and dereferencing

grammar Bali;

// Token Definition

WHITESPACE:                 [ \t\r\n]+ -> skip ;
LINE_COMMENT:               '//' ~[\r\n]* ('\r'? '\n' | EOF) -> skip ;

IDENTIFIER:                 [a-zA-Z_]+ ;

STRING_LITERAL:             '"' ~[^"]* '"' ;

NUMBER_LITERAL:             [0-9]+ ('.' [0-9]+)? ;

QM:                         '?';
AT:                         '@';

OPERATOR:                   [\+\-$%\^&\*#\~/\\\|=<>¬¦`!]+ ;

// Grammar Definition

packageDeclaration:
	importDeclaration*
	constantDeclaration*
	(
		beanDeclaration |
		interfaceDeclaration |
		objectDeclaration
	)*
	EOF
;

className:                  (IDENTIFIER '.')* IDENTIFIER ;

importDeclaration:          'import' className ;

constantDeclaration:        'constant' siteDefinition IDENTIFIER '=' expression ;

interfaceDeclaration:       'interface' typeDefinition ('extends' siteDefinitionList)? '{' (declarationDeclaration)* '}' ;

objectDeclaration:          'object' typeDefinition parameterList? ( 'implements' siteDefinitionList )? '{' fieldDeclaration* methodDeclaration* '}' ;

beanDeclaration:            'bean' typeDefinition ( 'extends' siteDefinition )? '{' propertyDeclaration* '}' ;

fieldDeclaration:           'field' siteDefinition IDENTIFIER ('=' expression )? ;

methodDeclaration:          'method' siteDefinition? IDENTIFIER parameterList? codeBlock ;

declarationDeclaration:     'declare' siteDefinition? IDENTIFIER parameterList? ;

propertyDeclaration:        'property' siteDefinition IDENTIFIER ;

codeBlock:                  '{' statement* '}' ;

statement:                  lineStatement | controlStatement ;

lineStatement:              (variableDeclaration | assignment | returnStatement | throwStatement | breakStatement | continueStatement | expression) ';';

controlStatement:           conditionalStatement | tryStatement | whileStatement | forStatement | switchStatement | runStatement ;

controlExpression:          codeBlock | controlStatement ;

conditionalStatement:       'if' '(' expression ')' controlExpression ('else' controlExpression)? ;

tryStatement:               'try' controlExpression catchBlock+ ;

catchBlock:                 'catch' '(' parameter ')' controlExpression ;

whileStatement:             'while' '(' expression ')' controlExpression ;

forStatement:               'for' '(' parameter ':' expression ')' controlExpression ;

switchStatement:            'switch' '(' expression ')' '{' caseBlock* defaultBlock? '}' ;

caseBlock:                  'case' expression ':' controlExpression ;

defaultBlock:               'default' ':' controlExpression ;

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

construction:               'new' className argumentList ;

returnStatement:            'return' expression? ;

throwStatement:             'throw' expression ;

breakStatement:             'break' ;

continueStatement:          'continue' ;

siteDefinition:             className ('['  siteDefinitionList ']')? AT? QM? ;

siteDefinitionList:         siteDefinition (',' siteDefinition)* ;

typeDefinition:             className ('['  typeVarDeclarationList ']')? ;

typeVarDeclaration:         className? IDENTIFIER ;

typeVarDeclarationList:     typeVarDeclaration (',' typeVarDeclaration)* ;

parameter:                  siteDefinition IDENTIFIER ;

parameterList:              '(' (parameter ( ',' parameter)*)? ')' ;

argument:                   (IDENTIFIER ':')? expression ;

argumentList:               '(' ( argument ( ',' argument)*)? ')' ;

memberName:                 call | IDENTIFIER ;

expressionBase:             literal | construction | '(' unaryOperation ')' | '(' operation ')' | memberName ;

expressionForOperation:     unaryOperation | nullCheck | invocation | reference | expressionBase ;

expression:                 operation | expressionForOperation ;

literal:                    STRING_LITERAL | NUMBER_LITERAL | booleanLiteral | arrayLiteral ;

arrayLiteral:               '[' (expression (',' expression)*)? ']' ;

booleanLiteral:             'true' | 'false' ;
