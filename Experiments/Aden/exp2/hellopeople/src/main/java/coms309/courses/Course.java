package coms309.courses;

/**
 * Provides the Definition/Structure for the class row
 *
 * @author Aden Koziol
 */

public class Course
{
    private String courseName;
    private String professorName;
    private String grade;

    public Course() {}

    public Course(String courseName, String professorName, String grade)
    {
        this.courseName = courseName;
        this.professorName = professorName;
        this.grade = grade;
    }

    public String getCourseName() { return this.courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getProfessorName() { return this.professorName; }
    public  void setProfessorName(String professorName) { this.professorName = professorName; }

    public String getGrade() { return this.grade; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return courseName + " "
                + professorName + " "
                + grade;
    }
}
