Array.prototype.sumOf = function (mapping) {
    return this.map(mapping).reduce((a, b) => a + b, 0)
};
const range = (from, to) => [...Array(to - from).keys()].map(it => it + from);
const filled = (n, elementSupplier) => range(0, n).map(elementSupplier);

function ParsingError(message) {
    this.message = message
}

ParsingError.prototype = Object.create(Error.prototype)
ParsingError.prototype.constructor = ParsingError
ParsingError.prototype.name = "ParsingError"

const AbstractFunctionPrototype = {
    toString: function () {
        return this.params.map(it => it.toString()).join(" ") + " " + this.symbol.replaceAll("$N", this.params.length)
    },
    diff: function (over) {
        return this.deriv(over, ...this.params)
    },
    evaluate: function (x, y, z) {
        return this.impl(...this.params.map(it => it.evaluate(x, y, z)))
    },
    prefix: function () {
        return "(" + this.symbol.replaceAll("$N", this.params.length) +
            " " + this.params.map(it => it.prefix()).join(" ") + ")"
    },
    postfix: function () {
        return "(" + this.params.map(it => it.postfix()).join(" ") +
            " " + this.symbol.replaceAll("$N", this.params.length) + ")"
    },
    isExpression: true
}

function generate(symbol, impl, deriv) {
    function Function(...args) {
        this.params = args
    }

    Function.prototype = Object.create(AbstractFunctionPrototype)
    Function.prototype.constructor = Function
    Function.prototype.symbol = symbol
    Function.prototype.impl = impl
    Function.prototype.deriv = deriv

    return Function
}

function Variable(v) {
    this.v = v
}

Variable.prototype.evaluate = function (...args) {
    return args[["x", "y", "z"].indexOf(this.v)]
}
Variable.prototype.toString = function () {
    return this.v
}
Variable.prototype.diff = function (over) {
    return this.v === over ? new Const(1) : new Const(0)
}
Variable.prototype.prefix = Variable.prototype.toString
Variable.prototype.postfix = Variable.prototype.toString
Variable.prototype.isExpression = true


function Const(c) {
    this.c = c
}

Const.prototype.toString = function () {
    return this.c.toString()
}
Const.prototype.evaluate = function () {
    return this.c
}
Const.prototype.diff = () => new Const(0)
Const.prototype.prefix = Const.prototype.toString
Const.prototype.postfix = Const.prototype.toString
Const.prototype.isExpression = true

const Negate = generate("negate", (a) => -a, (over, a) => new Negate(a.diff(over)))
const Add = generate("+", (a, b) => a + b, (over, a, b) => new Add(a.diff(over), b.diff(over)))
const Subtract = generate(
    "-", (a, b) => a - b,
    (over, a, b) => new Subtract(a.diff(over), b.diff(over))
)
const Multiply = generate(
    "*", (a, b) => a * b,
    (over, a, b) => new Add(new Multiply(a.diff(over), b), new Multiply(a, b.diff(over)))
)

const Divide = generate(
    "/", (a, b) => a / b,
    (over, u, v) => new Divide(
        new Subtract(new Multiply(u.diff(over), v), new Multiply(v.diff(over), u)),
        new Multiply(v, v)
    )
)

function derivSumrec(over, ...args) {
    return new Negate(
        new Sumrec(...args.map(it => new Divide(new Multiply(it, it), it.diff(over))))
    )
}

const Sumrec = generate("sumrec$N",
    (...args) => args.sumOf(it => 1 / it),
    derivSumrec,
)
const HMean = generate(
    "hmean$N",
    (...args) => args.length / args.sumOf(it => 1 / it),
    (over, ...args) => new Divide(
        new Multiply(new Const(-args.length), derivSumrec(over, ...args)),
        new Multiply(new Sumrec(...args), new Sumrec(...args))
    )
)

function derivMeansq(over, ...args) {
    return new Divide(
        args.map(it => new Multiply(new Const(2), new Multiply(it, it.diff(over))))
            .reduce((a, b) => new Add(a, b)),
        new Const(args.length),
    )
}

const Meansq = generate(
    "meansq", (...args) => args.sumOf(it => it * it) / args.length,
    derivMeansq
)
const RMS = generate(
    "rms", (...args) => Math.sqrt(args.sumOf(it => it * it) / args.length),
    (over, ...args) => new Divide(
        derivMeansq(over, ...args),
        new Multiply(new Const(2), new RMS(...args))
    )
)

const redefineVariableArityFunction = (arityFrom, arityTo, name, func) =>
    range(arityFrom, arityTo).forEach(it => globalThis[name + it] = func)

redefineVariableArityFunction(2, 6, "Sumrec", Sumrec)
redefineVariableArityFunction(2, 6, "HMean", HMean)

const functionsTable = {
    '+': [2, Add],
    '-': [2, Subtract],
    '*': [2, Multiply],
    '/': [2, Divide],
    negate: [1, Negate],
    sumrec: [undefined, Sumrec],
    hmean: [undefined, HMean],
    meansq: [undefined, Meansq],
    rms: [undefined, RMS],
}

const parseExpr = list => {
    let last = list.pop()
    if (last.length === 1 && ('x' <= last && last <= 'z')) return new Variable(last)
    let [arity, func] = functionsTable[last] ?? []
    if (func === undefined) {
        // Try interpreting as N-ary function
        let c = parseFloat(last)
        if (!isNaN(c)) return new Const(c)
    }
    if (func === undefined) {
        // Try interpreting as N-ary function
        arity = parseInt(last.slice(-1)) // NaN if unable
        if (2 <= arity && arity <= 5) { // False for NaN
            func = functionsTable[last.slice(0, -1)]?.[1]
        }
    }
    return new func(...filled(arity, () => parseExpr(list)).reverse())
}

const parse = str => parseExpr(str.split(/\s+/).filter(Boolean))

const parseExprClause = (prefixMode, tokens, from) => { //Returns [expression, end]
    if (from >= tokens.length) throw new ParsingError("Unexpected end, probably unclosed parentheses")
    if (tokens[from] === ")") throw new ParsingError("Unexpected token \")\":" + range(from - 2, from).map(it => tokens[it]).filter(Boolean).join(" "))
    if (tokens[from] === "(") {
        let entries = []
        let end = from + 1
        while (tokens[end] !== ")") {
            let [entry, newEnd] = parseExprClause(prefixMode, tokens, end)
            entries.push(entry)
            end = newEnd
        }
        end = end + 1
        if (entries.length === 0) throw new ParsingError("Empty parentheses: " + range(end - 2, end + 2).map(it => tokens[it]).filter(Boolean).join(" "))
        let arity = entries.length - 1;

        let func = entries[prefixMode ? 0 : entries.length - 1]
        let args = prefixMode ? entries.slice(1) : entries.slice(0, entries.length - 1)
        if (!args.every(it => it.isExpression)) {
            throw new ParsingError("No functions expected as arguments")
        }
        if(func.isExpression) {
            throw new ParsingError("Function expected at " + (prefixMode ? "first" : "last") + " place, but "+ tokens[from + (prefixMode ? 1 : entries.length)] + " appeared")
        }
        if (func[0] !== undefined && func[0] !== arity) {
            throw new ParsingError("Expected arity for function " + tokens[from + (prefixMode ? 1 : entries.length)] + " is " + func[0] + ", but not " + arity)
        }
        return [new func[1](...args), end]
    } else {
        let token = tokens[from] //Single token
        if (token.length === 1 && 'x' <= token && token <= 'z') {
            return [new Variable(token), from + 1]
        } else if (!isNaN(token) && token !== "") { // Whitespace symbols can't appear here
            return [new Const(parseFloat(token)), from + 1]
        } else {
            let func = functionsTable[token]
            if (func === undefined) throw new ParsingError("Unresolved token " + token)
            return [func, from + 1]
        }
    }
}

const parseClojureStyle = (prefixMode, str) => {
    let tokens = str.replaceAll("(", " ( ").replaceAll(")", " ) ").trim().split(/\s+/)
    if(tokens.length === 0) {
        throw new ParsingError("Blank or empty input")
    }
    let [expression, end] = parseExprClause(prefixMode, tokens, 0)
    if (end !== tokens.length) throw new ParsingError("Unexpected tokens after token " + end + ": " + tokens.join(" "))
    return expression
}

const parsePrefix = str => parseClojureStyle(true, str)
const parsePostfix = str => parseClojureStyle(false, str)
