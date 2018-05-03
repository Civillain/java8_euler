package utils;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EulerUtils {
	
	private static final double LOG2 = Math.log(2.0);
	
	public static final Function<Stream<BigInteger>, Function<Function<BigInteger, Boolean>, BigInteger>> fibonacciWithStop = 
				 fibs -> stopCondition -> {
		Optional<BigInteger> result = fibs
									.filter(f -> stopCondition.apply(f))
									.findFirst();
		return result.orElse(BigInteger.ZERO);
	};
	
	public static final Function<Stream<Entry<Integer, BigInteger>>, Function<Function<BigInteger, Boolean>, Integer>> fibonacciWithStopIdx = 
			 fibs -> stopCondition -> fibs.filter(f -> stopCondition.apply(f.getValue()))
			 								//.peek(System.out::println)
	 										.map(e -> e.getKey())
	 										.findFirst()
	 										.orElse(-1);
			 
	public static final Supplier<Stream<BigInteger>> fibonacci = () ->  {
		Stream<BigInteger> result = Stream
									.iterate(new BigInteger[] { BigInteger.ONE, BigInteger.ONE },
											p -> new BigInteger[] { p[1], p[0].add(p[1]) })
									.map(p -> p[1]);
		return result;
	};
	
	
	
	public static final Function<BigInteger, BigInteger> countDigits = b -> {
		if(b.equals(BigInteger.ZERO))
			return BigInteger.ZERO;
		return BigInteger.ONE.add(EulerUtils.countDigits.apply(b.divide(BigInteger.TEN)));
	};
	
	public static <A, B, C> Function<A, Function<B, C>> curry(final BiFunction<A, B, C> f) {
		return (A a) -> (B b) -> f.apply(a, b);
	}
	
	public static <A, B, C> BiFunction<A, B, C> uncurry(Function<A, Function<B, C>> f) {
		return (A a, B b) -> f.apply(a).apply(b); 
	}
	 
	
	public static List<Integer> divisors(final Integer number) {
		int half = Double.valueOf(Math.sqrt(number)).intValue();
		Stream<Tuple<Integer, Integer>> results = Stream.iterate(1, n -> n + 1)
					.limit(half)
					.filter(i -> number % i == 0)
					.map(i -> new Tuple<>(i, number / i));
		
		List<Integer> divisors = new ArrayList<>();
		results.forEach(t -> {
			divisors.add(t.first);
			divisors.add(t.second);
		});
		divisors.sort(Integer::compareTo);
		divisors.remove(number);
		return divisors;
	}
	
	public static double log2(BigInteger val) {
		int blex = val.bitLength() - 1022;
		if(blex > 0)
			val = val.shiftRight(blex);
		double res = Math.log(val.doubleValue());
		return blex > 0 ? res + blex * LOG2 : res;
	}

	public static BigInteger expBySquaring(Integer x, Integer n) {
		return expBySquaring(BigInteger.ONE, BigInteger.valueOf(x), BigInteger.valueOf(n)).invoke();
	}
	
	private static TailCall<BigInteger> expBySquaring(BigInteger y, BigInteger x, BigInteger n) {
		if( n.compareTo(BigInteger.ZERO) < 0) {  // n < 0
			return call(() -> expBySquaring(y, BigInteger.ONE.divide(x), n.multiply(BigInteger.valueOf(-1))));
		} else if ( n.compareTo(BigInteger.ZERO) == 0) { // n == 0
			return done(y);
		} else if ( n.compareTo(BigInteger.ONE) == 0) { // n == 1
			return done(x.multiply(y));
		} else if ( n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) { // n == even
			return call(() -> expBySquaring(y, x.multiply(x), n.divide(BigInteger.valueOf(2))));
		} else if ( n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) > 0) { // n == odd
			return call(() -> expBySquaring(y.multiply(x), x.multiply(x), (n.subtract(BigInteger.ONE)).divide(BigInteger.valueOf(2))));
		} else {
			throw new IllegalArgumentException("?");
		}
	}
	
	
	public static BigInteger digitSum(final BigInteger n) {
		BigInteger sum = BigInteger.ZERO;
		BigInteger number = BigInteger.ZERO.add(n); // create a copy
		while(number.compareTo(BigInteger.ZERO) > 0) {
			BigInteger digit= number.mod(BigInteger.TEN);
			number = number.divide(BigInteger.TEN);
			sum = sum.add(digit);
		}
		return sum;
	}
	
	public static Map<BigInteger, BigInteger> factorialTable(int n) {
		Map<BigInteger, BigInteger> factorials = 
					Stream.iterate(new Tuple<>(BigInteger.ONE, BigInteger.ONE), 
							x -> new Tuple<>(x.first.add(BigInteger.ONE), 
												x.second.multiply(x.first.add(BigInteger.ONE))))
					.limit(n)
					.collect(Collectors.toMap(x -> x.first, 
										x -> x.second, 
										(a, b) -> { throw new IllegalStateException(String.format("Duplicate key %s", a)); }, 
										TreeMap::new));
		return factorials;
	}
	
	public static long factNoverM(long n, long m) {
		long result = 1;
		for(long factor = n; factor > m; factor--) {
			result *= factor;
		}
		return result;
	}
	
	
	public static long gcd(long a, long b) {
		if(b == 0) return a;
		return gcd(b, a%b);
	}
	
	public static long lcm(long a, long b) {
		return a*b / gcd(a, b);
	}
	
	public static boolean coprime(long a, long b) {
		return gcd(a, b) == 1;
	}
	public static long totient(long n) {
		int count = 0;
		for (int i = 1; i < n; i++) { // definition of totient: the amount of
										// numbers less than n coprime to it
			if (coprime(n, i)) {
				count++;
			}
		}
		return count;
	}

	public static boolean isPrime(long n) {
		if (n < 2) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (long i = 6L; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
		}
		return true;
	}

	@SafeVarargs
	public static <T> Stream<T> zip(BinaryOperator<T> aggregator, Supplier<Stream<T>>... streams) {
		return Arrays
				.stream(streams)
				.reduce((s1, s2) -> () -> s1.get().flatMap(
						t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
				.orElse(Stream::empty).get();
	}
	
	/**
	 * Function for taill call optimization
	 * @author rko
	 *
	 * @param <T>
	 */
	@FunctionalInterface
	public interface TailCall<T> {

		TailCall<T> apply();
		
		default boolean isComplete() { return false; }
		
		default T result() { throw new Error("not implemented"); }
		
		default T invoke() {
			return Stream.iterate(this, TailCall::apply)
						.filter(TailCall::isComplete) 
						.findFirst()
						.get()
						.result();
		}
	}
	
	public static <T> TailCall<T> call(final TailCall<T> nextCall) {
		return nextCall;
	}
	
	public static <T> TailCall<T> done(final T value) {
		return new TailCall<T>() {
			@Override public boolean isComplete() { return true; }
			@Override public T result() { return value; }
			@Override public TailCall<T> apply() {
				throw new Error("not implemented"); 
			}
		};
	}
	
	/**
	 * The Try Monad
	 * @author rko
	 *
	 * @param <V>
	 */
	public abstract static class Try<V> {

		private Try() {
			
		}
		
		public abstract Boolean isSuccess();
		
		public abstract Boolean isFailure();
		
		public abstract void throwException();
		
		public abstract V get() throws Throwable;
		
		public static <V> Try<V> failure(Throwable t) {
			Objects.requireNonNull(t);
			return new Failure<>(t);
		}
		
		public static <V> Try<V> success(V value) {
			Objects.requireNonNull(value);
			return new Success<>(value);
		}
		
		public static <T> Try<T> failable(CheckedSupplier<T> f) {
			Objects.requireNonNull(f);
			
			try {
				return Try.success(f.get());
			} catch (Throwable t) {
				return Try.failure(t);
			}
		}
		
		private static class Failure<V> extends Try<V> {

		    private RuntimeException exception;
			
		    public Failure(Throwable t) {
		    	super();
		    	this.exception = new RuntimeException(t);
		    }
		    
			@Override
			public Boolean isSuccess() {
				return false;
			}

			@Override
			public void throwException() {
				throw this.exception;
			}

			@Override
			public V get() throws Throwable {
				throw exception;
			}

			@Override
			public Boolean isFailure() {
				return true;
			}
		}
		
		private static class Success<V> extends Try<V> {

			private final V value;
			
			public Success(V value) {
				super();
				this.value = value;
			}
			
			@Override
			public Boolean isSuccess() {
				return true;
			}

			@Override
			public void throwException() {
				return;
			}

			@Override
			public V get() throws Throwable {
				return value;
			}

			@Override
			public Boolean isFailure() {
				return false;
			}
		}
	}
	
	/**
	 * A Tuple of two values
	 * @author rko
	 *
	 * @param <T>
	 * @param <U>
	 */
	public static class Tuple<T, U> {
		public final T first;
		public final U second;

		public Tuple(T first, U second) {
			this.first = first;
			this.second = second;
		}
	}
	
	public static class Pair<T, U> {
		public T first;
		public U second;
		public Pair(T first, U second) {
			this.first = first;
			this.second = second;
		}
		public Pair<T, U> setFirst(T first) {
			this.first = first;
			return this;
		}
		public Pair<T, U> setSecond(U second) {
			this.second = second;
			return this;
		}
	}
	
	/**
	 * A value type
	 * @author rko
	 *
	 * @param <T>
	 */
	public static class Value<T> {
		private T value;

		Value(T value) {
			this.value = value;
		}

		T get() {
			return value;
		}

		void set(T value) {
			this.value = value;
		}
	}
	
	@FunctionalInterface
	public interface CheckedSupplier<T> {
		T get() throws Throwable;
	}
	
	/**
	 * Memoization of a function
	 * @author rko
	 *
	 * @param <T> 
	 * @param <U>
	 */
	public static class Memoizer<T, U> {
		private final Map<T, U> cache = new ConcurrentHashMap<>();
		
		private Memoizer() {}
		
		private Function<T, U> doMemoize(final Function<T, U> function) {
			return input -> cache.computeIfAbsent(input, function::apply);
		}
		
		public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
			return new Memoizer<T, U>().doMemoize(function);
		}
		
	}
	
	/**
	 * 
	 * @author rko
	 *
	 * @param <T> A Node class to implement directed graphs (or trees)
	 */
	public static class Node<T> {
		private final T value;
		private List<Node<T>> children;
		private List<Node<Integer>> parents;
		public Node(final T value) {
			this.value = value;
			this.children = new ArrayList<>();
		}
		public T getValue() {
			return value;
		}
		public List<Node<T>> getChildren() {
			return children;
		}
		public boolean isLeaf() {
			return children == null;
		}
		public void addChild(final Node<T> child) {
			this.children.add(child);
		}
		public void addChildren(@SuppressWarnings("unchecked") final Node<T>... children) {
			this.children.addAll(Arrays.asList(children));
		}
		public void addParent(Node<Integer> parent) {
			this.parents.add(parent);
		}
		public List<Node<Integer>> getParents() {
			return this.parents;
		}
		public Node<T> getChild(final int pos) {
			return children.get(pos);
		}
		public boolean hasChildren() {
			return children != null && !children.isEmpty();
		}
	}
	
	/**
     * Converts an {@link java.util.Iterator} to {@link java.util.stream.Stream}.
     */
    public static <T> Stream<T> iterate(Iterator<? extends T> iterator) {
        int characteristics = Spliterator.ORDERED | Spliterator.IMMUTABLE;
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, characteristics), false);
    }

    /**
     * Zips the specified stream with its indices.
     */
    public static <T> Stream<Map.Entry<Integer, T>> zipWithIndex(Stream<? extends T> stream) {
        return iterate(new Iterator<Map.Entry<Integer, T>>() {
            private final Iterator<? extends T> streamIterator = stream.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return streamIterator.hasNext();
            }

            @Override
            public Map.Entry<Integer, T> next() {
                return new AbstractMap.SimpleImmutableEntry<>(index++, streamIterator.next());
            }
        });
    }

    /**
     * Returns a stream consisting of the results of applying the given two-arguments function to the elements of this stream.
     * The first argument of the function is the element index and the second one - the element value. 
     */
    public static <T, R> Stream<R> mapWithIndex(Stream<? extends T> stream, BiFunction<Integer, ? super T, ? extends R> mapper) {
        return zipWithIndex(stream).map(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }
    
    public static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }
    
    public static void print(String msg) {
    	System.out.println(msg);
    }

}
