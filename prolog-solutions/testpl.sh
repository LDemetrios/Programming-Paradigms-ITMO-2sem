#!/usr/bin/bash

chmod 777 ../kgeorgiy/paradigms-2023/prolog/TestProlog.sh

src=.
tests=../kgeorgiy/paradigms-2023/prolog

cp $src/primes.pl $tests/primes.pl
# I didn't complete it well
$tests/TestProlog.sh prtest.primes.PrimesTest easy Primes # Divisors
rm $tests/primes.pl

cp $src/expression.pl $tests/expression.pl
$tests/TestProlog.sh prtest.parsing.ParserTest infix VarImplIff
rm $tests/expression.pl

cp $src/tree-map.pl $tests/tree-map.pl
$tests/TestProlog.sh prtest.tree.TreeTest hard PutCeiling
rm $tests/tree-map.pl

rm -r __OUT
