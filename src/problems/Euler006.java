package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

public class Euler006 {

	@Test
	public void test() {
		// sum of squares: S(n) = n(n+1)(2n+1)/6
		// square of sum: S(i) = (n(n+1)/2)^2

		Function<Integer, Integer> sum_squares = n -> (n * ((n + 1) * (2 * n + 1))) / 6;
		Function<Integer, Integer> square_sum = n -> Double.valueOf(Math.pow((n * (n + 1)) / 2, 2d)).intValue();

		int a = sum_squares.apply(100);
		int b = square_sum.apply(100);
		int d = b - a;
		System.out.println(d);
		assertEquals(25164150, d);
	}

}
