:- dynamic employee/2.
:- dynamic head/2.


comTrust(department_a,empl).
comTrust(department_b,empl).

comEmployee(X) :-
	employee(X,M),
	comTrust(M,empl);
	manager(X);
	engineer(X).

manager(X) :-
	head(X,M),
	comTrust(M,empl).

engineer(X) :-
	employee(X,department_a),
	comTrust(department_a,empl).
