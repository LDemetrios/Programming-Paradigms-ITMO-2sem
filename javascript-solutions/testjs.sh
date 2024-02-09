#!/usr/bin/bash

chmod 777 ../kgeorgiy/paradigms-2023/javascript/TestJS.sh

src=.
tests=../kgeorgiy/paradigms-2023/javascript

cp $src/objectExpression.js $tests/objectExpression.js
$tests/TestJS.sh jstest.prefix.PostfixTest hard MeansqRMS
$tests/TestJS.sh jstest.object.ObjectTest hard SumrecHMean
rm $tests/objectExpression.js

cp $src/functionalExpression.js $tests/functionalExpression.js
$tests/TestJS.sh jstest.functional.FunctionalTest hard OneArgMinMax
rm $tests/functionalExpression.js

rm -r __out