import java.io.Console;
import java.io.File;

/**
 * Provides data fields and methods to receive path to a .py file from the user.
 *
 * @author Bhavyai Gupta
 * @since 1.0
 */
public class App {
	/**
	 * The file created based on user input would be stored here.
	 */
	private File pythonFile;
	private Console console;

	/**
	 * Constructs a App object and initializes the console.
	 */
	public App() {
		this.console = System.console();

		if (this.console == null) {
			System.err.println("%n%nApplication not started in console");
			Runtime.getRuntime().exit(1);
		}
	}

	/**
	 * Get a string representing a valid python file from the user.
	 */
	void getValidString() {
		String temp = null;

		while (true) {
			temp = this.console.readLine("%n%nEnter the full path of the python filename: ");

			if (isValidFile(temp)) {
				return;
			}

			this.console.printf("%n%nPlease enter a valid python file name with .py extension");
		}
	}

	/**
	 * Check if the string passed to this function is a valid python file.
	 *
	 * @param pythonFilePath
	 * @return {@code true} is file is valid python file, {@code false} otherwise
	 */
	boolean isValidFile(String pythonFilePath) {
		this.pythonFile = new File(pythonFilePath);

		if (pythonFile.isFile() && pythonFile.getAbsolutePath().endsWith(".py")) {
			return true;
		}

		else {
			return false;
		}
	}

	public static void main(String[] args) {
		App a = new App();

		if (args.length == 0) {
			a.getValidString();
		}

		else if (!a.isValidFile(args[0])) {
			a.getValidString();
		}

		Analyzer an = new Analyzer();
		an.fileAnalyzer(a.pythonFile);
	}
}
