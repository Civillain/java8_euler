package problems;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler009 {

	/**
	 * Pythagoras triple:
	 * a^2 + b^2 = c^2
	 * a < b < c
	 * Find: a + b + c = 1000
	 * Give product: abc
	 * 
	 * Euler's formula:
	 * a = m^2 - n^2, b = 2mn, c = m^2 + n^2
	 * 
	 * m,n , k positive integers, m > n, m - n is odd
	 * 
	 * Observation:
	 * 
	 * m(m + n) = 500
	 * 
	 * m < 500 and n < m
	 * 
	 * The triplet for which a + b + c = 1000 is unique.
	 * 
	 */
	
	@Test
	public void test() {
		long r = LongStream.range(1,500).map(m -> {
			long n = LongStream.range(1,  m)
					.filter(nn -> m * (m + nn) == 500)
					.findFirst()
					.orElse(0l);
			long a = m*m - n*n;
			long b = 2 * m * n;
			long c = m*m + n*n;
			return a*b*c;
		})
		.filter(i -> i > 0)
		.findFirst()
		.getAsLong();
		
		System.out.println(r);
		
		assertEquals(31875000, r);
	}	
}
