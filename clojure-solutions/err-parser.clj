; This file should be placed in clojure-solutions
; You may use it via (load-file "parser.clj")


; ------------------ Character predicates ------------------

(def ?one-of set)
(defn ?in-range [first last] #(and (<= (int first) (int %)) (< (int last) (int %))))
(defn ?in-range-inc [first last] #(and (<= (int first) (int %)) (<= (int last) (int %))))

; ------------------ Result manipulations ------------------

(def void (Object.))

(defn -return [value tail] {:value value :tail tail})
(defn -throw [& error-details] {:desc (str error-details)})
(def -fail nil)

(defn -valid? [x] (not (identical? -fail x)))
(def -value :value)
(def -tail :tail)

(defn -success? [x] (contains? x :value))
(defn -error? [x] (contains? x :desc))
(def -failure? (partial identical? -fail))

(def -value :value)
(def -tail :tail)
(def -description :desc)

(def -ignore? (partial identical? void))

; ----------------- Basic parser functions -----------------

(defn _empty [value] (partial -return value))

(defn _char [predicate]
  (fn [[head & tail]]
    (if (and head (predicate head)) (-return head tail) -fail)))

(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)) -fail)))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
         ((force b) (-tail ar))) -fail))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [parser]
  (fn [input]
    (let [res (parser input)] (if (empty? (-tail res)) (-value res)))))
(mapv (_parser (_combine str (_char #{\a \b}) (_char #{\x}))) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])

; ---------------------- Combinations ----------------------

(def +char _char)
(defmacro +char-j [form] `(+char #(~form %)))
(def +char-not (comp _char (partial comp not)))
(defmacro +char-not-j [form] `(+char-not #(~form %)))

(defn +map [f parser] (comp (_map f) parser))
(def +ignore (partial +map (constantly void)))

(defn iconj [coll value]
  (if (-ignore? value) coll (conj coll value)))

(defn +seq [& parsers]
  (reduce (partial _combine iconj) (_empty []) parsers))

(defn +seqf [f & parsers] (+map (partial apply f) (apply +seq parsers)))

(defn +seqn [n & parsers] (apply +seqf (fn [& vs] (nth vs n)) parsers))

(defn +or [parser & parsers]
  (reduce (partial _either) parser parsers))

(defn +opt [parser]
  (+or parser (_empty nil)))

(defn +star [parser]
  (letfn [(rec [] (+or (+seqf cons parser (delay (rec))) (_empty ())))] (rec)))

(defn +plus [parser] (+seqf cons parser (+star parser)))

(defn +str [parser] (+map (partial apply str) parser))

(def +parser _parser)

(defn +rules [defs]
  (cond
    (empty? defs) ()
    (seq? (first defs)) (let [[[name args body] & tail] defs]
                          (cons
                            {:name name :args args :body body}
                            (+rules tail)))
    :else (let [[name body & tail] defs]
            (cons
              {:name name :args [] :body body :plain true}
              (+rules tail)))))

(defmacro defparser [name & defs]
  (let [rules (+rules defs)
        plain (set (map :name (filter :plain rules)))]
    (letfn [(rule [{name :name, args :args, body :body}] `(~name ~args ~(convert body)))
            (convert [value]
              (cond
                (seq? value) (map convert value)
                (char? value) `(+char #{~value})
                (contains? plain value) `(~value)
                :else value))]
      `(def ~name (letfn ~(mapv rule rules) (+parser (~(:name (last rules)))))))))

; ------------------------- Sample -------------------------

(defparser json
           *null (+seqf (constantly 'null) \n \u \l \l)
           *all-chars (mapv char (range 0 128))
           *letter (+char-j Character/isLetter)
           *digit (+char-j Character/isDigit)
           *space (+char-j Character/isWhitespace)
           *ws (+ignore (+star *space))
           *number (+map read-string (+str (+plus *digit)))
           *identifier (+str (+seqf cons *letter (+star (+or *letter *digit))))
           *string (+seqn 1 \" (+str (+star (+char-not #{\"}))) \")
           (*seq [begin p end]
                 (+seqn 1 begin (+opt (+seqf cons *ws p (+star (+seqn 1 *ws \, *ws p)))) *ws end))
           *array (+map vec (*seq \[ (delay *value) \]))
           *member (+seq *identifier *ws (+ignore \:) *ws (delay *value))
           *object (+map (partial reduce #(apply assoc %1 %2) {}) (*seq \{ *member \}))
           *value (+or *null *number *string *object *array)
           *json (+seqn 0 *ws *value *ws))
(json "[1, {a: \"hello\", b: [1, 2, 3]}, null]")
(json "[1, {a: \"hello\", b: [1, 2, 3]}, null]~")
