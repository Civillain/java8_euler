package problems;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

import utils.EulerUtils;

public class Euler025 {

	Function<BigInteger, Boolean> thousandDigits = b -> EulerUtils.countDigits.apply(b).doubleValue()  == 1000l;
	Function<BigInteger, Boolean> threeDigits = b -> EulerUtils.countDigits.apply(b).doubleValue() == 3l;
	
	@Test
	public void test() {
		Stream<BigInteger> fibs = EulerUtils.fibonacci.get();
		Stream<Entry<Integer, BigInteger>> zipped = EulerUtils.zipWithIndex(fibs);
		Integer result = EulerUtils.fibonacciWithStopIdx.apply(zipped).apply(thousandDigits);
		System.out.println(result+2); // +2 while 0-based and double keys are discarded in the map, so the first two 1s of the fibonacci stream disappear!
		assertEquals(4782, result.intValue()+2);
	}
	
	@Test
	public void example() {
		Stream<BigInteger> fibs = EulerUtils.fibonacci.get();
		Stream<Entry<Integer, BigInteger>> zipped = EulerUtils.zipWithIndex(fibs);
		Integer result = EulerUtils.fibonacciWithStopIdx.apply(zipped).apply(threeDigits);
		System.out.println(result+2); // +2 while 0-based and double keys are discarded in the map, so the first two 1s of the fibonacci stream disappear!
		assertEquals(12, result.intValue()+2);
	}
}
