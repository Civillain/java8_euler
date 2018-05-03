package problems;

import static org.junit.Assert.assertEquals;
import static utils.EulerUtils.digitSum;
import static utils.EulerUtils.expBySquaring;

import java.math.BigInteger;

import org.junit.Test;

public class Euler016 {

	@Test
	public void test() {
		// exponentiation by squaring
		BigInteger result = expBySquaring(2, 1000);
		
		// digit sum
		BigInteger sum = digitSum(result);
		System.out.println(sum.toString());
		assertEquals(1366, sum.intValue());
		
	}

}
