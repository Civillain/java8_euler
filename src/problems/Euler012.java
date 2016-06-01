package problems;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler012 {
	
	private List<Integer> primes = new ArrayList<>(1100);
	
	@Test
	public void test() {
		
		primes.add(2);
		
		int result = Stream.iterate(2, n -> n + 1).limit(Integer.MAX_VALUE)
			.map(n ->  triangle.apply(n) )
			.filter(n ->  countDivisors.apply(sumDivisors.apply(primeFactors.apply(n))) > 500)
			.findFirst().get();
		
		assertEquals(76576500, result);
		System.out.println(result);
	}
	
	Function<Integer, Integer> triangle = n -> (n * (n + 1)) / 2;
	
	Function<Map<Integer, Integer>, Integer> countDivisors = m -> m.values().stream().map(d -> d + 1).reduce((a,b) -> a * b).get();
	
	Supplier<Map<Integer, Integer>> map = () -> new HashMap<Integer, Integer>();
	
	Function<List<Integer>, Map<Integer, Integer>> sumDivisors = l -> l.stream().collect(Collectors.toMap(i -> i, i -> 1, Integer::sum, map));
	
	
    Function<Integer, Boolean> isPrime = n -> {
		if (n < 2) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		int sqrtN = (int) Math.sqrt(n) + 1;
		for (int i = 6; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
		}
		return true;
	};
    
    Consumer<Integer> findPrimes = n -> {
    	int lastPrime = primes.get(primes.size()-1); // primes must be initialized with 2
    	for(int i = lastPrime+1; i*i < n; i++ ) {
    		if(isPrime.apply(i)) {
    			primes.add(i);
    		}
    	}
    };
    
    Function<Integer, List<Integer>> primeFactors = n -> { 

		findPrimes.accept(n); // find the next primes up to sqrt(n)
		
		List<Integer> pFactors = new ArrayList<>();
        // for each potential factor i
        for (int i = 0; i < primes.size(); i++) {
        	int prime = primes.get(i);
            // if i is a factor of N, repeatedly divide it out
            while (n % prime == 0) {
            	pFactors.add(prime);
                n = n / prime;
            }
        }

        // if biggest factor occurs only once, n > 1
        if (n > 1) pFactors.add(n);
        
        return pFactors;
    };
}
