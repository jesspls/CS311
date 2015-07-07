import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class larvaeWalk {

	// represents all steps that need to be taken to visit each of cells
	// neighbors
	static int[][] move = { { -1, -1 }, { 0, -1 }, { 1, 0 }, { 1, 1 },
			{ 0, 1 }, { -1, 0 } };

	// comb[n][x][y] = # ways that we can start from (15,15), and after n steps,
	// we end up in (x, y)
	static int[][][] comb = new int[15][31][31];

	public static void main(String[] args) {

		int i, row, col, j;

		// initlialize all of comb to 0
		for (i = 1; i < 15; i++) {
			for (row = 1; row < 30; row++) {
				for (col = 1; col < 30; col++) {
					comb[i][row][col] = 0;
				}
			}
		}

		// Open the scanner to take in from System.in
		Scanner scanner = new Scanner(System.in);

		// While you continue to give it numbers
		while (scanner.hasNextInt()) {

			int toCalc = scanner.nextInt();

			comb[0][15][15] = 1;

			// Main logic
			for (i = 1; i < toCalc + 1; i++) {
				for (row = 1; row < 30; row++) {
					for (col = 1; col < 30; col++) {

						if (comb[i][row][col] != 0) {
							// Stops it from calculating a value that has
							// already been calculated and stored.
						} else {

							// For each i, row, and col.. it adds up the values
							// of all of it's neighbors
							for (j = 0; j < 6; j++) {
								int cur_row = row + move[j][0];
								int cur_col = col + move[j][1];
								comb[i][row][col] += comb[i - 1][cur_row][cur_col];
							}
						}
					}
				}
			}

			System.out.println(comb[toCalc][15][15]);
		}

	}
}