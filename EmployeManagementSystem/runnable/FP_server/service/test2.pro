:- dynamic comEmployee/1.
:- dynamic engineer/1.
:- dynamic manager/1.

serviceTrust(com,comempl).
serviceTrust(com,mgr).
serviceTrust(com,egn).


readDoc(X) :-
	comEmployee(X).

readCode(X) :-
	engineer(X);
	manager(X).

writeCode(X) :-
	engineer(X).

addProj(X) :-
	manager(X).
