package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler003 {

	@Test
	public void test() {
		final long n = 600851475143l;
		long n_sqrt = (long) Math.sqrt(n);
		long r = Stream.iterate(n_sqrt, i -> i -1)
			.limit(n_sqrt)
			.filter(i -> n % i == 0)
			.filter(p -> isPrime.apply(p))
			.findFirst().get();
		
		System.out.println(r);
		
		assertEquals(6857, r);
	}
	
	Function<Long, Boolean> isPrime = p -> {
		if (p < 2) return false;
		if (p == 2 || p == 3) return true;
		if (p % 2 == 0 || p % 3 == 0) return false;
		long sqrtP = (long) Math.sqrt(p) + 1;
		for (long i = 6L; i <= sqrtP; i += 6) {
			if (p % (i - 1) == 0 || p % (i + 1) == 0) return false;
		}
		return true;
	};
}
