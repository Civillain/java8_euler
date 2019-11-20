package cryptopals.set1;

import static org.junit.Assert.assertEquals;
import static utils.EulerUtils.memoize;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import utils.CryptoUtils;
import utils.EulerUtils;
import utils.EulerUtils.Tuple;


public class Challenge1 {

	
	@Test
	public void challenge1() {
		String hex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		String expectedBase64 = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
		String actualB64 = CryptoUtils.base16to64(hex);
		assertEquals(expectedBase64, actualB64);
	}
	
	@Test
	public void challenge2() {
		String hex = "1c0111001f010100061a024b53535009181c";
		String hexXorWith = "686974207468652062756c6c277320657965";
		String expected = "746865206b696420646f6e277420706c6179";
		BitSet b1 = BitSet.valueOf(CryptoUtils.base16tobytes(hex));
		BitSet b2 = BitSet.valueOf(CryptoUtils.base16tobytes(hexXorWith));
		b1.xor(b2);
		String actual = CryptoUtils.bytestobase16(b1.toByteArray());
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void challenge3() {
		final String hex = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		
		Optional<Entry<Byte, Integer>> result = Stream.iterate((byte)0, n -> (byte)(n + (byte)1))
				.limit(Byte.MAX_VALUE)
				.parallel()
				.map(pwd -> { // pwd here is the position of the char in the ASCII table
					List<String> msg = CryptoUtils.decrypt.apply(hex, pwd);
					Entry<Byte, List<String>> map = new AbstractMap.SimpleEntry<>(pwd, msg);
					return map;
				})
				.map(s -> {
					Byte pwd = s.getKey();
					List<String> msg = s.getValue();
					String str = msg.stream().collect(Collectors.joining());
					String[] words = str.split("\\h");
					Supplier<Map<String, String>> wc = memoize(CryptoUtils.wordFrequencies);
					Map<String, String> wordCount = wc.get();
					int sum = Arrays.stream(words).map(w -> wordCount.containsKey(w.toLowerCase()) ? 1 : 0).reduce(0, (a, b) -> a + b);
					Entry<Byte, Integer> score = new AbstractMap.SimpleEntry<>(pwd, sum);
					return score;
				})
				.max((a, b) -> a.getValue() > b.getValue() ? 1 : -1);
		
		
		if(result.isPresent()) { 
			System.out.println("password: " + new String(new byte[] {result.get().getKey().byteValue()}));
			String msg = CryptoUtils.decrypt.apply(hex, result.get().getKey()).stream().collect(Collectors.joining());
			System.out.println(msg);
		} else {
			System.out.println("not found");
		}
	}
	
	@Test
	public void challeng4_loopings() throws IOException {
		String txtFileName = "resources/challenge4.txt";
		List<String> content = Files.readAllLines(Paths.get(txtFileName), Charset.forName("UTF-8"));
		
		List<String> alfabet = EulerUtils.alphabet.apply(' ', '~');
		
		class Score {
			final String cipher;
			final String deciphered;
			final String pwd;
			final Integer score;
			public Score(String cipher, String deciphered, String pwd, Integer score) {
				this.cipher = cipher;
				this.deciphered = deciphered;
				this.pwd = pwd;
				this.score = score;
			}
			@Override
			public String toString() {
				return "[" + this.deciphered + ", " + this.pwd + ", " + this.score + ", " + this.cipher + "]";
			}
		}
		
		Function<List<String>, Boolean> isPrintableASCII = msg -> {
			return !msg.stream().filter(str -> {
				return str.length() == 1 &&
						!(31 < (int)str.toCharArray()[0] && (int)str.toCharArray()[0] < 127);
			}).findFirst().isPresent();
		};
		
		Function<List<String>, List<String>> asWords = msg -> {
			String str = msg.stream().collect(Collectors.joining());
			String[] words = str.split("\\W");
			return Arrays.asList(words);
		};
		
		Function<List<String>, String> joinString = chars -> chars.stream().collect(Collectors.joining());
		
		Function<List<String>, Integer> wordCount = words -> {
			Supplier<Map<String, String>> wc = memoize(CryptoUtils.wordFrequencies);
			Map<String, String> wordFreqs = wc.get();
			int sum = words.stream().map(w -> wordFreqs.containsKey(w.toLowerCase()) ? 1 : 0).reduce(0, (a, b) -> a + b);
			return sum;
		};
		
		Function<String, Integer> letterCount = msg -> {
			int sum = msg.chars().filter(s -> alfabet.contains(String.valueOf((char)s))).sum();
			return sum;
		};
		
		PrintWriter log = new PrintWriter("log.txt");
		
		Map<Integer, Score> results = new TreeMap<>();
		for(String hex : content) {
			for(String letter : alfabet) {
				byte pwd = letter.getBytes()[0];
				List<String> msg = CryptoUtils.decrypt.apply(hex.trim(), pwd);
				if(!isPrintableASCII.apply(msg)) {
					continue;
				}
				String str = msg.stream().collect(Collectors.joining());
				List<String> words = asWords.apply(msg);
				int sum = wordCount.apply(words);
				Score score = new Score(hex, str, letter, sum);
				log.println(score);
				results.put(sum, score);
			}
		}
		log.close();
		
		Optional<Integer> found = results.keySet().stream().max(Integer::compareTo);
		if(found.isPresent()) {
			Score score = results.get(found.get());
			System.out.println(score);
		} else {
			System.out.println("not found");
		}
	}
	
	@Test
	public void challenge4_1() {
		Supplier<Stream<String>> ciphers = () -> {
			String txtFileName = "resources/challenge4.txt";
			List<String> content = null;
			try {
				content = Files.readAllLines(Paths.get(txtFileName), Charset.forName("UTF-8"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return content.stream().map(l -> l.trim());
		};
		
		Function<String, Optional<Tuple<Byte, String>>> decode = hex -> {
			Optional<Entry<Byte, Integer>> result = Stream.iterate((byte)0, n -> (byte)(n + (byte)1))
					.limit(Byte.MAX_VALUE)
					.parallel()
					.map(pwd -> { // pwd here is the position of the char in the ASCII table
						List<String> msg = CryptoUtils.decrypt.apply(hex, pwd);
						Entry<Byte, List<String>> map = new AbstractMap.SimpleEntry<>(pwd, msg);
						return map;
					})
					.map(s -> {
						Byte pwd = s.getKey();
						List<String> msg = s.getValue();
						String str = msg.stream().collect(Collectors.joining());
						String[] words = str.split("\\h");
						Supplier<Map<String, String>> wc = memoize(CryptoUtils.wordFrequencies);
						Map<String, String> wordCount = wc.get();
						int sum = Arrays.stream(words).map(w -> wordCount.containsKey(w.toLowerCase()) ? 1 : 0).reduce(0, (a, b) -> a + b);
						Entry<Byte, Integer> score = new AbstractMap.SimpleEntry<>(pwd, sum);
						return score;
					})
					.max((a, b) -> a.getValue() > b.getValue() ? 1 : -1);
			
				if(result.isPresent()) { 
					System.out.println("password: " + new String(new byte[] {result.get().getKey().byteValue()}));
					String msg = CryptoUtils.decrypt.apply(hex, result.get().getKey()).stream().collect(Collectors.joining());
					System.out.println(msg);
					Tuple<Byte, String> t = new Tuple<>(result.get().getKey(), msg);
					return Optional.of(t);
				}
				return Optional.<Tuple<Byte, String>>empty();
		};
		
		
		Stream<Optional<Tuple<Byte, String>>> result = ciphers.get().map(hex -> decode.apply(hex));
		
		Stream<Tuple<Byte, String>> results = result.filter(opt -> opt.isPresent()).map(opt -> opt.get());
		
		results.forEach(r -> {
			System.out.println(r.first + " -> " + r.second);
		});
	}
	
	@Test
	public void challenge4() {
		
		/*
		 * One cipher of 60 chars, 30 hex  numbers, takes 20 seconds to decode and find a single char password.
		 * 
		 * There are 327 ciphers, each with length 60, and 127 possible passwords.
		 * 
		 * This will take ~2 hours to decode. Parallelised it will take at best 8 times less, ~30 minutes.
		 * 
		 * Per character it takes ~20secs/ 30chars = 0.7secs/char. If the first three chars are decoded,
		 * and any contains a non-printable char, then the cipher is rejected. This will take ~2secs per cipher.
		 * 
		 * Any cipher with printable chars in the first three positions is moved forward to complete deciphering.
		 * 
		 * This will take 327*2 ~ 10minutes. If parallelised over 8 cores, 10/8 ~2minutes.
		 * 
		 */
		
		long start = System.currentTimeMillis();
		Supplier<Stream<String>> ciphers = () -> {
			String txtFileName = "resources/challenge4.txt";
			List<String> content = null;
			try {
				content = Files.readAllLines(Paths.get(txtFileName), Charset.forName("UTF-8"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return content.stream().map(l -> l.trim());
		};
		
		Supplier<Stream<Byte>> passwords = () -> {
			return Stream.iterate((byte)0, n -> (byte)(n + (byte)1)).limit(256); // printable utf-8 chars
		};
		
		Function<List<String>, Boolean> isPrintableUTF8 = msg -> {
			return !msg.stream().filter(str -> {
				return str.length() == 1 &&
						!(31 < (int)str.toCharArray()[0] && (int)str.toCharArray()[0] < 256);
			}).findFirst().isPresent();
		};
		
		Function<List<String>, Boolean> isPrintableASCII = msg -> {
			return !msg.stream().filter(str -> {
				return str.length() == 1 &&
						!(31 < (int)str.toCharArray()[0] && (int)str.toCharArray()[0] < 127);
			}).findFirst().isPresent();
		};
		
		Function<List<String>, List<String>> asWords = msg -> {
			String str = msg.stream().collect(Collectors.joining());
			String[] words = str.split("\\W");
			return Arrays.asList(words);
		};
		
		Function<List<String>, String> joinString = chars -> chars.stream().collect(Collectors.joining());
		
		Function<List<String>, Integer> wordCount = words -> {
			Supplier<Map<String, String>> wc = memoize(CryptoUtils.wordFrequencies);
			Map<String, String> wordFreqs = wc.get();
			int sum = words.stream().map(w -> wordFreqs.containsKey(w.toLowerCase()) ? 1 : 0).reduce(0, (a, b) -> a + b);
			return sum;
		};
		
		BiFunction<String, Byte, List<String>> decrypt = (cipher, pwd) -> {
			 List<String> decrypted = CryptoUtils.decrypt.apply(cipher, pwd);
			 return decrypted;
		};
		
		BiFunction<String, Integer, String> head = (msg, count) -> msg.substring(0, count);
		
		BiFunction<List<String>, Integer, Boolean> minWords = (words, count) -> words.size() >= count;  
		
		Optional<Tuple<Integer, Tuple<List<String>, Byte>>> result = passwords.get()
			//.peek(b -> System.out.println((char)b.byteValue()))
			.parallel()
			.flatMap(pwd -> { 
				return ciphers.get()
					//.peek(cipher -> System.out.println(cipher))
					.map(cipher -> new Tuple<String, String>(head.apply(cipher, 6), cipher))
					.map(t -> new Tuple<List<String>, String>(decrypt.apply(t.first, pwd), t.second))
					.filter(t -> isPrintableASCII.apply(t.first))
					//.peek(a -> System.out.println(a.first))
					.map(f -> new Tuple<List<String>, Byte>(decrypt.apply(f.second, pwd), pwd))
					//.map(f -> new Tuple<List<String>, Byte>(decrypt.apply(f, pwd), pwd))
					//.peek(a -> System.out.println(joinString.apply(a.first)))
					.filter(p -> isPrintableASCII.apply(p.first));
					//.peek(a -> System.out.println(a.first));
			})
			.map(r -> new Tuple<List<String>, Byte>(asWords.apply(r.first), r.second))
			//.peek(a -> System.out.println(a.first))
			.filter(m -> minWords.apply(m.first, 2))
			//.peek(a -> System.out.println(a.first))
			.map(u -> new Tuple<Integer, Tuple<List<String>, Byte>>(wordCount.apply(u.first), u))
			//.peek(a -> System.out.println(a.first + " -> " + a.second.first))
			.max((a, b) -> a.first > b.first ? 1 : -1);
	
		if(result.isPresent()) { 
			Tuple<List<String>, Byte> m = result.get().second;
			System.out.println("password: " + new String(new byte[] {m.second.byteValue()}));
			System.out.println(joinString.apply(m.first) + " score: " + result.get().first);
		} else {
			System.out.println("not found");
		}
		long end = System.currentTimeMillis() - start;
		System.out.println("found in: " + end/1000 + "s.");
	}
}
