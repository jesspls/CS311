
public class Course {
	
	String name;
	int numPreReqs;
	String semester;
	String[] prereqs;
	
	public Course(){

	}
	
	public Course(String name, int numPreReqs, String semester, String[] prereqs){
		this.name = name;
		this.numPreReqs = numPreReqs;
		this.semester = semester;
		this.prereqs = prereqs;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getNumPreReqs(){
		return this.numPreReqs;
	}
	
	public String getSemester(){
		return this.semester;
	}
	
	public String[] getPreReqs(){
		return this.prereqs;
	}
	
	@Override
	public String toString(){
		return name;
	}

	
}
