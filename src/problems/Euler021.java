package problems;

import static utils.EulerUtils.divisors;
import static utils.EulerUtils.throwingMerger;
import static utils.EulerUtils.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import utils.EulerUtils.Pair;

public class Euler021 {

	Function<Integer, Map<Integer, Integer>> findDivisorSums = l -> {
		Map<Integer, Integer> results = Stream.iterate(1, n -> n + 1).limit(l).map(number -> {
			List<Integer> d = divisors(number);
			Integer divisorsSum = d.stream().reduce((a, b) -> a + b).get();
			return new Pair<>(number, divisorsSum);
		}).filter(p -> p.first != p.second).collect(Collectors.toMap(p -> p.first, p -> p.second, throwingMerger(), TreeMap::new));
		return results;
	};
	
	Function<Map<Integer, Integer>, Map<Integer, Integer>> amicablePairs = results -> {
		Map<Integer, Integer> amicablePairs = results.entrySet().stream()
				.filter(e -> results.containsKey(e.getValue()) && results.get(e.getValue()).equals(e.getKey()) )
				.filter(e -> !e.getKey().equals(e.getValue()) )
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
		return amicablePairs;
	};

	@Test
	public void test() {
		Map<Integer, Integer> results = findDivisorSums.apply(10_000);
		Map<Integer, Integer> found = amicablePairs.apply(results);
		found.forEach((k, v) -> print(k + " -> " + v));
		
		Integer sum = found.keySet().stream().reduce((a, b) -> a + b).get();
		print(sum.toString());
	}
	
	@Test
	public void example() {
		Map<Integer, Integer> results = findDivisorSums.apply(300);
		Map<Integer, Integer> found = amicablePairs.apply(results);
		found.forEach((k, v) -> print(k + " -> " + v));
		
		Integer sum = found.keySet().stream().reduce((a, b) -> a + b).get();
		print(sum.toString());
	}
}
