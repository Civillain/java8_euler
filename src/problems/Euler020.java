package problems;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import utils.EulerUtils;

public class Euler020 {


	@Test
	public void test() {
		BigInteger fact = EulerUtils.factorialTable(100).get(BigInteger.valueOf(100l));
		System.out.println(fact.toString());
		BigInteger sum = EulerUtils.digitSum(fact);
		System.out.println(sum.toString());
		assertEquals(648, sum.intValue());
	}
	
	@Test
	public void example() {
		BigInteger fact = EulerUtils.factorialTable(10).get(BigInteger.valueOf(10l));
		System.out.println(fact.toString());
		BigInteger sum = EulerUtils.digitSum(fact);
		System.out.println(sum.toString());
	}
}
