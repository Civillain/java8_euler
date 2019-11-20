package problems.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;

import utils.Digraph;
import utils.DirectedCycle;
import utils.Topological;

/*
 * A common security method used for online banking is to ask the user for three random characters from a passcode. For example, if the passcode was 531278, they may ask for the 2nd, 3rd, and 5th characters; the expected reply would be: 317.
The text file, keylog.txt, contains fifty successful login attempts.
Given that the three characters are always asked for in order, analyse the file so as to determine the shortest possible secret passcode of unknown length.
 */

public class Euler079 {

	@Test
	public void test() {
		
		String [] keylog = loadFile.get();
		Set<Character> uniq = Arrays.stream(keylog)
				.map(str -> str.chars()  )
				.flatMap(s -> s.boxed())
				.map(intValue -> (char)intValue.intValue())
				.collect(Collectors.toSet());
		
		//uniq.forEach(System.out::println);
		
		int minSize = uniq.size();
		
		List<Character> sortedLabels = uniq.stream().sorted().collect(Collectors.toList());
		
		Digraph digraph = new Digraph(minSize); 
		
		// if the digraph contains cycles, then there must be more then minSize numbers
		// add the number of cycles to the minSize
		
		Arrays.stream(keylog)
		.forEach(str -> {
			for(int i = 0; i < str.length()-1;i++) {
				char from = str.charAt(i);
				char to = str.charAt(i+1);
				int vertexIdx_v = sortedLabels.indexOf(from);
				int vertexIdx_w = sortedLabels.indexOf(to);
				digraph.addEdge(vertexIdx_v, vertexIdx_w);
			}
		});
		Topological sorted = new Topological(digraph);
		if(sorted.hasOrder()) {
			sorted.order().forEach(idx -> System.out.print(sortedLabels.get(idx)));
		}
	}
	
	
	static Supplier<String[]> loadFile = () -> {
		String fileName = "resources/keylog.txt";
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String[] splitted = content.split("\\v+");
		return splitted;
	};
	
	@Test
	public void example() {
	}
}
