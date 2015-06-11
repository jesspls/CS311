import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

public class Graduate {

	public static void main(String[] args) throws Exception {

		DirectedAcyclicGraph dag = new DirectedAcyclicGraph(Course.class);

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

		// Reads in number of courses total and number of courses allowed per
		// semester
		int numCourses = scanner.nextInt();
		int numSemester = scanner.nextInt();

		// Skips second line of individual data sets
		scanner.nextLine();
		scanner.nextLine();

		Course[] courses = new Course[numCourses];

		// Reads in each line of course data and creates course objects, adds
		// them to the dag
		for (int i = 0; i < numCourses; i++) {
			String name = scanner.next();
			String semester = scanner.next();
			int numPrereqs = scanner.nextInt();
			String[] prereqs = new String[numPrereqs];

			for (int j = 0; j < numPrereqs; j++) {
				prereqs[j] = scanner.next();
			}

			Course course = new Course(name, numPrereqs, semester, prereqs);
			dag.addVertex(course);
			courses[i] = course;

		}

		// Adds the edges to the dag
		for (int i = 0; i < courses.length; i++) {
			if (courses[i].numPreReqs > 0) {
				for (int j = 0; j < courses[i].numPreReqs; j++) {
					Course begin = findCourse(courses[i].prereqs[j], courses);
					dag.addEdge(begin, courses[i]);
				}

			}
		}

		backTrackMin("F", dag, numSemester);

	}

	public static void backTrackMin(String term, DirectedAcyclicGraph dag,
			int numSemester) {

		ArrayList<Course> courses = new ArrayList<Course>();
		Iterator iterator = dag.iterator();

		// Check to see if dag is empty, if it is check to see if change in min
		if (!iterator.hasNext()) {
			return;
		}

		// Gets all the courses from the dag that can currently be taken,
		// including sorting out those that aren't this semester
		while (iterator.hasNext()) {

			Course toAdd = (Course) iterator.next();
			if (dag.incomingEdgesOf(toAdd).isEmpty()) {
				if ((toAdd.semester.equals("B") || toAdd.semester.equals(term))) {
					courses.add(toAdd);
				}
			} else {
				break;
			}
		}

		// Adds check to see if total courses can take are less than max,
		// because then no set generation is necessary
		if (courses.size() <= numSemester) {

		}

	}
	
	/**
	 * Finds the course object given a string name
	 * @param name
	 * @param courses
	 * @return course that has given name
	 * @throws Exception
	 */

	public static Course findCourse(String name, Course[] courses)
			throws Exception {

		for (int i = 0; i < courses.length; i++) {
			if (courses[i].name.equals(name))
				return courses[i];
		}

		throw new Exception("Name not found");
	}

}
