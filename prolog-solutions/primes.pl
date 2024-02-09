sift(1).

sift_inner_for(P, N, Max) :- Max >= N, assert(sift(N)), NextN is N + P, sift_inner_for(P, NextN, Max).
sift_for(Max, Max) :- !.
sift_for(X, Max) :- sift(X), !, NextX is X + 1, sift_for(NextX, Max).
sift_for(X, Max) :- X2 is 2 * X, \+ sift_inner_for(X, X2, Max), NextX is X + 1, sift_for(NextX, Max).
init(MAX_N) :- sift_for(2, MAX_N).

composite(N) :- sift(N).
prime(N) :- \+ sift(N).

multiplier(A, B, R) :- number(A), number(B), !, R is A * B.
multiplier(A, B, R) :- number(A), number(R), !, B is R / A.
multiplier(A, B, R) :- number(B), number(R), !, A is R / B.

foldl(Func, [], Init, Init).
foldl(Func, [H | T], Acc, R) :- G =.. [Func, H, Acc, NewAcc], call(G), foldl(Func, T, NewAcc, R).

sorted([ ]).
sorted([ _ ]).
sorted([ H1 , H2 | T ]) :- H1 =< H2, sorted([H2 | T]).

generate_divisors(1, _, []).

generate_divisors(N, MinDivisor, [MinDivisor | T]) :-
    MinDivisor =< N,
    prime(MinDivisor),
    0 is N mod MinDivisor, !,
    R is N / MinDivisor,
    generate_divisors(R, MinDivisor, T).

generate_divisors(N, MinDivisor, Res) :-
    MinDivisor =< N,
    \+(0 is N mod MinDivisor),
    NextDivisor is MinDivisor + 1,
    generate_divisors(N, NextDivisor, Res).

prime_divisors(N, Divisors) :- number(N), !, generate_divisors(N, 2, Divisors).

prime_divisors(N, Divisors) :- sorted(Divisors), forall(member(D, Divisors), prime(D)), foldl(multiplier, Divisors, 1, N).
