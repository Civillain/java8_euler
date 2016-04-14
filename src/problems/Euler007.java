package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler007 {

	/**
	 * What is the 10 001st prime number?
	 */
	@Test
	public void test() {
		
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
		
		int r = Stream.iterate(3, n -> n + 2)
			.filter(n -> isPrime.apply((long)n))
			.limit(10000)
			.reduce(0, (a, b) -> b);
		
		System.out.println(r);
		
		assertEquals(104743, r);
	}
}
