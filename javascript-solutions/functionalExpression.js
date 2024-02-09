const argOfMost = predicate => (...args) => [...args.keys()].reduce(
    (res, i) => predicate(args[i], args[res]) ? i : res,
    0
)

const argMin = argOfMost((newEl, oldEl) => newEl < oldEl)
const argMax = argOfMost((newEl, oldEl) => newEl > oldEl)

const cnst = c => () => c

const variables = ["x", "y", "z"]
const variable = varName => (...args) => args[variables.indexOf(varName)]

const nAry = f => (...args) => (x, y, z) => f(...args.map(it => it(x, y, z)))

const one = cnst(1)
const two = cnst(2)
const negate = nAry(x => -x)
const add = nAry((x, y) => x + y)
const subtract = nAry((x, y) => x - y)
const multiply = nAry((x, y) => x * y)
const divide = nAry((x, y) => x / y)
const argMin3 = nAry(argMin)
const argMin5 = argMin3
const argMax3 = nAry(argMax)
const argMax5 = argMax3
const floor = nAry(Math.floor)
const ceil = nAry(Math.ceil)
const madd = nAry((a, b, c) => a * b + c)

const nAryParseFunction = (n, func) => list => func(...([...Array(n).keys()].map(() => parseExpr(list))))

const flipped = (func) => (...args) => func(...args.reverse())

const parseFunctionsTable = {
    one: () => cnst(1),
    two: () => cnst(2),
    x: () => variable('x'),
    y: () => variable('y'),
    z: () => variable('z'),
    '+': nAryParseFunction(2, add),
    '-': nAryParseFunction(2, flipped(subtract)),
    '*': nAryParseFunction(2, multiply),
    '/': nAryParseFunction(2, flipped(divide)),
    argMin3: nAryParseFunction(3, flipped(argMin3)),
    argMin5: nAryParseFunction(5, flipped(argMin5)),
    argMax3: nAryParseFunction(3, flipped(argMax3)),
    argMax5: nAryParseFunction(5, flipped(argMax5)),
    negate: nAryParseFunction(1, negate),
    '_': nAryParseFunction(1, floor),
    '^': nAryParseFunction(1, ceil),
    '*+': nAryParseFunction(3, flipped(madd)),
}

const parseExpr = list => {
    let last = list.pop()
    return parseFunctionsTable[last]?.(list) || cnst(parseFloat(last))
}

const parse = str => parseExpr(str.split(/\s+/).filter(str => str))
