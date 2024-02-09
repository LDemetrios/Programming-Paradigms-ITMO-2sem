%%%%%%%%%%%%%%%%%%%% Maths

sum(A, B, R) :- number(A), number(B), R is A + B, !.
sum(A, B, R) :- number(A), number(R), B is R - A, !.
sum(A, B, R) :- number(B), number(R), A is R - B, !.

prod(0, B, 0) :- !.
prod(A, 0, 0) :- !.
prod(A, B, R) :- number(A), number(B), R is A * B, !.
prod(A, B, R) :- number(A), number(R), R \= 0, B is R / A, !.
prod(A, B, R) :- number(B), number(R), R \= 0, A is R / B, !.

%%%%%%%%%%%%%%%%%%%% Utilities

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

%%%%%%%%%%%%%%%%%%%% Evaluation

evaluate_all([], Map, []).
evaluate_all([H | T], Map, [HV | TV]) :- evaluate(H, Map, HV), evaluate_all(HV, Map, TV).

apply(op_negate, X, R) :- sum(X, R, 0).

apply(op_add, X, Y, R) :- sum(X, Y, R).
apply(op_subtract, X, Y, R) :- sum(R, Y, X).
apply(op_multiply, X, Y, R) :- prod(X, Y, R).
apply(op_divide, X, Y, R) :- Y \= 0, prod(R, Y, X).

int_bool(I, B) :- (I > 0 -> B = 1 ; B = 0).
bool_op(op_not, X, RB) :- (X = 0 -> RB = 1 ; RB = 0).
bool_op(op_and, X, Y, RB) :- prod(X, Y, R), (R = 1 -> RB = 1 ; RB = 0).
bool_op(op_or, X, Y, RB) :- sum(X, Y, R), (R \= 0 -> RB = 1 ; RB = 0).
bool_op(op_xor, X, Y, RB) :- sum(X, Y, R), (R = 1 -> RB = 1 ; RB = 0).
bool_op(op_impl, X, Y, RB) :- sum(Y, R, X), (R \= 1 -> RB = 1 ; RB = 0).
bool_op(op_iff, X, Y, RB) :- (X = Y -> RB = 1 ; RB = 0).
apply(Op, X, R) :- int_bool(X, XB), bool_op(Op, XB,R).
apply(Op, X, Y, R) :- int_bool(X, XB),  int_bool(Y, YB), bool_op(Op, XB, YB, R).

evaluate(const(Value), _, Value).
evaluate(variable(Name), Map, R) :- atom_chars(Name, [Char | _]), lookup(Char, Map, R).
evaluate(operation(Op, E), Map, R) :- evaluate(E, Map, EV), apply(Op, EV, R).
evaluate(operation(Op, E1, E2), Map, R) :- evaluate(E1, Map, E1V), evaluate(E2, Map, E2V), apply(Op, E1V, E2V, R).

example(operation(op_subtract,operation(op_multiply,const(2),operation(op_negate, variable(x))),const(3))).

%%%%%%%%%%%%%%%%%%%% Grammar

:- load_library('alice.tuprolog.lib.DCGLibrary').

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Name)) --> % [xyzXYZ]*
    { nonvar(Name, atom_chars(Name, Chars)) }, letters_p(Chars), { atom_chars(Name, Chars) }.

letter_p(V) --> { member(V, ['x', 'y', 'z', 'X', 'Y', 'Z']) }, [V].
letters_p([H]) --> letter_p(H).
letters_p([H | T]) --> letter_p(H), letters_p(T).

expr_p(const(Value)) -->
    { nonvar(Value, number_chars(Value, Chars)) }, number_p(Chars), { number_chars(Value, Chars) }.

number_p(Chars) --> digits_p(Chars).
number_p(['-' | T]) --> ['-'], digits_p(T).
digit_p(D) --> { member(D, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']) }, [D].
digits_p([H]) --> digit_p(H).
digits_p([H | T]) --> digit_p(H), digits_p(T).

op1(op_not, '!').
op1(op_negate, 'negate').
op2(op_add, '+').
op2(op_subtract, '-').
op2(op_multiply, '*').
op2(op_divide, '/').
op2(op_and, '&&').
op2(op_or, '||').
op2(op_xor, '^^').
op2(op_impl, '->').
op2(op_iff, '<->').

op1_p(Op) --> { op1(Op, S) }, exactly_p(S).
op2_p(Op) --> { op2(Op, S) }, exactly_p(S).
exactly_p(Op) --> { atom_chars(Op, C) }, C.

skip_ws([], []).
skip_ws([' ' | T], RT) :- !, skip_ws(T, RT).
skip_ws([H | T], [H | RT]) :- skip_ws(T, RT).

expr_p(operation(Op, A)) --> {var(Op) -> Pad = []; Pad = [' ']}, op1_p(Op), Pad, expr_p(A).
expr_p(operation(Op, A, B)) -->
    { var(Op) -> Pad = []; Pad = [' '] },
    ['('], expr_p(A), Pad, op2_p(Op), Pad, expr_p(B), [')'].

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), skip_ws(C, C1), phrase(expr_p(E), C1).
