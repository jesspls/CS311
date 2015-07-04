import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class larvaeWalk {

	public static void main(String[] args) {

		// Gets file name from user input and creates a scanner of file
		Scanner scanner = createScanner();

	}

	public static Scanner createScanner() {
		// Takes input from user for file name
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.next();
		scanner.close();

		// Opens file from given file name
		File file = new File(fileName);

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return scanner;
	}

}