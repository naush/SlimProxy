package util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.junit.Test;

public class SlimIOTest {
	@Test
	public void testReadInstructions() throws NumberFormatException, Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(new String("000001:i").getBytes());
		StreamReader reader = new StreamReader(is);
		String instructions = SlimIO.readInstructions(reader);
		assertEquals("i", instructions);
	}
	@Test
	public void testWriteString() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		assertEquals("000001:i", SlimIO.writeString(writer, "i"));
	}
}
