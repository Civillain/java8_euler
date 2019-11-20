package problems;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import utils.EulerUtils.Memoizer;
import utils.EulerUtils.Node;

public class Euler018 {

	final Function<Node<Integer>, Integer> traverse = n -> {
		if(n.hasChildren()) {
			List<Node<Integer>> children = n.getChildren();
			Integer max = children.stream().map(c -> {
				Function<Node<Integer>, Integer> m = Memoizer.memoize(this.traverse);
				Integer value = m.apply(c);
				Integer sum = n.getValue() + value;
				return sum;
			}).max(Integer::compare).get();
			return max;
		}
		return n.getValue();
	};
	
	private Node<Integer> buildPyramid(String fileName) {
		try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
			
			// build the pyramid, without links between the nodes
			List<List<Node<Integer>>> pyramid = stream.map(s -> s.split(" ")).map(a -> {
					List<Node<Integer>> row = Arrays.stream(a).map(Integer::valueOf)
						.map(i -> new Node<Integer>(i))
						.collect(Collectors.toList());
					return row;
			}).collect(Collectors.toList());
					
			// reduce the pyramid bottom up to a single node, the root
			for(int i = 0; i < pyramid.size()-1; i++) {
				List<Node<Integer>> row = pyramid.get(i);
				for(int j = 0; j < row.size(); j++) {
					Node<Integer> parent = row.get(j);
					Node<Integer> left = pyramid.get(i+1).get(j);
					Node<Integer> right = pyramid.get(i+1).get(j+1);
					parent.addChildren(left, right);
				}
			}
			return pyramid.get(0).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void test() {
		Node<Integer> root = buildPyramid("resources/euler018_2.txt");
		long start = System.currentTimeMillis();
		Integer result = traverse.apply(root);
		long end = System.currentTimeMillis() - start;
		System.out.println(result + " in " + end + "ms.");
		assertEquals(1074, result.intValue());
	}
	
	@Test
	public void example() {
		// Maximum path sum I
		// bottom-up dynamic programming using optimal substructure with 2-dimensional array or
		// top-down using memoization and pruning with a pyramid structure

		// test pyramid
		Node<Integer> root = new Node<>(3);
		Node<Integer> l1_1 = new Node<>(7);
		Node<Integer> l1_2 = new Node<>(4);
		Node<Integer> l2_1 = new Node<>(2);
		Node<Integer> l2_2 = new Node<>(4);
		Node<Integer> l2_3 = new Node<>(6);
		Node<Integer> l3_1 = new Node<>(8);
		Node<Integer> l3_2 = new Node<>(5);
		Node<Integer> l3_3 = new Node<>(9);
		Node<Integer> l3_4 = new Node<>(3);
		root.addChildren(l1_1, l1_2);
		l1_1.addChildren(l2_1, l2_2);
		l1_2.addChildren(l2_2, l2_3);
		l2_1.addChildren(l3_1, l3_2);
		l2_2.addChildren(l3_2, l3_3);
		l2_3.addChildren(l3_3, l3_4);
		
		Integer result = traverse.apply(root);
		System.out.println(result);
		assertEquals(23, result.intValue());
	}
}
