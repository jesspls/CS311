import java.util.Scanner;

/**
 *Finds the shortest time taken to spread rumor to all broker's in a given data set.
 *1. Compute the shortest path from root to all other vertex (floyd)
 *2. The shortest time taken will be the maximum of all paths in 1
 *
 * @author Jessica Haynes
 */

public class rumors {

	static int MAX_NUM_PEOPLE = 101; // 100 people total possible
	static int MAX_DISTANCE = 1001; // 100 max people with max distance 10 each
									// = 1,000
	static int graph[][] = new int[MAX_NUM_PEOPLE][MAX_DISTANCE]; //G[x][y] is the weight between broker x and y
	static int n;

	public static void main(String[] args) {

		int numConnected, personNum, i, j;

		//Create scanner and get # people in first data set
		Scanner scanner = new Scanner(System.in);
		n = scanner.nextInt();
		
		while (scanner.hasNextInt()) {

			// initialize/reset the matrix path to 0
			for (i = 0; i < n; i++)
				for (j = 0; j < n; j++)
					graph[i][j] = 0;

			// read in data for current dataset and put into graph
			for (i = 0; i < n; i++) {
				numConnected = scanner.nextInt();
				for (j = 0; j < numConnected; j++) {
					personNum = scanner.nextInt();
					graph[i][personNum - 1] = scanner.nextInt();
				}
			}
			
			// Finds all brokers that are not connected and sets their distance to MAX_DISTANCE
			for (i = 0; i < n; i++) {
				for (j = 0; j < n; j++) {
					if (i != j && graph[i][j] == 0)
						graph[i][j] = MAX_DISTANCE;
				}
			}
			
			// Calls floyd algorithm to fill in the rest of the graph with shortest distances
			floyd(); 
			
			int longest, shortestTotal = MAX_DISTANCE, curPerson = 0;
			
			
			//Finds the shortest distance for all of the brokers, and each individual broker's longest distance
			for (i = 0; i < n; i++) 
			{
				longest = 0; 
				for (j = 0; j < n; j++)
					if (graph[i][j] > longest)
						longest = graph[i][j];
				if (longest < shortestTotal)
				{
					curPerson = i;
					shortestTotal = longest;
				}
			}
			
			if (shortestTotal < MAX_DISTANCE)
				System.out.println(curPerson + 1 + " " + shortestTotal);
			else
				//We have an unconnected graph if the shortest distance is still the max distance
				System.out.println("disjoint"); 

			n = scanner.nextInt();
		}

	}

	/**
	 * Implementation of floyd's algorithm to find shortest paths
	 */
	public static void floyd() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					if (graph[j][k] > graph[j][i] + graph[i][k])
						graph[j][k] = graph[j][i] + graph[i][k];
				}
			}
		}
	}
}