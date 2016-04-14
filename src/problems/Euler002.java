package problems;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;

public class Euler002 {

	@Test
	public void test() {
		long r = Stream
				.iterate(new long[] { 1, 1 },
						p -> new long[] { p[1], p[0] + p[1] })
				.limit(33)
				.filter(p -> p[0] % 2l == 0)
				.reduce(new long[] { 0l }, (a, b) -> new long[] { a[0] + b[0] })[0];
		
		System.out.print(r);
		
		assertEquals(4613732, r);
	}

}
