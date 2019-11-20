package problems.security;

import static org.junit.Assert.assertEquals;
import static utils.EulerUtils.alphabet;
import static utils.EulerUtils.cartesian;
import static utils.EulerUtils.memoize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

/*
 * Each character on a computer is assigned a unique code and the preferred standard is ASCII (American Standard Code for Information Interchange). For example, uppercase A = 65, asterisk (*) = 42, and lowercase k = 107.
A modern encryption method is to take a text file, convert the bytes to ASCII, then XOR each byte with a given value, taken from a secret key. The advantage with the XOR function is that using the same encryption key on the cipher text, restores the plain text; for example, 65 XOR 42 = 107, then 107 XOR 42 = 65.
For unbreakable encryption, the key is the same length as the plain text message, and the key is made up of random bytes. The user would keep the encrypted message and the encryption key in different locations, and without both "halves", it is impossible to decrypt the message.
Unfortunately, this method is impractical for most users, so the modified method is to use a password as a key. If the password is shorter than the message, which is likely, the key is repeated cyclically throughout the message. The balance for this method is using a sufficiently long password key for security, but short enough to be memorable.
Your task has been made easy, as the encryption key consists of three lower case characters. Using cipher.txt (right click and 'Save Link/Target As...'), a file containing the encrypted ASCII codes, and the knowledge that the plain text must contain common English words, decrypt the message and find the sum of the ASCII values in the original text.
 */
public class Euler059 {


	@Test
	public void test() throws Exception {
		List<String> lowercaseAlphabet = alphabet.apply(new Character('a'), new Character('z'));
		List<String> pwds = cartesian(3, lowercaseAlphabet);
		
		Stream<List<Character>> printable = pwds.stream().map(pwd -> decrypt.apply(pwd)).filter(decrypted -> {
				Optional<Character> notPrintableFound = decrypted.stream().filter(c -> {
					return c.charValue() < 32 || c.charValue() > 126;
				}).findFirst();
				return !notPrintableFound.isPresent();
			});
		
		Map<String, String> freq = wordFrequencies.get();
		
		Optional<SimpleEntry<List<Character>, Integer>> cipher = printable.map(chars -> {
			List<String> words = asWords.apply(chars);
			int sum = words.stream().mapToInt(w -> freq.containsKey(w) ?  1 :  0).sum();
			return new AbstractMap.SimpleEntry<List<Character>, Integer>(chars, sum);
		}).reduce((a, b) -> a.getValue() > b.getValue() ? a : b);
		
		if(cipher.isPresent()) {
			cipher.get().getKey().stream().forEach(System.out::print);
			System.out.println(Euler059.sumOfAsciiValues.apply(cipher.get().getKey()));
		} else {
			throw new Exception("Not found!");
		}
	}
	
	static Function<List<Character>, Integer> sumOfAsciiValues = chars -> chars.stream().mapToInt(c -> (int)c.charValue()).sum();
			
	
	static Function<List<Character>, List<String>> asWords = chars -> {
		
		// any non-alphanumeric character is a word boundary diff((32-126) , union( (65-90), (97-122)))
		// real boundaries: space = 32, ',' = 44, '.' =  46, ( = 40, ) = 41, ! = 33, ? = 63
		
		// split on the space character, filter on [a-zA-Z], match with frequency table to find real word counts
		// the text with the most real words is the cipher, decrypted
		
		String asString = String.valueOf(chars);
		List<String> words = Stream.of(asString)
									.map(str -> str.split("\\s+"))
									.flatMap(Arrays::stream)
									.map(w -> w.replaceAll("[^a-zA-Z]", ""))
									.collect(Collectors.toList());
		
		
		
		return words;
	};
	
	static Function<String, List<Character>> decrypt = pwd -> {
		
		char[] pwdAsChars = pwd.toCharArray();
		String[] contents = memoize(Euler059.parseCipher).get();
		IntStream decryptedCharInts = IntStream.range(0, contents.length).map(idx -> {
			int pwdIdx = idx % pwdAsChars.length;
			char pwdChar = pwdAsChars[pwdIdx];
			char contentChar = (char)Integer.valueOf(contents[idx]).intValue();
			int xorred = contentChar ^ pwdChar;
			return xorred;
		});
		
		List<Character> decryptedChars = decryptedCharInts.boxed().map(i -> (char)i.intValue()).collect(Collectors.toList());
		
		return decryptedChars;
	};
	
	static Supplier<String[]> parseCipher = () -> {
		String cipherTxtFileName = "resources/cipher.txt";
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(cipherTxtFileName)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String[] splitted = content.split(",");
		return splitted;
	};
	
	static Supplier<Map<String, String>> wordFrequencies = () -> {
		String txtFileName = "resources/wordcount_en.txt";
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(txtFileName)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String[] lines = content.split("\n");
		Map<String, String> wordCount = Arrays.stream(lines)
												.map(line -> line.split("\\s+"))
												.collect(Collectors.toMap(line -> line[0], line -> line[1]));
		return wordCount;
	};
	
	@Test
	public void testXOR() {
		int x1 = 100 ^ 'a';
		System.out.println(x1);
		assertEquals('a', x1 ^100);
		assertEquals(100, x1 ^ 'a');
		
	}
		
	@Test
	public void testCartesian() {
		List<String> lowercaseAlphabet = alphabet.apply(new Character('a'), new Character('z'));
		List<String> pwds = cartesian(3, lowercaseAlphabet);
		pwds.forEach(System.out::println);
	}
}
