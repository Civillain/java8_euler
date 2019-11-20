package utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.bind.DatatypeConverter;

public class CryptoUtils {
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static final String LETTER_FREQUENCY = "EARIOTNSLCUDPMHGBFYWKVXZJQ";
	public static char[] alfabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static char[] alfabetUpperAndLower = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdfghijklmnopqrstuvwxyz".toCharArray(); 
	
	public static boolean containsOnlyASCII(byte[] bytes) {
		for(int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			if(32 <= b && b <= 127) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static byte[][] split(byte[] bytes, final int size) {
		final int rowLength = (int) Math.ceil(bytes.length/size) + 1;
		byte [][] cols = new byte[size][rowLength];
		int rowIdx = 0;
		for(int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			int colIdx = i % size;
			cols[colIdx][rowIdx] = b;
			if(colIdx == (size - 1) ) {
				rowIdx++;
			}
		}
		return cols;
	}
	
	public static Map<String, Float> words() {
		Map<String, Float> words = new HashMap<>();
		words.put("THE", 6.42f);
		words.put("OF", 2.76f);
		words.put("AND", 2.75f);
		words.put("TO", 2.67f);
		words.put("A", 2.43f);
		words.put("IN", 2.31f);
		words.put("IS", 1.12f);
		words.put("FOR", 1.01f);
		words.put("THAT", 0.92f);
		words.put("WAS", 0.88f);
		words.put("ON", 0.78f);
		words.put("WITH", 0.75f);
		words.put("HE", 0.75f);
		words.put("IT", 0.74f);
		words.put("AS", 0.71f);
		words.put("AT", 0.58f);
		words.put("HIS", 0.55f);
		words.put("BY", 0.51f);
		words.put("BE", 0.48f);
		words.put("FROM", 0.47f);
		words.put("ARE", 0.47f);
		words.put("THIS", 0.42f);
		words.put("I", 0.41f);
		words.put("BUT", 0.40f);
		words.put("HAVE", 0.39f);
		words.put("AN", 0.37f);
		words.put("HAS", 0.35f);
		words.put("NOT", 0.34f);
		words.put("THEY", 0.33f);
		words.put("OR", 0.30f);
		return words;
	}
	
	public static Map<String, Float> digrams() {
		Map<String, Float> digrams = new HashMap<>();
		digrams.put("th", 1.52f); 
		digrams.put("he", 1.28f); 
		digrams.put("in", 0.94f); 
		digrams.put("er", 0.94f); 
		digrams.put("an", 0.82f); 
		digrams.put("re", 0.68f); 
		digrams.put("nd", 0.63f); 
		digrams.put("at", 0.59f); 
		digrams.put("on", 0.57f); 
		digrams.put("nt", 0.56f); 
		digrams.put("ha", 0.56f); 
		digrams.put("es", 0.56f); 
		digrams.put("st", 0.55f); 
		digrams.put("en", 0.55f);
		digrams.put("ed", 0.53f);
		digrams.put("to", 0.52f);
		digrams.put("it", 0.50f);
		digrams.put("ou", 0.50f);
		digrams.put("ea", 0.47f);
		digrams.put("hi", 0.46f);
		digrams.put("is", 0.46f);
		digrams.put("or", 0.43f);
		digrams.put("ti", 0.34f);
		digrams.put("as", 0.33f);
		digrams.put("te", 0.27f);
		digrams.put("et", 0.19f);
		digrams.put("ng", 0.18f);
		digrams.put("of", 0.16f);
		digrams.put("al", 0.09f);
		digrams.put("de", 0.09f);
		digrams.put("se", 0.08f);
		digrams.put("le", 0.08f);
		digrams.put("sa", 0.06f);
		digrams.put("si", 0.05f);
		digrams.put("ar", 0.04f);
		digrams.put("ve", 0.04f);
		digrams.put("ra", 0.04f);
		digrams.put("ld", 0.02f);
		digrams.put("ur", 0.02f);
		return digrams;
	}
	
	public static Map<String, Integer> observedDigrams(String str) {
		String alfabetic = onlyAlfabetic(str).toLowerCase();
		Map<String, Long> digramFrequencies = Arrays
			    .stream(alfabetic
			        .replaceAll("(?<!^| ).(?! |$)", "$0$0") // double letters
			        .split(" |(?<=\\G..)")) // split into digrams 
			    .filter(s -> s.length() > 1) // discard short terms
			    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
		
		Map<String, Integer> digramFrequenciesInts = digramFrequencies.entrySet()
														.stream()
														.map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().intValue()))
														.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
											
		return digramFrequenciesInts;
	}
	
	public static Map<String, Integer> expectedDigrams(String str) {
		Map<String, Float> digrams = digrams();
		Map<String, Integer> expectedDigrams = digrams.entrySet().stream()
												.map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Math.round((e.getValue() / 100) * str.length()) ))
												.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
		return expectedDigrams;
	}
	
	public static Map<Character, Float> letterFrequencies() {
		Map<Character, Float> letterFreqs = new HashMap<>();
		letterFreqs.put('A', 8.55f);
		letterFreqs.put('B', 1.60f);
		letterFreqs.put('C', 3.16f);
		letterFreqs.put('D', 3.87f);
		letterFreqs.put('E',12.10f);
		letterFreqs.put('F', 2.18f);
		letterFreqs.put('G', 2.09f);
		letterFreqs.put('H', 4.96f);
		letterFreqs.put('I', 7.33f);
		letterFreqs.put('J', 0.22f);
		letterFreqs.put('K', 0.81f);
		letterFreqs.put('L', 4.21f);
		letterFreqs.put('M', 2.53f);
		letterFreqs.put('N', 7.17f);
		letterFreqs.put('O', 7.47f);
		letterFreqs.put('P', 2.07f);
		letterFreqs.put('Q', 0.10f);
		letterFreqs.put('R', 6.33f);
		letterFreqs.put('S', 6.73f);
		letterFreqs.put('T', 8.94f);
		letterFreqs.put('U', 2.68f);
		letterFreqs.put('V', 1.06f);
		letterFreqs.put('W', 1.83f);
		letterFreqs.put('X', 0.19f);
		letterFreqs.put('Y', 1.72f);
		letterFreqs.put('Z', 0.11f);
		return letterFreqs;
	}
	
	// For binary strings a and b the Hamming distance is equal to the number of ones 
		// (population count) in a XOR b.
	public static int hamming_distance(BitSet a, BitSet b) {
		BitSet xorred = BitSet.valueOf(a.toByteArray());
		xorred.xor(b);
		return xorred.cardinality();
	}
	
	public static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
	
	public static String onlyAlfabetic(String str) {
		IntStream filtered = str.toUpperCase().chars().filter(i -> i >= Character.valueOf('A') && i <= Character.valueOf('Z'));
		String onlyUpperCaseAlfabetic = convert(filtered);
		return onlyUpperCaseAlfabetic;
	}
	
	public static String convert(IntStream ints) {
		Stream<Character> chars = ints.mapToObj(i -> Character.valueOf((char)i));
		String str = chars.map(c -> String.valueOf(c)).collect(Collectors.joining());
		return str;
	}
	
	public static Map<Character, Integer> observedLetterFrequency(String str) {
		String alfabetic = onlyAlfabetic(str);
		Stream<Integer> ints = alfabetic.toUpperCase().chars().mapToObj(Integer::valueOf);
		Map<Character, Integer> result = ints.collect(Collectors.toMap(i -> Character.valueOf((char)i.intValue()), i -> 1, Integer::sum, HashMap::new));
		for(Character c : alfabet) {
			result.putIfAbsent(c, 0);
		}
		return result;
	}
	
	
	public static String sortByFreq(String str) {
		Map<Character, Integer> freqs = observedLetterFrequency(str);
		String inFreqOrder = freqs.entrySet()
								.stream()
								.sorted((a, b) -> b.getValue().compareTo(a.getValue()) )
								.map(entry -> String.valueOf(entry.getKey()))
								.collect(Collectors.joining());
		return inFreqOrder;
	}
	
	
	public static Map<Character, Integer> expectedLetterFrequency(String str) {
		return letterFrequencies().entrySet()
							.stream()
							.map(e -> new AbstractMap.SimpleImmutableEntry<Character, Integer>( e.getKey(), Math.round( (e.getValue() * str.length())/100 )) )
							.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
	
	
	public static <T> Double chiSquare(Map<T, Integer> expected, Map<T, Integer> observed) {
		Double chi2 = observed.entrySet().stream()
			.map(e -> {
				Integer exp = expected.get(e.getKey());
				
				if(exp == null || exp == 0) return 0.0d; // infrequent letters, such as X, may not occur in short texts
				
				Integer obs = e.getValue();
				Integer diff = obs - exp;
				double squaredDiff = Math.pow(diff, 2);
				double fraction = squaredDiff / exp;
				return fraction;
			})
			//.peek(System.out::println)
			.mapToDouble(d -> Double.valueOf(d))
			.sum();
		
		return chi2;
	}
	
	public static int distance(String sortedByFreq) {
		return minDistance(sortedByFreq, LETTER_FREQUENCY);
	}

	public static int levenshtein(String x, String y) {
	    int[][] dp = new int[x.length() + 1][y.length() + 1];
	 
	    for (int i = 0; i <= x.length(); i++) {
	        for (int j = 0; j <= y.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	                dp[i][j] = min(dp[i - 1][j - 1] 
	                 + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
	                  dp[i - 1][j] + 1, 
	                  dp[i][j - 1] + 1);
	            }
	        }
	    }
	 
	    return dp[x.length()][y.length()];
	}
	
	public static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
	
	public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
	
	public static String base16to64(String hex){
	    return Base64.getEncoder().encodeToString(new BigInteger(hex, 16).toByteArray());
	}
	
	public static byte[] base16tobytes(String hex){
	    return Base64.getDecoder().decode(base16to64(hex));
	}
	
	public static String bytestobase64(byte [] bytes){
	    return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static String bytestobase16(byte [] bytes) {
		BigInteger n = new BigInteger(bytes);
		String hexa = n.toString(16);
		return hexa;
	}

	public static byte[] xor(byte[] b1, byte[] b2) {
		byte[] result = new byte[b1.length];
		int i = 0;
		for (byte b : b1)
			result[i] = (byte) (b ^ b2[i++]);
		return result;
	}

	
	public static String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}

	
	public static BiFunction<String, Byte, List<String>> decrypt = (cipher, pwd) -> {
		List<String> msg = new ArrayList<>();
		byte[] asBytes = toByteArray(cipher);
		ByteBuffer bfr = ByteBuffer.wrap(asBytes);
		while(bfr.hasRemaining()) {
			byte toDecrypt = bfr.get();
			byte xorred = (byte) ((int)pwd.byteValue() ^ (int)toDecrypt);
			String str = new String(new byte[] {xorred});
			msg.add(str);
		}
		return msg;
	};
	
	
	public static Supplier<Map<String, String>> wordFrequencies = () -> {
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
	
	public static Supplier<Map<String, Float>> words = () -> {
		return words();
	};
	
	
	public static Supplier<Map<String, Double>> letterFrequencies = () -> {
		String txtFileName = "resources/letter_freqs_en.txt";
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(txtFileName)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String[] lines = content.split("\n");
		Map<String, Double> wordCount = Arrays.stream(lines)
												.map(line -> line.split(":"))
												.collect(Collectors.toMap(line -> line[0].trim(), line -> Double.valueOf(line[1].trim())));
		return wordCount;
	};

	public static byte[] base64tobytes(String cipher) {
		 return Base64.getDecoder().decode(cipher);
	}
	
	public static Decryption getMostLikelyPlainText(List<Decryption> decrypted) {
		return findBestMatch(decrypted);
	}
	
	public static List<Decryption> decrypt(String cipherText) {
		BitSet decoded = decode(cipherText);
		return decrypt(decoded);
	}
	
	public static List<Decryption> decrypt(BitSet decoded) {
		int numOfBytes = decoded.toByteArray().length;
		List<BitSet> pwds = makePasswords(numOfBytes);
		
		List<Decryption> solutions = pwds.stream().map(pwd -> new Decryption(xor(BitSet.valueOf(decoded.toByteArray()), pwd), new String(pwd.toByteArray(), Charset.forName("US-ASCII")))).collect(Collectors.toList());
		
		//solutions  = solutions.stream().filter(s -> isText(s.deciphered)).collect(Collectors.toList());
		solutions.forEach(s -> makePlainText(s));
		solutions.forEach(s -> s.letterFreqChi2 = letterFreqs(s.msg));
		//solutions.forEach(s -> s.digramChi2 = digrams(s.msg));
		return solutions;
	}
	
	public static boolean isText(BitSet deciphered) {
		return CryptoUtils.containsOnlyASCII(deciphered.toByteArray());
	}
	
	public static Decryption findBestMatch(List<Decryption> solutions) {
		return solutions.stream().min((s1, s2) -> s1.letterFreqChi2.compareTo(s2.letterFreqChi2)).get();
	}

	
	public static Double digrams(String txt) {
		Map<String, Integer> obs = CryptoUtils.observedDigrams(txt);
		Map<String, Integer> exp = CryptoUtils.expectedDigrams(txt);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		return chi2;
	}
	
	public static Double letterFreqs(String txt) {
		Map<Character, Integer> obs = CryptoUtils.observedLetterFrequency(txt);
		Map<Character, Integer> exp = CryptoUtils.expectedLetterFrequency(txt);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		return chi2;
	}
	
	public static BitSet decode(String cipherText) {
		BitSet decoded = BitSet.valueOf(CryptoUtils.base16tobytes(cipherText));
		return decoded;
	}
	
	public static List<BitSet> makePasswords(int numOfBytes) {
		Stream<List<Byte>> byteStream = IntStream.range(32, 128)
				.mapToObj(i -> (byte)i)
				.map(b -> Stream.iterate(0, n -> n +1).limit(numOfBytes).map(n -> b).collect(Collectors.toList()) );

		List<BitSet> pwds = byteStream.map(listOfBytes -> {
				byte [] bytes = new byte[numOfBytes];
				int i = 0;
				for(Byte bObj : listOfBytes) {
					byte b = bObj.byteValue();
					bytes[i++] = b;
				}
				BitSet bs = BitSet.valueOf(bytes);
				return bs;
			})
			.collect(Collectors.toList());
		
		return pwds;
	}
	
	public static BitSet xor(BitSet cipher, BitSet pwd) {
		cipher.xor(pwd);
		return cipher;
	}
	
	public static Decryption makePlainText(Decryption d) {
		String plainText = new String(d.deciphered.toByteArray(), Charset.forName("US-ASCII"));
		d.msg = plainText;
		return d;
	}
	
	
	public static class Decryption {
		public Decryption(BitSet d, String password) {
			this.deciphered = d;
			this.password = password;
		}
		public String cipherText;
		public BitSet cipher;
		public BitSet deciphered;
		public String msg;
		public String password;
		public Double letterFreqChi2;
		public Double digramChi2;
	}
}
