import java.util.ArrayList;
import java.util.List;

public class CourseAssignment {
    private Course course;
    private List<Student> studentList;

    public CourseAssignment(Course course, List<Student> studentList) {
        this.course = course;
        this.studentList = studentList;
    }

    public CourseAssignment(Course course) {
        this.course = course;
        this.studentList = new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
