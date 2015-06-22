import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

public class Graduate {

	static int min = Integer.MAX_VALUE;

	public static void main(String[] args) throws Exception {

		// Takes user input for file name, opens file, and creates scanner
		Scanner scanner = createScanner();

		while (scanner.hasNext()) {

			min = Integer.MAX_VALUE;

			DirectedAcyclicGraph dag = new DirectedAcyclicGraph(Course.class);

			// Reads in number of courses total and number of courses allowed
			// per
			// semester
			int numCourses = scanner.nextInt();
			int numSemester = scanner.nextInt();

			if (numCourses == -1 && numSemester == -1) {
				break;
			}

			// Skips second line of individual data sets
			scanner.nextLine();
			scanner.nextLine();

			// Scans in the course information and creates course objects,
			// adding
			// them to the dag
			Course[] courses = createCourses(numCourses, scanner, dag);

			// Adds the edges to the dag
			addEdges(courses, dag, courses);

			// Get first set of leaves (aka eligible courses to be taken)
			ArrayList<Course> leaves = availableCourses(dag, "F");

			backTrackMin("F", dag, numSemester, leaves, 0, courses);

			System.out.println("The minimum number of semesters required to graduate is " + min + ".");

			scanner.nextLine();
		}

	}

	/**
	 * recursively finds the min value of semesters needed to graduate
	 * 
	 * @param term
	 * @param dag
	 * @param numSemester
	 * @param leaves
	 * @param newMin
	 * @param courses
	 * @throws Exception
	 */

	public static void backTrackMin(String term, DirectedAcyclicGraph dag,
			int numSemester, ArrayList<Course> leaves, int newMin,
			Course[] courses) throws Exception {

		// Check to see if solution is found, or we have already gone over
		// min
		Iterator iterator = dag.iterator();
		if (!iterator.hasNext() || newMin == min) {
			if (newMin < min) {
				min = newMin;
			}
			return;
		}

		List<Set<Course>> subsets = new ArrayList<>();

		// Adds check to see if total courses can take are less than max,
		// because then no set generation is necessary
		if (leaves.size() <= numSemester) {

			subsets.add(new HashSet<>(leaves));

		}

		// Generate subsets
		else {
			subsets = getSubsets(leaves, numSemester);
		}

		// For each subset in subsets
		for (Set<Course> courseSet : subsets) {
			
			//System.out.println(courseSet);
			//System.out.println(dag);

			List<Course> eDeleted = new ArrayList<>();
			List<Course> vDeleted = new ArrayList<>();
			moveForward(courseSet, dag, vDeleted, eDeleted);
			newMin++;

			if (term.equals("F")) {
				term = "S";
			} else {
				term = "F";
			}

			ArrayList<Course> newLeaves = availableCourses(dag, term);

			backTrackMin(term, dag, numSemester, newLeaves, newMin, courses);

			moveBackward(vDeleted, eDeleted, dag, courses);
			newMin--;
			
			if (term.equals("F")) {
				term = "S";
			} else {
				term = "F";
			}

		}

	}

	/**
	 * Move forward by deleting the appropriate courses from the dag as well as
	 * their edges, and store the deleted courses in an array, and the courses
	 * with edges to the deleted courses in a seperate array for help during
	 * moveBackward() method
	 * 
	 * @param subset
	 * @param dag
	 * @param vDeleted
	 * @param eDeleted
	 */

	private static void moveForward(Set<Course> subset,
			DirectedAcyclicGraph dag, List<Course> vDeleted,
			List<Course> eDeleted) {

		for (Course toDelete : subset) {
			Set<Course> toAdd = (Set<Course>) dag.outgoingEdgesOf(toDelete);
			for (Course c : toAdd) {
				Course course = (Course) dag.getEdgeTarget(c);
				if (!eDeleted.contains(course)) {
					eDeleted.add(course);
				}
			}
			vDeleted.add(toDelete);
			dag.removeVertex(toDelete);
		}

	}

	/**
	 * Step backward by readding the last deleted courses back to the dag,
	 * including any edges that were deleted
	 * 
	 * @param vDeleted
	 * @param eDeleted
	 * @param dag
	 * @param courses
	 * @throws Exception
	 */

	private static void moveBackward(List<Course> vDeleted,
			List<Course> eDeleted, DirectedAcyclicGraph dag, Course[] courses)
			throws Exception {

		Course[] vCourses = new Course[vDeleted.size()];
		vDeleted.toArray(vCourses);

		Course[] eCourses = new Course[eDeleted.size()];
		eDeleted.toArray(eCourses);

		for (Course course : vCourses) {
			dag.addVertex(course);
		}

		addEdges(eCourses, dag, courses);

	}

	/**
	 * Method to return all subsets of the leaves of size numSemester
	 * 
	 * @param leaves
	 * @param numSemester
	 * @return
	 */

	private static List<Set<Course>> getSubsets(List<Course> leaves,
			int numSemester) {

		List<Set<Course>> res = new ArrayList<>();
		getSubsets(leaves, numSemester, 0, new HashSet<Course>(), res);
		return res;

	}

	/**
	 * Method that recursively generates all subsets of the leaves of size
	 * numSemester
	 * 
	 * @param leaves
	 * @param numSemester
	 * @param idx
	 * @param current
	 * @param solution
	 */

	private static void getSubsets(List<Course> leaves, int numSemester,
			int idx, Set<Course> current, List<Set<Course>> solution) {

		// succesful stop clause
		if (current.size() == numSemester) {
			solution.add(new HashSet<>(current));
			return;
		}

		// unsuccesful stop clause
		if (idx == leaves.size()) {
			return;
		}

		Course x = leaves.get(idx);
		current.add(x);

		getSubsets(leaves, numSemester, idx + 1, current, solution);
		current.remove(x);

		getSubsets(leaves, numSemester, idx + 1, current, solution);

	}

	/**
	 * Finds the course object given a string name
	 * 
	 * @param name
	 * @param courses
	 * @return course that has given name
	 * @throws Exception
	 */

	public static Course findCourse(String name, Course[] courses)
			throws Exception {

		for (Course course : courses) {
			if (course.name.equals(name))
				return course;
		}

		System.out.println(name);
		throw new Exception("Name not found");
	}

	/**
	 * Adds edges from a course to its prereqs
	 * 
	 * @param courses
	 * @param dag
	 * @throws Exception
	 */

	public static void addEdges(Course[] courses, DirectedAcyclicGraph dag,
			Course[] toSearch) throws Exception {

		for (Course course : courses) {
			for (String prereq : course.prereqs) {
				Course begin = findCourse(prereq, toSearch);
				if (dag.containsVertex(begin)) {
					dag.addEdge(begin, course);
				}
			}
		}
	}

	/**
	 * Reads in the course information from the file and creates course objects
	 * 
	 * @param numCourses
	 * @param scanner
	 * @param dag
	 * @return
	 */

	public static Course[] createCourses(int numCourses, Scanner scanner,
			DirectedAcyclicGraph dag) {

		Course[] courses = new Course[numCourses];

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

		return courses;
	}

	/**
	 * Gets user input for file name, opens file, and creates a scanner
	 * 
	 * @return scanner for file
	 */
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

	/**
	 * Finds the courses with all completed prereqs by iterating through the
	 * graph and stopping when a vertex has an incoming edge
	 * 
	 * @param iterator
	 * @param term
	 * @param dag
	 * @return
	 */

	public static ArrayList<Course> availableCourses(DirectedAcyclicGraph dag,
			String term) {

		Iterator iterator = dag.iterator();
		ArrayList<Course> courses = new ArrayList<Course>();

		while (iterator.hasNext()) {

			Course toAdd = (Course) iterator.next();
			if (dag.incomingEdgesOf(toAdd).isEmpty()) {
				if ((toAdd.semester.equals("B") || toAdd.semester.equals(term))) {
					courses.add(toAdd);
				}
			}
		}

		return courses;
	}

}
