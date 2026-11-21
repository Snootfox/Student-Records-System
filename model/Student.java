package main.Model;

public class Student {
	private int studentID;
	private String studentName;
	private String yearLevel;
	private String program;
	private double gwa;
	
	public int getID() {
		return studentID;
	}
	
	public void setID(int id) {
		this.studentID = id;
	}
	
	public String getName() {
		return studentName;
	}
	
	public void setName(String name) {
		this.studentName = name;
	}
	
	public String getYear() {
		return yearLevel;
	}
	
	public void setYear(String year) {
		this.yearLevel = year;
	}
	
	public String getProgram() {
		return program;
	}
	
	public void setProgram(String program) {
		this.program = program;
	}
	
	public double getGrade() {
		return gwa;
	}
	
	public void setGrade(double grade) {
		this.gwa = grade;
	}
	
	public String toFile() {
		return studentID + "," + studentName + "," + yearLevel + "," + program + "," + gwa + "\n";
	}
	
	public static Student fromLine(String line) {
        String[] parts = line.strip().split(",");
        if (parts.length < 5) return null;
        try {
            Student s = new Student();
            s.studentID = Integer.parseInt(parts[0].trim());
            s.studentName = parts[1].trim();
            s.yearLevel = parts[2].trim();
            s.program = parts[3].trim();
            s.gwa = Double.parseDouble(parts[4].trim());
            return s;
        } catch (NumberFormatException e) {
            return null;
        }
    }
	
	public String getGradeRange() {
        if (gwa >= 1.00 && gwa < 1.50) return "1.00 - 1.50";
        if (gwa >= 1.50 && gwa < 2.00) return "1.50 - 2.00";
        if (gwa >= 2.00 && gwa < 2.50) return "2.00 - 2.50";
        if (gwa >= 2.50 && gwa <= 3.00) return "2.50 - 3.00";
        if (gwa > 3.00 && gwa <= 4.00) return "3.50 - 4.00";
        return "N/A";
    }
	
	public int compareTo(Student other) {
        return Double.compare(this.gwa, other.gwa);
    }
}
