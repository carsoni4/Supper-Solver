package coms309.assignments;

import coms309.people.Person;
import coms309.courses.Course;

/**
 * Provides the Definition/Structure for the assignment row
 *
 * @author Aden Koziol
 */

public class Assignment
{
    private String assignmentName;
    private Person student;
    private Course course;
    private char grade;

    public Assignment() {}

    public Assignment(String assignmentName, Person student, Course course, char grade)
    {
        this.assignmentName = assignmentName;
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }

    public Person getStudent() { return student; }
    public void setStudent(Person student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public char getGrade() { return grade; }
    public void setGrade(char grade) { this.grade = grade; }

    @Override
    public String toString() {
        return  assignmentName + " "
                + student.getFirstName() + " "
                + course.getCourseName() + " "
                + grade;
    }
}