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
	
	private List<Long> primes = new ArrayList<>(1000);
	
	@Test
	public void test() {
		
		primes.add(2l);
		
		long result = Stream.iterate(2l, n -> n + 1).limit(Integer.MAX_VALUE)
			.map(n ->  triangle.apply(n) )
			.filter(n ->  countDivisors.apply(sumDivisors.apply(primeFactors.apply(n))) > 500)
			.findFirst().get();
		
		assertEquals(76576500, result);
		System.out.println(result);
	}
	
	Function<Long, Long> triangle = n -> (n * (n + 1)) / 2;
	
	Function<Map<Long, Long>, Long> countDivisors = m -> m.values().stream().map(d -> d + 1).reduce((a,b) -> a * b).get();
	
	Supplier<Map<Long, Long>> map = () -> new HashMap<Long, Long>();
	
	Function<List<Long>, Map<Long, Long>> sumDivisors = l -> l.stream().collect(Collectors.toMap(i -> i, i -> 1l, Long::sum, map));
	
	
    Function<Long, Boolean> isPrime = n -> {
		if (n < 2) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (long i = 6L; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
		}
		return true;
	};
    
    Consumer<Long> findPrimes = n -> {
    	long lastPrime = primes.get(primes.size()-1); // primes must be initialized with 2
    	for(long i = lastPrime+1; i*i < n; i++ ) {
    		if(isPrime.apply(i)) {
    			primes.add(i);
    		}
    	}
    };
    
    Function<Long, List<Long>> primeFactors = n -> { 

		findPrimes.accept(n); // find the next primes up to sqrt(n)
		
		List<Long> pFactors = new ArrayList<>();
        // for each potential factor i
        for (int i = 0; i < primes.size(); i++) {
        	long prime = primes.get(i);
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
