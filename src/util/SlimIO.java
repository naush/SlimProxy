package util;

import java.io.BufferedWriter;

public class SlimIO {
	public static String readInstructions(StreamReader reader)
			throws Exception {
		int instructionLength = Integer.parseInt(reader.read(6));
		reader.read(1);
		return reader.read(instructionLength);
	}
	public static String writeString(BufferedWriter writer, String instruction) throws Exception {
		String string = String.format("%06d:%s",
				instruction.getBytes("UTF-8").length, instruction);
		writer.write(string);
		writer.flush();
		return string;
	}
}
