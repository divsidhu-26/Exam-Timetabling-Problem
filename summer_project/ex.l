ENAME	(ANUM)*
ANUM	['A'-'Z' 0-9]

%{
#include<stdio.h>
%}


%s STATE1

%%

<INITIAL>{ENAME}(" ")								{ECHO;BEGIN STATE1;}

<STATE1>[3][":"][0-9][0-9]							{printf("3");}
<STATE1>[1][":"][0-9][0-9]							{printf("1");}
<STATE1>[2][":"][0-9][0-9]							{printf("2");}
<STATE1>.											{;}
<STATE1>\n											{printf("\n");BEGIN 0;}

%%

main(){
	yylex();

}
