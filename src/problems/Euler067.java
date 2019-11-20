package problems;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class Euler067 {

	private List<List<Integer>> buildPyramid(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(new File(fileName).toPath());
		List<List<Integer>> pyramidList = lines.stream().map(line -> {
			String [] splitted = line.split(" ");
			List<Integer> cols = Arrays.stream(splitted).map(s -> Integer.valueOf(s)).collect(Collectors.toList());
			return cols;
		}).collect(Collectors.toList());
		return pyramidList;
	}
	
	private Integer traverse(List<List<Integer>> pyramid) {
		Integer result = 0;
		
		// start at the last - 1 th row
		for(int row = pyramid.size() - 2 ; row >= 0; row--) {
			for(int col = 0; col <= row; col++) {
				Integer i = pyramid.get(row).get(col);
				Integer left = pyramid.get(row+1).get(col);
				Integer right = pyramid.get(row+1).get(col+1);
				Integer leftSum = i + left;
				Integer rightSum = i + right;
				Integer maxSum = leftSum > rightSum ? leftSum : rightSum;
				pyramid.get(row).set(col, maxSum);
			}
		}
		
		return pyramid.get(0).get(0);
	}
	
	@Test
	public void test() throws IOException {
		List<List<Integer>> pyramid = buildPyramid("resources/p067_triangle.txt");
		long start = System.currentTimeMillis();
		Integer result = traverse(pyramid);
		long end = System.currentTimeMillis() - start;
		System.out.println(result + " in " + end + "ms.");
		assertEquals(7273, result.intValue());
	}
	
	public static void main(String [] args) throws IOException {
		Euler067 euler067 = new Euler067();
		euler067.test();
	}
	
	@Test
	public void example() throws IOException {
		List<List<Integer>> pyramid = buildPyramid("resources/euler018_2.txt");
		long start = System.currentTimeMillis();
		Integer result = traverse(pyramid);
		long end = System.currentTimeMillis() - start;
		System.out.println(result + " in " + end + "ms.");
		assertEquals(1074, result.intValue());
	}

}
