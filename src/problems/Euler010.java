package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler010 {

	/**
	 * Sum of primes below two million
	 */
	
	@Test
	public void test() {
		long r = Stream.iterate(3l, n -> n + 2l).limit(999999) // we're starting at 3, and make steps of 2, so (2*10^6 - 2) / 2
			.filter(n -> isPrime.apply(n)).reduce(2l, (a,b) -> a + b);
		System.out.println(r);
		assertEquals(142913828922l, r);
	}
	
	Function<Long, Boolean> isPrime = n -> {
		if (n < 2) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (long i = 6L; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
		}
		return true;
	};
}
