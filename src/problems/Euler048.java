package problems;

import static org.junit.Assert.assertEquals;
import static utils.EulerUtils.print;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

public class Euler048 {

	Function<BigInteger, BigInteger> selfPower = number -> {
		BigInteger result = number.pow(number.intValue());
		return result;
	};
	
	BiFunction<Function<BigInteger, BigInteger>, Integer, BigInteger> sum = (f, limit) -> {
		BigInteger sum = Stream.iterate(1, n -> n + 1).limit(limit)
			.map(n -> {
				BigInteger bigN = BigInteger.valueOf((long)n);
				BigInteger result = f.apply(bigN);
				return result;
			}).reduce((a, b) -> a.add(b))
			.get();
		return sum;
	};
	
	@Test
	public void test() {
		BigInteger summed = sum.apply(selfPower, 1000);
		String result = summed.toString();
		print(result);
		String lastTenDigits = result.substring(result.length()-10, result.length());
		print(lastTenDigits);
		assertEquals("9110846700", lastTenDigits);
	}
	
	@Test
	public void example() {
		BigInteger summed = sum.apply(selfPower, 10);
		String result = summed.toString();
		print(result);
		String lastTenDigits = result.substring(result.length()-10, result.length());
		print(lastTenDigits);
	}
}
