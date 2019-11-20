package problems;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Map;

import org.junit.Test;

import utils.EulerUtils;

public class Euler015 {

	private final int length = 20;
	private final int breadth = 20;
	
	@Test
	public void test() {
		// combinatorial problem knows as 'trajectories'
		// calculate n+m over n -> n!/k!*(n-k)!
		int n = length + breadth;
		int k = length;
		int n_k = n - k;
		Map<BigInteger, BigInteger> factorialTable = EulerUtils.factorialTable(n);
		BigInteger nFact = factorialTable.get(BigInteger.valueOf(n));
		BigInteger kFact = factorialTable.get(BigInteger.valueOf(k));
		BigInteger n_kFact = factorialTable.get(BigInteger.valueOf(n_k));
		BigInteger result = nFact.divide(  kFact.multiply(n_kFact)  );
		System.out.println("result: " + result.toString());
		assertEquals("137846528820", result.toString());
	}

}
