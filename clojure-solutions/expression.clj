; Math

(defn div
  ([x] (div 1 x))
  ([x y] (/ (double x) (double y)))
  ([x y & more] (reduce div (div x y) more)))

(defn meansq-math [& args]
  (/ (apply + (map #(* % %) args)) (count args)))

(defn rms-math [& args]
  (Math/sqrt (apply meansq-math args)))

(defn exp [x] (Math/exp x))

(defn log [x] (Math/log x))

(defn sumexp [& xs] (apply + (map exp xs)))

(def lse (comp log sumexp))

; ---------------- Functional expressions ----------------

(defn variable [name] (fn [num-args] (num-args name)))
(def constant constantly)
(defn functional [op]
  (fn [& expr-args]
    (fn [num-args]
      (apply op (map #(% num-args) expr-args)))))

(def negate (functional -))
(def add (functional +))
(def subtract (functional -))
(def multiply (functional *))
(def divide (functional div))
(def meansq (functional meansq-math))
(def rms (functional rms-math))

; Parsing

(def function-map
  {'+      add
   '-      subtract
   '*      multiply
   '/      divide
   'negate negate
   'meansq meansq
   'rms    rms})

; :NOTE: refactor
(defn parser-factory [func-map const-gen var-gen]
  (letfn [(functionalize [arg]
            {:post (some? %)}
            (cond
              (number? arg) (const-gen arg)
              (symbol? arg) (var-gen (name arg))
              (list? arg) (apply
                            (func-map (first arg))
                            (map functionalize (rest arg)))))]
    (comp functionalize read-string)))

(def parseFunction (parser-factory function-map constant variable))

; ------------------ Object expressions ------------------

(definterface Expression
  (^Number evaluate [args-map])
  (diff [var])
  (toStringInfix []))

(declare ZERO)

(deftype ConstantImpl [c]
  Expression
  (evaluate [this args] c)
  (diff [this var] ZERO)
  (toStringInfix [this] (str c))
  Object
  (toString [this] (str c)))

(def ZERO (ConstantImpl. 0))
(def ONE (ConstantImpl. 1))
(def MINUS-ONE (ConstantImpl. -1))

(defn first-lower [s] (clojure.string/lower-case (first s)))

(deftype VariableImpl [v]
  Expression
  (evaluate [this args] (args (first-lower v)))
  (diff [this var] (if (= (first-lower v) (first-lower var)) ONE ZERO))
  (toStringInfix [this] v)
  Object
  (toString [this] v))

(defn Constant [c] (ConstantImpl. c))
(defn Variable [v] (VariableImpl. v))                       ; auto-cast to str

(declare Add)
(declare Multiply)
(declare toString)

(deftype Operation [impl symbol derivative-by operands]
  Expression
  (evaluate [this args] (apply impl (map #(.evaluate % args) operands)))
  (diff [this var] (apply Add (map-indexed
                                #(Multiply
                                   (apply (derivative-by %1) operands)
                                   (.diff %2 var))
                                operands)))
  (toStringInfix [this]
    (cond
      (= 1 (count operands)) (str symbol " " (.toStringInfix (first operands)))
      (= 2 (count operands)) (str "(" (.toStringInfix (first operands)) " " symbol " " (.toStringInfix (second operands)) ")")))

  Object
  (toString [this] (str "(" symbol " " (clojure.string/join " " (mapv toString operands)) ")")))

(defn evaluate [expr args] (.evaluate expr args))
(defn diff [expr var] (.diff expr var))
(defn toString [expr] (.toString expr))
(defn toStringInfix [expr] (.toStringInfix expr))

(defn partial-Operation [impl symbol derivative-by]
  (fn [& operands] (Operation. impl symbol derivative-by operands)))

(def Negate (partial-Operation - 'negate (constantly (constantly MINUS-ONE))))

(def Add (partial-Operation + '+ (constantly (constantly ONE))))

(def Multiply
  (partial-Operation
    * '*
    #(fn [& operands]
       (apply Multiply (concat (take % operands) (drop (inc %) operands))))))

(def Subtract
  (partial-Operation
    - '-
    (fn [i]
      (fn [& operands]
        (if (and (not (= (count operands) 1)) (= i 0))
          ONE
          MINUS-ONE)))))

(def Divide
  (partial-Operation
    div '/
    #(fn [& operands]
       (cond
         (= (count operands) 1) (Divide MINUS-ONE (first operands) (first operands))
         (= % 0) (apply Divide ONE (rest operands))
         :else (Negate (Divide (apply Divide operands) (nth operands %)))))))

(def Sumexp
  (partial-Operation
    sumexp 'sumexp
    (fn [i]
      (fn [& operands]
        (Sumexp (nth operands i))))))

(def LSE
  (partial-Operation
    lse 'lse
    (fn [i]
      (fn [& operands]
        (Divide (Sumexp (nth operands i)) (apply Sumexp operands))))))

(defmacro apply-b-op [op args] `(~op ~@args))

(defn boolean-op [b-impl symbol]
  (partial-Operation (fn [& args] (if (apply b-impl (map #(> % 0) args)) 1 0)) symbol (constantly (constantly ##NaN))))

(defn xor
  ([] false)
  ([a] a)
  ([a b] (or (and a (not b)) (and b (not a))))
  ([a b & next] (reduce xor (xor a b) next)))

(defn and-fn [& args] (reduce #(and %1 %2) args))
(defn or-fn [& args] (reduce #(or %1 %2) args))

(def And (boolean-op and-fn '&&))
(def Or (boolean-op or-fn '||))
(def Xor (boolean-op xor (symbol "^^")))
(def Iff (boolean-op (comp not xor) '<->))
(def Not (boolean-op not '!))
(def Impl (boolean-op #(or %2 (not %1)) '->))

(def object-map {'+            Add
                 '-            Subtract
                 '*            Multiply
                 '/            Divide
                 'negate       Negate
                 'sumexp       Sumexp
                 'lse          LSE
                 '&&           And
                 '||           Or
                 (symbol "^^") Xor
                 '!            Not
                 '<->          Iff
                 '->           Impl
                 })

(def parseObject (parser-factory object-map Constant Variable))

; ------------------ Parser combinators ------------------

(load-file "parser.clj")

(defmacro +char-j [form] `(_char #(~form %)))

(defn +exactly [value]
  {:post [(some? %)]}
  (cond
    (string? value) (apply +seqf str (map #(+char (str %)) value))
    (symbol? value) (+map symbol (+exactly (name value)))
    (keyword? value) (+map keyword (+exactly (name value)))))

(defn +opt                                                  ; Override
  ([parser] (+opt parser nil))
  ([parser else] (+or parser (_empty else))))

(defparser parseObjectInfix
           *ws (+ignore (+star (+char-j Character/isWhitespace)))
           (*padding [parser] (+seqn 0 *ws parser *ws))
           *variable (+seqf (comp Variable str) (+char "xyzXYZ") (+str (+star *letter)))
           *letter (+char-j Character/isLetter)
           *digit (+char-j Character/isDigit)
           *number (+map (comp Constant read-string)
                         (+seqf str
                                (+opt (+char "+-") "")
                                (+str (+plus *digit))
                                (+opt (+seqf str \. (+plus *digit)))))
           (*op [& symbols] (+map object-map (apply +or (map #(+exactly %) symbols))))
           (*binary-reducer [operand-p & op-symbols]
                            (+map
                              (partial reduce #(%2 %1))
                              (+seqf cons operand-p (+star (+seqf
                                                             #(fn [l] (%1 l %2))
                                                             (apply *op op-symbols) operand-p)))))
           (*binary-reducer-right [operand-p & op-symbols]
                                  (+seqf #(%1 %2)
                                         (+map
                                           (partial apply comp)
                                           (+star (+seqf #(partial %2 %1) operand-p (apply *op op-symbols))))
                                         operand-p))

           *formula (*binary-reducer *clause0 '<->)
           *clause0 (*binary-reducer-right *clause1 '->)
           *clause1 (*binary-reducer *clause2 (symbol "^^"))
           *clause2 (*binary-reducer *clause3 '||)
           *clause3 (*binary-reducer *clause4 '&&)
           *clause4 (*binary-reducer *term '+ '-)
           *term (*binary-reducer *unit '* '/)
           *unary-op (+seqf #(%1 %2) (*op 'negate '!) *unit)
           *unit (delay (*padding (+or *variable *number (+seqn 1 \( *formula \)) *unary-op)))
           *parseObjectInfix *formula)
