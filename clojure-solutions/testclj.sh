#!/usr/bin/bash

chmod 777 ../kgeorgiy/paradigms-2023/clojure/TestClojure.sh

src=.
tests=../kgeorgiy/paradigms-2023/clojure

cp $src/linear.clj $tests/linear.clj
$tests/TestClojure.sh cljtest.linear.LinearTest hard Broadcast
rm $tests/linear.clj

cp $src/parser.clj $tests/parser.clj
cp $src/expression.clj $tests/expression.clj
$tests/TestClojure.sh cljtest.functional.FunctionalTest hard MeansqRMS
$tests/TestClojure.sh cljtest.object.ObjectTest hard SumexpLSE
$tests/TestClojure.sh cljtest.parsing.ParserTest hard ImplIff
rm $tests/expression.clj
rm $tests/parser.clj

rm -r __OUT
