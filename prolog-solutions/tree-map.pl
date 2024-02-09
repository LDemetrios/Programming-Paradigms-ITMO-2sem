sum(A, B, R) :- number(A), number(B), R is A + B, !.
sum(A, B, R) :- number(A), number(R), B is R - A, !.
sum(A, B, R) :- number(B), number(R), A is R - B, !.
max(A, B, Max) :- (A >= B -> Max = A ; Max = B).
abs(A, R) :- R = A, R >= 0.
abs(A, R) :- sum(A, R, 0), R > 0.
inc(A, B) :- sum(A, 1, B).

% tree(Key, Value, Left, Right, Height, Balance)
key(tree(Key, _, _, _, _, _), Key).
value(tree(_, Value, _, _, _, _), Value).
left(tree(_, _, Left, _, _, _), Left).
right(tree(_, _, _, Right, _, _), Right).
height(tree(_, _, _, _, Height, _), Height).
height(null, 0).
balance(tree(_, _, _, _, _, Balance), Balance).
balance(null, 0).

buildBalanced(K, V, LChild, RChild, tree(K, V, LChild, RChild, NH, NB)) :-
    height(LChild, LH), height(RChild, RH),
    max(LH, RH, NH1), inc(NH1, NH),
    sum(RH, NB, LH), abs(NB, NBAbs), NBAbs < 2.

build(K, V, LChild, RChild, Res) :-
    buildBalanced(K, V, LChild, RChild, Res), !.

% Small left rotation
build(AKey, AVal, P, tree(BKey, BVal, Q, R, BHei, BBal), Result) :-
    height(P, PHei), sum(BHei, ABal, PHei), ABal = -2, (BBal = 0 ; BBal = -1),
    buildBalanced(AKey, AVal, P, Q, ASubtree),
    buildBalanced(BKey, BVal, ASubtree, R, Result), !.

% Big left rotation
build(AKey, AVal, P, 
  tree(BKey, BVal, tree(CKey, CVal, Q, R, _, _), S, BHei, BBal),
  Result) :-
    height(P, PHei), sum(BHei, ABal, PHei), ABal = -2, BBal = 1,
    buildBalanced(AKey, AVal, P, Q, ASubtree),
    buildBalanced(BKey, BVal, R, S, BSubtree),
    buildBalanced(CKey, CVal, ASubtree, BSubtree, Result), !.


% Small right rotation
build(AKey, AVal, tree(BKey, BVal, L, C, BHei, BBal), R, Result) :-
    height(R, RHei), sum(RHei, ABal, BHei), ABal = 2, (BBal = 0 ; BBal = 1),
    buildBalanced(AKey, AVal, C, R, ASubtree),
    buildBalanced(BKey, BVal, L, ASubtree, Result), !.

% Big right rotation
build(AKey, AVal, 
  tree(BKey, BVal, L, tree(CKey, CVal, M, N, _, _), BHei, BBal),
  R,
  Result) :-
    height(R, RHei), sum(RHei, ABal, BHei), ABal = 2, BBal = -1,
    buildBalanced(BKey, BVal, L, M, BSubtree),
    buildBalanced(AKey, AVal, N, R, ASubtree),
    buildBalanced(CKey, CVal, BSubtree, ASubtree, Result), !.

%%%%%%%%%%%%%%%%

map_put(null, K, V, Result) :- Result = tree(K, V, null, null, 1, 0), !.

map_put(tree(K, RootV, L, R, H, B), K, V, tree(K, V, L, R, H, B)) :- !.
map_put(tree(RootK, RootV, L, R, _, _), K, V, Result) :-
    K < RootK, !, map_put(L, K, V, NL), build(RootK, RootV, NL, R, Result).
map_put(tree(RootK, RootV, L, R, _, _), K, V, Result) :-
    K > RootK, !, map_put(R, K, V, NR), build(RootK, RootV, L, NR, Result).

map_put(null, K, V, Result) :- Result = tree(K, V, null, null, 1, 0), !.

%%%%%%%%%%%%%%%%%%%%%%%%%

leftmost(tree(K, V, null, _, _, _), K, V).
leftmost(tree(_, _, L, _, _, _), K, V) :- leftmost(L, K, V).
rightmost(tree(K, V, _, null, _, _), K, V).
rightmost(tree(_, _, _, R, _, _), K, V) :- rightmost(R, K, V).

map_remove(null, K, null) :- !.
map_remove(tree(K, _, L, null, _, _), K, L) :- !.
map_remove(tree(K, _, null, R, _, _), K, R) :- !.
map_remove(tree(K, _, L, R, _, _), K, Result) :- !,
    height(L, LHei), height(R, RHei), (
        LHei < RHei, leftmost(R, KLeftmostR, VLeftmostR),
        map_remove(R, KLeftmostR, NR),
        buildBalanced(KLeftmostR, VLeftmostR, L, NR, Result)
    ;
        LHei >= RHei, rightmost(L, KRightmostL, VRightmostL),
        map_remove(L, KRightmostL, NL),
        buildBalanced(KRightmostL, VRightmostL, NL, R, Result)
    ).

map_remove(tree(RootK, RootV, L, R, _, _), K, Result) :-
    K < RootK, !, map_remove(L, K, NL), build(RootK, RootV, NL, R, Result).
map_remove(tree(RootK, RootV, L, R, _, _), K, Result) :-
    K > RootK, !, map_remove(R, K, NR), build(RootK, RootV, L, NR, Result).

%%%%%%%%%%%%%%%%%%%%%%%

map_get(tree(K, V, _, _, _, _), K, V) :- !.
map_get(tree(RK, RV, L, _, _, _), K, V) :- K < RK, !, map_get(L, K, V).
map_get(tree(RK, RV, _, R, _, _), K, V) :- K > RK, !, map_get(R, K, V).

map_build([], null).
map_build([(K, V) | T], NR) :- map_build(T, R), map_put(R, K, V, NR).

%%%%%%%% Ceiling

map_getCeiling(tree(K, V, _, _, _, _), K, V) :- !.
map_getCeiling(tree(RtK, RtV, L, R, _, _), K, V) :- K < RtK, map_getCeiling(L, K, V), !.
map_getCeiling(tree(RtK, RtV, L, R, _, _), K, V) :- K < RtK, V = RtV.
map_getCeiling(tree(RtK, RtV, L, R, _, _), K, V) :- K > RtK, map_getCeiling(R, K, V), !.

map_putCeiling(tree(K, _, L, R, H, B), K, V, tree(K, V, L, R, H, B), true) :- !.
map_putCeiling(tree(RtK, RtV, L, R, H, B), K, V, tree(RtK, RtV, NL, R, H, B), true) :- K < RtK, map_putCeiling(L, K, V, NL, true), !.
map_putCeiling(tree(RtK, RtV, L, R, H, B), K, V, tree(RtK, V, L, R, H, B), true) :- K < RtK.
map_putCeiling(tree(RtK, RtV, L, R, H, B), K, V, tree(RtK, RtV, L, NR, H, B), Success) :- K > RtK, map_putCeiling(R, K, V, NR, Success).
map_putCeiling(tree(RtK, RtV, L, null, H, B), K, V, tree(RtK, RtV, L, null, H, B), false) :- K > RtK.

map_putCeiling(T, K, V, R) :- map_putCeiling(T, K, V, R, _).
map_putCeiling(null, K, V, null).
