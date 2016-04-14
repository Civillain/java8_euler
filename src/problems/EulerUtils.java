package problems;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EulerUtils {

	static long gcd(long a, long b) {
		if(b == 0) return a;
		return gcd(b, a%b);
	}
	
	static long lcm(long a, long b) {
		return a*b / gcd(a, b);
	}
	
	static boolean coprime(long a, long b) {
		return gcd(a, b) == 1;
	}
	static long totient(long n) {
		int count = 0;
		for (int i = 1; i < n; i++) { // definition of totient: the amount of
										// numbers less than n coprime to it
			if (coprime(n, i)) {
				count++;
			}
		}
		return count;
	}

	static boolean isPrime(long n) {
		if (n < 2) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (long i = 6L; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
		}
		return true;
	}

	static <T> Stream<T> cartesian(BinaryOperator<T> aggregator, Supplier<Stream<T>>... streams) {
		return Arrays
				.stream(streams)
				.reduce((s1, s2) -> () -> s1.get().flatMap(
						t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
				.orElse(Stream::empty).get();
	}

	class Tuple<T, U> {
		public final T first;
		public final U second;

		Tuple(T first, U second) {
			this.first = first;
			this.second = second;
		}
	}

	class Value<T> {
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
}
