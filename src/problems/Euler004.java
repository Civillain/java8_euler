package problems;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler004 {

	@Test
	public void test() {
		
		Function<Integer, Boolean> hasProduct = palindrome -> {
			for(int a = 999; a>99;a--) {
					int p = palindrome.intValue();
					if(p%a==0) {
						int b = p/a; 
						if( b > 99 && b < 1000 ) {
							System.out.println(a + " * " + b + " = " + p);
							return true;
						}
					}
				}
			return false;
		};
		
		Integer max_p  = Stream.iterate(999, n -> n - 1).limit(900)
			.map(n -> {
				return Integer.valueOf(new StringBuilder(String.valueOf(n)).append(new StringBuilder(String.valueOf(n)).reverse()).toString());
			})
			.filter(p -> hasProduct.apply(p))
			.findFirst().get();

		System.out.println(max_p);
		
		assertEquals(906609, max_p.intValue());
		
	}

}
