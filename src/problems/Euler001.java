package problems;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;

public class Euler001 {

	@Test
	public void test() {
		int r = Stream.iterate(1, n -> n + 1).limit(999)
				.filter(n -> n % 3 == 0 || n % 5 == 0)
				.reduce(0, (a, b) -> a + b);
		
		System.out.print(r);
		
		assertEquals(233168, r);
	}

}
