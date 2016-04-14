package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler005 {

	/**
	 * lcm(a, b) = a*b / gcd(a, b)
	 * 
	 * gcd(a, b) = gcd(b, a mod b)
	 * 
	 * lcm(a, b, c) = lcm(lcm(a, b), c) 
	 * 
	 */
	
	BiFunction<Integer, Integer, Integer> gcd = (a, b) -> {
		if(b == 0) return a;
		return this.gcd.apply(b, a%b);
	};
	
	BiFunction<Integer, Integer, Integer> lcm = (a, b) -> {
		return a*b / gcd.apply(a, b);
	};
	
	@Test
	public void test() {
		int r = Stream.iterate(1, n -> n + 1).limit(19).reduce(1, (a, b) -> lcm.apply(a, b));
		System.out.println(r);
		assertEquals(232792560, r);
	}

}
