package cryptopals.set1;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import utils.CryptoUtils;

public class Challenge6 {

	
	@Test
	public void test1() throws IOException {
		String cipher = Files.lines(Paths.get("resources/6.txt")).collect(Collectors.joining());
		byte [] bytes = CryptoUtils.base64tobytes(cipher);
		for(int keySize = 2; keySize <= 40; keySize++) {
			CryptoUtils.split(bytes, keySize);
		}
	}
	
	
	@Test
	public void test2() throws UnsupportedEncodingException {
		String txt1 = "this is a test";
		String txt2 = "wokka wokka!!!";
		BitSet a = BitSet.valueOf(txt1.getBytes("US-ASCII"));
		BitSet b = BitSet.valueOf(txt2.getBytes("US-ASCII"));
		int d = CryptoUtils.hamming_distance(a, b);
		assertEquals(37, d);
	}
	
	@Test
	public void test() throws IOException {
		breakVigenere("resources/6.txt");
	}
	

	public void breakVigenere(String cipherPath) throws IOException {
		String cipher = Files.lines(Paths.get(cipherPath)).collect(Collectors.joining());
		byte [] bytes = CryptoUtils.base64tobytes(cipher);
		List<Double> distances = new ArrayList<>();
		final int startPos = 2;
		final int endPos = 40;
		for(int keySize = startPos; keySize <= endPos; keySize++) {
			List<BitSet> rows = new ArrayList<>();
			byte [][] cols = CryptoUtils.split(bytes, keySize);
			final int maxRowIdx = 2;
			for(int rowIdx = 0; rowIdx < maxRowIdx; rowIdx++) {
				byte [] row = new byte[keySize];
				for(int colIdx = 0; colIdx < keySize; colIdx++) {
					row[colIdx] = cols[colIdx][rowIdx];
				}
				BitSet bs = BitSet.valueOf(row);
				rows.add(bs);
			}
			final double d = CryptoUtils.hamming_distance(rows.get(0), rows.get(1)) / keySize;
			distances.add(d);
		}
		double min = Double.MAX_VALUE;
		int expectedKeySize = 0;
		for(int i = 0; i < distances.size(); i++) {
			final int keySize = i + startPos;
			double d = distances.get(i);
			if(d < min) {
				min = d;
				expectedKeySize = keySize;
			}
			//System.out.println(keySize + " -> " + d);
		}
		System.out.println(expectedKeySize + " -> " + min);
		
		// first index are the columns, second index are the rows
		// thus all characters encrypted with the first character of the password are 
		// stored in column 0, etc.
		byte[][] blocks = CryptoUtils.split(bytes, expectedKeySize);
		List<BitSet> columns = new ArrayList<>();
		for(int colIdx = 0; colIdx < blocks.length; colIdx++) {
			BitSet bs = BitSet.valueOf(blocks[colIdx]);
			columns.add(bs);
		}
		
		/*
		 * The solutions are ordered per column -> password, for each password a decrypted byte array exists.
		 * 
		 * The stream is terminated at the last possible instance. All byte-level operation are sequential for-loops.
		 * Each operation is parallelised to utilise the 8-cores of this machine as optimally as possible. The stream
		 * operations are used to operate on the solutions. Stream offer an excellent way to lazily evaluate the operations
		 * per solution and to parallelise the operations on each solution.
		 * 
		 * The filter operation clearly breaks the correspondence of index to password, and ordering can't be guaranteed.
		 * A state object must be introduced to keep track of the relations (password, column index, cipher, plain text, chi2).
		 */
		
		class Deciphered {
			char password;
			int colIdx;
			byte [] cipher;
			String plainText;
			double chi2;
			byte [] deciphered;
		}
		
		Stream<Stream<byte[]>> solutions = columns.stream()
											.parallel()
											.map(col -> {
												 return Stream.iterate(32, n -> n + 1).limit(128).map(pwd -> {
													byte [] rows = col.toByteArray();
													byte [] deciphered = new byte[rows.length];
													for(int rowIdx = 0; rowIdx < rows.length; rowIdx++) {
														byte b = rows[rowIdx];
														byte xorred = (byte)((int) b ^ pwd);
														deciphered[rowIdx] = xorred;
													}
													return deciphered;
												});
											});
		
		solutions = solutions
						.parallel()
						.map(cols -> {
								return cols.filter(deciphered -> {
									for(int i = 0; i < bytes.length; i++) {
										byte b = deciphered[i];
										if(32 <= b && b <= 127) {
											continue;
										} else {
											return false;
										}
									}
									return true;
								});
							});
							
		Stream<Stream<String>> plainTexts = solutions
												.parallel()
												.map(cols -> {
													return cols.map(xorred -> {
														String plainText = new String(xorred, Charset.forName("US-ASCII"));
														return plainText;
													});
												});
		
		
		
	}
	
}
