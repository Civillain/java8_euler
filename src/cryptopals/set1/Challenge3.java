package cryptopals.set1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import utils.CryptoUtils;
import utils.CryptoUtils.Decryption;

public class Challenge3 {
	
	
	
	//Single-byte XOR cipher
	
	@Test
	public void challeng3() {
		String orig = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		Decryption found = CryptoUtils.getMostLikelyPlainText(CryptoUtils.decrypt(orig));
		System.out.println(found.msg);
		System.out.println(found.password);
		System.out.println(found.letterFreqChi2);
	}
	
	@Test
	public void challeng4() throws IOException {
		long start = System.currentTimeMillis();
		try (Stream<String> ciphers = Files.lines(Paths.get("resources/challenge4.txt"))) {
			List<Decryption> allDecrypted = ciphers
												//.parallel()
												.map(cipherText -> CryptoUtils.decrypt(cipherText))
												.flatMap(decrypted -> decrypted.stream())
												.collect(Collectors.toList());
			
			//allDecrypted.forEach(d -> System.out.println(d.msg));
			
			Decryption found = CryptoUtils.getMostLikelyPlainText(allDecrypted);
			System.out.println(found.msg);
			System.out.println(found.password);
			System.out.println(found.letterFreqChi2);
		}
		long end = System.currentTimeMillis() - start;
		System.out.println(end +"ms." );
	}
	
	
	
	@Test
	public void testRandomLetterFreqs() {
		Random r = new Random();
		int numOfPrintableASCIICHars = 127 - 32;
		IntStream randomInts = IntStream.range(0, 500).map(i -> r.nextInt(numOfPrintableASCIICHars)).map(i -> i+32);
		String randomString = CryptoUtils.convert(randomInts);
		Map<Character, Integer> obs = CryptoUtils.observedLetterFrequency(randomString);
		Map<Character, Integer> exp = CryptoUtils.expectedLetterFrequency(randomString);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void testRandomChi2_bigrams() {
		Random r = new Random();
		int numOfPrintableASCIICHars = 127 - 32;
		IntStream randomInts = IntStream.range(0, 500).map(i -> r.nextInt(numOfPrintableASCIICHars)).map(i -> i+32);
		String randomString = CryptoUtils.convert(randomInts);
		Map<String, Integer> obs = CryptoUtils.observedDigrams(randomString);
		Map<String, Integer> exp = CryptoUtils.expectedDigrams(randomString);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void testChi2() {
		String str = "Do you think I cannot call on my Father, and he will at once put at my disposal more than twelve legions of angels?";
		Map<Character, Integer> obs = CryptoUtils.observedLetterFrequency(str);
		Map<Character, Integer> exp = CryptoUtils.expectedLetterFrequency(str);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void testChi2_digrams() {
		String str = "Do you think I cannot call on my Father, and he will at once put at my disposal more than twelve legions of angels?";
		Map<String, Integer> obs = CryptoUtils.observedDigrams(str);
		Map<String, Integer> exp = CryptoUtils.expectedDigrams(str);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void testChi2_largeText() {
		String str = "The Beginning  1 In the beginning God created the heavens and the earth. 2 Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.  3 And God said, “Let there be light,” and there was light. 4 God saw that the light was good, and he separated the light from the darkness. 5 God called the light “day,” and the darkness he called “night.” And there was evening, and there was morning—the first day.  6 And God said, “Let there be a vault between the waters to separate water from water.” 7 So God made the vault and separated the water under the vault from the water above it. And it was so. 8 God called the vault “sky.” And there was evening, and there was morning—the second day.  9 And God said, “Let the water under the sky be gathered to one place, and let dry ground appear.” And it was so. 10 God called the dry ground “land,” and the gathered waters he called “seas.” And God saw that it was good.  11 Then God said, “Let the land produce vegetation: seed-bearing plants and trees on the land that bear fruit with seed in it, according to their various kinds.” And it was so. 12 The land produced vegetation: plants bearing seed according to their kinds and trees bearing fruit with seed in it according to their kinds. And God saw that it was good. 13 And there was evening, and there was morning—the third day.  14 And God said, “Let there be lights in the vault of the sky to separate the day from the night, and let them serve as signs to mark sacred times, and days and years, 15 and let them be lights in the vault of the sky to give light on the earth.” And it was so. 16 God made two great lights—the greater light to govern the day and the lesser light to govern the night. He also made the stars. 17 God set them in the vault of the sky to give light on the earth, 18 to govern the day and the night, and to separate light from darkness. And God saw that it was good. 19 And there was evening, and there was morning—the fourth day.  20 And God said, “Let the water teem with living creatures, and let birds fly above the earth across the vault of the sky.” 21 So God created the great creatures of the sea and every living thing with which the water teems and that moves about in it, according to their kinds, and every winged bird according to its kind. And God saw that it was good. 22 God blessed them and said, “Be fruitful and increase in number and fill the water in the seas, and let the birds increase on the earth.” 23 And there was evening, and there was morning—the fifth day.  24 And God said, “Let the land produce living creatures according to their kinds: the livestock, the creatures that move along the ground, and the wild animals, each according to its kind.” And it was so. 25 God made the wild animals according to their kinds, the livestock according to their kinds, and all the creatures that move along the ground according to their kinds. And God saw that it was good.  26 Then God said, “Let us make mankind in our image, in our likeness, so that they may rule over the fish in the sea and the birds in the sky, over the livestock and all the wild animals,[a] and over all the creatures that move along the ground.”  27 So God created mankind in his own image,      in the image of God he created them;      male and female he created them.  28 God blessed them and said to them, “Be fruitful and increase in number; fill the earth and subdue it. Rule over the fish in the sea and the birds in the sky and over every living creature that moves on the ground.”  29 Then God said, “I give you every seed-bearing plant on the face of the whole earth and every tree that has fruit with seed in it. They will be yours for food. 30 And to all the beasts of the earth and all the birds in the sky and all the creatures that move along the ground—everything that has the breath of life in it—I give every green plant for food.” And it was so.  31 God saw all that he had made, and it was very good. And there was evening, and there was morning—the sixth day.";
		Map<Character, Integer> obs = CryptoUtils.observedLetterFrequency(str);
		Map<Character, Integer> exp = CryptoUtils.expectedLetterFrequency(str);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void testChi2_largeText_digrams() {
		String str = "The Beginning  1 In the beginning God created the heavens and the earth. 2 Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.  3 And God said, “Let there be light,” and there was light. 4 God saw that the light was good, and he separated the light from the darkness. 5 God called the light “day,” and the darkness he called “night.” And there was evening, and there was morning—the first day.  6 And God said, “Let there be a vault between the waters to separate water from water.” 7 So God made the vault and separated the water under the vault from the water above it. And it was so. 8 God called the vault “sky.” And there was evening, and there was morning—the second day.  9 And God said, “Let the water under the sky be gathered to one place, and let dry ground appear.” And it was so. 10 God called the dry ground “land,” and the gathered waters he called “seas.” And God saw that it was good.  11 Then God said, “Let the land produce vegetation: seed-bearing plants and trees on the land that bear fruit with seed in it, according to their various kinds.” And it was so. 12 The land produced vegetation: plants bearing seed according to their kinds and trees bearing fruit with seed in it according to their kinds. And God saw that it was good. 13 And there was evening, and there was morning—the third day.  14 And God said, “Let there be lights in the vault of the sky to separate the day from the night, and let them serve as signs to mark sacred times, and days and years, 15 and let them be lights in the vault of the sky to give light on the earth.” And it was so. 16 God made two great lights—the greater light to govern the day and the lesser light to govern the night. He also made the stars. 17 God set them in the vault of the sky to give light on the earth, 18 to govern the day and the night, and to separate light from darkness. And God saw that it was good. 19 And there was evening, and there was morning—the fourth day.  20 And God said, “Let the water teem with living creatures, and let birds fly above the earth across the vault of the sky.” 21 So God created the great creatures of the sea and every living thing with which the water teems and that moves about in it, according to their kinds, and every winged bird according to its kind. And God saw that it was good. 22 God blessed them and said, “Be fruitful and increase in number and fill the water in the seas, and let the birds increase on the earth.” 23 And there was evening, and there was morning—the fifth day.  24 And God said, “Let the land produce living creatures according to their kinds: the livestock, the creatures that move along the ground, and the wild animals, each according to its kind.” And it was so. 25 God made the wild animals according to their kinds, the livestock according to their kinds, and all the creatures that move along the ground according to their kinds. And God saw that it was good.  26 Then God said, “Let us make mankind in our image, in our likeness, so that they may rule over the fish in the sea and the birds in the sky, over the livestock and all the wild animals,[a] and over all the creatures that move along the ground.”  27 So God created mankind in his own image,      in the image of God he created them;      male and female he created them.  28 God blessed them and said to them, “Be fruitful and increase in number; fill the earth and subdue it. Rule over the fish in the sea and the birds in the sky and over every living creature that moves on the ground.”  29 Then God said, “I give you every seed-bearing plant on the face of the whole earth and every tree that has fruit with seed in it. They will be yours for food. 30 And to all the beasts of the earth and all the birds in the sky and all the creatures that move along the ground—everything that has the breath of life in it—I give every green plant for food.” And it was so.  31 God saw all that he had made, and it was very good. And there was evening, and there was morning—the sixth day.";
		Map<String, Integer> obs = CryptoUtils.observedDigrams(str);
		Map<String, Integer> exp = CryptoUtils.expectedDigrams(str);
		Double chi2 = CryptoUtils.chiSquare(exp, obs);
		System.out.println(chi2);
	}
	
	@Test
	public void encryptDummy() {
		String txt = "Now that the party is jumping!";
		String pwd = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		String hex = CryptoUtils.bytestobase16(txt.getBytes());
		System.out.println(hex);
		BitSet bits = BitSet.valueOf(txt.getBytes());
		BitSet pwdBin = BitSet.valueOf(pwd.getBytes());
		bits.xor(pwdBin);
		String encrypted = CryptoUtils.bytestobase16(bits.toByteArray());
		System.out.println(encrypted);
	}
}
