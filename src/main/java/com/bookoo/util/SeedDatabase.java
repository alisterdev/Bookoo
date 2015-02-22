package com.bookoo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Darrel-Day Guerrero
 *
 */
public class SeedDatabase implements Serializable  {

	public static String loadAsString(final String path) {
		try (InputStream inputStream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(path)) {
			return new Scanner(inputStream).useDelimiter("\\A").next();
		} catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}
	}

	public static List<String> splitStatements(Reader reader,
			String statementDelimiter) {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder sqlStatement = new StringBuilder();
		final List<String> statements = new LinkedList<String>();
		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || isComment(line)) {
					continue;
				}
				sqlStatement.append(line);
				if (line.endsWith(statementDelimiter)) {
					statements.add(sqlStatement.toString());
					sqlStatement.setLength(0);
				}
			}
			return statements;
		} catch (IOException e) {
			throw new RuntimeException("Failed parsing sql", e);
		}
	}

	public static boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("//")
				|| line.startsWith("/*");
	}
}
