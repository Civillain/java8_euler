package problems;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author rko
 *
 */
public class Euler014 {
	final int max = 1000000;
	long next = 0;
	long maxPathLength = 0;
	long startPos = 0;
	
	
	@Test
	public void test() {
		int pathLength = Stream.iterate(2l, n -> n + 1).limit(max)
			.map(n -> {
				// start at 1 because we're not counting the last element in the sequence, namely 1
				int length = 1; 
				// the last element is not counted and is used as stop condition
				// the while-loop is not replaced with a lazy evaluated stream because the stop condition can't be implemented in java 8
				while(n > 1) {  
					if( (n % 2) == 0) {
						n = n / 2;
					} else {
						n = (n * 3) + 1;
					}
					length++;
				}
				return length;
			})
			.reduce(0, (a,b) -> a > b ? a : b);
		
		assertEquals(525, pathLength);
		System.out.println("max path: " + pathLength);
	}
}
