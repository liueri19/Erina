package erina.core;

import java.io.*;

/**
 * Log events happened in the Erina.
 *
 * @version 1.0
 * @author Eric
 */
class Logger {

	private static final Writer OUTPUT;

	static {
		// stdout
		OUTPUT = new BufferedWriter(new OutputStreamWriter(System.out));

		// or some file
//		final String fileName = "Erina_" + LocalDateTime.now().toString() + ".log";
//
//		Writer tempWriter;
//		try {
//			tempWriter = Files.newBufferedWriter(Paths.get(fileName));
//		}
//		catch (IOException e) {
//			System.err.println("Failed to open file: " + fileName);
//			e.printStackTrace();
//			tempWriter = new BufferedWriter(new OutputStreamWriter(System.out));
//		}
//
//		OUTPUT = tempWriter;
	}

	/**
	 * Logs a message like printf.
	 */
	static void log(String message, Object... args) {
		message = String.format(message, args);
		tryWrite(message);
	}

	/**
	 * Logs a message like printf followed by a line separator.
	 */
	static void logLine(String message, Object... args) {
		log(message + "%n", args);
	}

	/**
	 * Produces an empty line in the log.
	 */
	static void logLine() {
		logLine("");
	}


	/**
	 * Writes and flushes without throwing checked exceptions.
	 */
	private static void tryWrite(String message) {
		try {
			OUTPUT.write(message);
			OUTPUT.flush();
		}
		catch (IOException e) {
			System.err.println("Exception occurred while logging");
			e.printStackTrace();
		}
	}
}
