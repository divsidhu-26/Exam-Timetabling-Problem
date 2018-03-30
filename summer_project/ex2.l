ENAME	(ANUM)*
ANUM	['A'-'Z' 0-9]

%{
#include<stdio.h>
%}


%s EXAM

%%

<EXAM>{ENAME}										{ECHO;}


<INITIAL>{ENAME}(" ")								{ECHO;BEGIN STATE1;}

[3][":"][0-9][0-9]							{printf("3");}
[1][":"][0-9][0-9]							{printf("1");}
[2][":"][0-9][0-9]							{printf("2");}
.											{;}
\n											{printf("\n");BEGIN 0;}

%%

main(){
	yylex();

}
