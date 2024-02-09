(defn num-vector? [x] (and (vector? x) (every? number? x)))
(defn v-same-size? [& vs] (or (empty? vs) (apply = (map count vs))))
(defn matrix? [x]
  (and
    (vector? x)
    (every? num-vector? x)
    (apply v-same-size? x)))
(defn m-same-size? [& ms]
  (and
    (apply v-same-size? ms)
    (apply v-same-size? (mapcat identity ms))))

(defn t-same-size? [& x]
  (cond
    (every? number? x) true
    (every? num-vector? x) (apply v-same-size? x)
    (not-any? number? x) (and
                           (apply = (map count x))
                           (or
                             (empty? x)
                             (empty? (first x))
                             (apply t-same-size? (map first x))))
    :else false))

(defn tensor? [x]
  (or
    (number? x)
    (num-vector? x)
    (and
      (vector? x)
      (every? tensor? x)
      (apply t-same-size? x))))
(defn rows-num [m] (count m))
(defn cols-num [m] (if (zero? (rows-num m)) 0 (count (first m))))
(defn element [m r c] (nth (nth m r) c))

(defn zip [op & args]
  (if (every? num-vector? args)
    (apply mapv op args)
    (apply mapv (partial zip op) args)))

(defn linear-fun [type? same-size? op & args]
  {:pre [(every? type? args)
         (apply same-size? args)]}
  (apply zip op args))
(defn vect-fun [op] (partial linear-fun num-vector? v-same-size? op))
(defn matrix-fun [op] (partial linear-fun matrix? m-same-size? op))
(defn tensor-fun [op]
  (let [t-fun (partial linear-fun tensor? t-same-size? op)]
    (fn [& args]
      (if (some number? args)
        (apply op args)
        (apply t-fun args)))))

(def v+ (vect-fun +))
(def v- (vect-fun -))
(def v* (vect-fun *))
(def vd (vect-fun /))
(def m+ (matrix-fun +))
(def m- (matrix-fun -))
(def m* (matrix-fun *))
(def md (matrix-fun /))
(def t+ (tensor-fun +))
(def t- (tensor-fun -))
(def t* (tensor-fun *))
(def td (tensor-fun /))

(defn v*s [t & ss]
  {:pre [(every? number? ss)]}
  (let [s (apply * ss)] (mapv (partial * s) t)))

(defn m*s [t & ss]
  {:pre [(every? number? ss)]}
  (let [s (apply * ss)] (mapv (partial mapv (partial * s)) t)))

(def transpose (partial apply mapv vector))

(defn scalar [& vs]
  {:pre [(every? num-vector? vs)]}
  (apply + (apply v* vs)))

(defn m*v [m v]
  (if (empty? v)
    (mapv (constantly 0.0) (range 0 (rows-num m)))
    (mapv (partial scalar v) m)))

(defn m*m [& ms]
  (letfn [(m*m2 [a b]
            {:pre [(matrix? a)
                   (matrix? b)
                   (= (cols-num a) (rows-num b))]}
            (transpose (mapv (partial m*v a) (transpose b))))]
    (reduce m*m2 ms)))

(defn vect [& vs]
  {:pre [(every? num-vector? vs)
         (every? #(= (count %) 3) vs)]}
  (letfn [(vect2 [v u]
            (let [[ax ay az] v
                  [bx by bz] u]
              [(- (* ay bz) (* az by))
               (- (* az bx) (* ax bz))
               (- (* ax by) (* bx ay))]))]
    (reduce vect2 vs)))

(defn t-form [t]
  (cond
    (number? t) ()
    (empty? t) '(0)
    :else (conj (t-form (first t)) (count t))))

(defn broadcastable [a b]
  (let [aform (t-form a)
        bform (t-form b)]
    (and
      (<= (count aform) (count bform))
      (every?
        #(= (nth aform %) (nth bform %))
        (range 0 (count aform))))))

(defn broadcast [a b]
  (if (= (t-form a) (t-form b))
    a
    (letfn [(broadcast1 [t n]
              (if (number? t)
                (vec (repeat n t))
                (mapv #(broadcast1 % n) t)))]
      (reduce
        #(broadcast1 %1 %2)
        a
        (nthrest (t-form b) (count (t-form a)))))))

(defn tb-op [op & args]
  (if (= (count args) 1)
    (op (first args))
    (letfn [(tb-op2 [op a b]
              (if (broadcastable a b)
                (op (broadcast a b) b)
                (op a (broadcast b a))))]
      (reduce #(tb-op2 op %1 %2) args))))

(def tb+ (partial tb-op t+))
(def tb- (partial tb-op t-))
(def tb* (partial tb-op t*))
(def tbd (partial tb-op td))
