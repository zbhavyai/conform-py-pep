import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.Console;

/**
 * Provides methods to analyze the .py file and prints some of the non
 * conformance based on PEP8 style guide.
 *
 * @author Bhavyai Gupta
 * @since 1.0
 */
public class Analyzer {
	private Console console;
	private String COLOR;
	private String YELOW;
	private String RESET;
	/**
	 * <code>true</code> if there has been atleast one flag raised by the program;
	 * <code>false</code> otherwise
	 */
	private boolean atleastOne;

	/**
	 * Constructs a Analyzer object with default values of the data fields.
	 */
	Analyzer() {
		console = System.console();
		COLOR = "\033[0;91m";
		YELOW = "\033[0;93m";
		RESET = "\033[0m";
		atleastOne = false;
	}

	/**
	 * Loop over all the lines of the python file, and pass each line to
	 * lineAnalyzer(). Print start and end messages.
	 *
	 * @param pyFile The <code>File</code> pointing to the .py file
	 */
	public void fileAnalyzer(File pyFile) {
		int lineCount = 0;
		try {
			Scanner sc = new Scanner(new BufferedReader(new FileReader(pyFile)));

			console.printf("%n%nChecking file %s%s%s%n", YELOW, pyFile.getName(), RESET);

			while (sc.hasNextLine()) {
				this.lineAnalyzer(sc.nextLine(), ++lineCount);
			}

			if (atleastOne == false) {
				console.printf("%n%nConformed!%n%n");
			}

			else {
				console.printf("%n%nChecks complete!%n%n");
			}

			sc.close();
		}

		catch (FileNotFoundException e) {
			console.printf("%n%nFile not found! Exit%n");
			System.exit(1);
		}
	}

	/**
	 * Loop over all the characters of the String, to analyze the PEP8 conformity
	 *
	 * @param str       The string passed to be analyzed by the function
	 * @param lineCount The current line number in the file
	 */
	private void lineAnalyzer(String str, int lineCount) {
		// dont access empty lines
		if (str.length() == 0) {
			return;
		}

		char prevChar = '\n';
		char currChar;
		char nextChar = '\n';
		boolean commentMode = false;
		boolean stringMode = false;

		// loop through all the characters of the string
		for (int i = 0; i < str.length(); i++) {
			currChar = str.charAt(i);

			// read the nextChar ahead, to make easier ahead
			// if nextChar can't be read, its already a '\n'
			if ((i + 1) < str.length()) {
				nextChar = str.charAt(i + 1);
			}

			// if encountered pound sign, turn on the commentMode
			// to prevent style warnings in comments
			if (currChar == '#') {
				commentMode = true;
			}

			// if encounterd quote sign, toggle the stringMode
			// to prevent style warnings in strings
			if ((currChar == '\"') && (stringMode == false)) {
				stringMode = true;
			}

			else if (currChar == '\"' && (stringMode == true)) {
				stringMode = false;
			}

			// RULE - no tab indentation (no tabs anywhere)
			if (currChar == '\t') {
				console.printf("%n%sLine %4d%s: %s %d", COLOR, lineCount, RESET, "tab found at column ", i + 1);
				this.atleastOne = true;
			}

			// RULE - space after comment starts
			else if ((currChar == '#') && !((nextChar == ' ') || (nextChar == '\n'))) {
				console.printf("%n%sLine %4d%s: %s %d", COLOR, lineCount, RESET, "no space after # at column", i + 1);
				this.atleastOne = true;
			}

			// RULE - space around every operator
			else if (isOperator(currChar)) {
				if (!(isOperator(prevChar) || prevChar == ' ')) {
					if (!commentMode && !stringMode) {
						console.printf("%n%sLine %4d%s: %s %c %s %d", COLOR, lineCount, RESET, "no space before",
								currChar, "at column", i + 1);
						this.atleastOne = true;
					}
				}

				if (!(isOperator(nextChar) || nextChar == ' ')) {
					if (!commentMode && !stringMode) {
						console.printf("%n%sLine %4d%s: %s %c %s %d", COLOR, lineCount, RESET, "no space after",
								currChar, "at column", i + 1);
						this.atleastOne = true;
					}
				}
			}

			prevChar = currChar;
		}
	}

	/**
	 * Check if the parameter passed is an operator in python
	 *
	 * @param c of type <code>char</code>
	 * @return <code>true</code> if c is of type <code>char</code>;
	 *         <code>false</code> otherwise
	 */
	private boolean isOperator(char c) {
		boolean flag = false;

		switch (c) {
			case '=':
			case '+':
			case '-':
			case '*':
			case '/':
			case '%':
			case '!':
			case '>':
			case '<':
			case '&':
			case '|':
			case '^':
			case '~':
				flag = true;
				break;
		}

		return flag;
	}
}
