package problems;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

import utils.EulerUtils.Tuple;

/**
 * @author rko
 *
 */
public class Euler014 {
	final int max = 1000000;
	long next = 0;
	long maxPathLength = 0;
	long startPos = 0l;
	
	@Test
	public void test() {
		Optional<Tuple<Long, Integer>> result = Stream.iterate(startPos, n -> n + 1).limit(max)
			.map(n -> {
				long startPos = n;
//				System.out.print("startPos: " + n + ", ");
				int length = 1; // start at 1 because we're not counting the last element in the sequence, namely 1
				while(n > 1) {  // the last element is not counted and is used as stop condition
					if( (n % 2) == 0) {
						n = n / 2;
					} else {
						n = (n * 3) + 1;
					}
					length++;
				}
//				System.out.println("length: " + length);
				Tuple<Long, Integer> t = new Tuple<Long, Integer>(startPos, length);
				return t;
			})
			.reduce((a,b) -> a.second > b.second ? a : b);
		
		assertEquals(525, result.get().second.intValue());
		System.out.println("max path: " + result.get().second + " at startPos: " + result.get().first);
	}
}
