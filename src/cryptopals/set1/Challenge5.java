package cryptopals.set1;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Test;

import utils.CryptoUtils;

public class Challenge5 {
	String msg = "Burning 'em, if you ain't quick and nimble\n" + 
			"I go crazy when I hear a cymbal";
	
	String key = "ICE";
	
	String expected = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272"
						+ "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"; 
			
	
	String actual = "b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272"
						+ "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
	
	@Test
	public void test() throws UnsupportedEncodingException {
		byte [] bytes = msg.getBytes("US-ASCII");
		byte [] keyBytes = key.getBytes("US-ASCII");
		byte [] encrypted = new byte[bytes.length];
		for(int i = 0; i < bytes.length; i++) {
			int p = i % keyBytes.length;
			byte toEncryptWith = keyBytes[p];
			byte b = bytes[i];
			byte xorred = (byte)( (int)b ^ (int)toEncryptWith);
			encrypted[i] = xorred;
		}
		String hex = CryptoUtils.bytestobase16(encrypted);
		System.out.println(hex);
		assertEquals(expected, hex);
	}
	
	@Test
	public void reverse() throws UnsupportedEncodingException {
		byte [] bytes = CryptoUtils.base16tobytes(expected);
		byte [] keyBytes = key.getBytes("US-ASCII");
		byte [] decrypted = new byte[bytes.length];
		for(int i = 0; i < bytes.length; i++) {
			int p = i % keyBytes.length;
			byte toEncryptWith = keyBytes[p];
			byte b = bytes[i];
			byte xorred = (byte)( (int)b ^ (int)toEncryptWith);
			decrypted[i] = xorred;
		}
		String deciphered = new String(decrypted, Charset.forName("US-ASCII"));
		System.out.println(deciphered);
		assertEquals(msg, deciphered);
	}
}
