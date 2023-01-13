import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Tatec
{
    private static final int CORRECT_TOTAL_TOKEN_PER_STUDENT = 100;
    private static final String OUT_TATEC_UNHAPPY = "unhappyOutTATEC.txt";
    private static final String OUT_TATEC_ADMISSION = "admissionOutTATEC.txt";
    private static final String OUT_RAND_UNHAPPY = "unhappyOutRANDOM.txt";
    private static final String OUT_RAND_ADMISSION = "admissionOutRANDOM.txt";

    public static void main(String args[])
    {
        if(args.length < 4)
        {
            System.err.println("Not enough arguments!");
            return;
        }

        // File Paths
        String courseFilePath = args[0];
        String studentIdFilePath = args[1];
        String tokenFilePath = args[2];
        double h;

        try { h = Double.parseDouble(args[3]);}
        catch (NumberFormatException ex)
        {
            System.err.println("4th argument is not a double!");
            return;
        }

        // TODO: Rest is up to you
        List<Course> courses;
        List<Student> students;
        List<Tokens> tokens;

        try {
            courses = Files.lines(Paths.get(courseFilePath))
                    .map(Course::new)
                    .collect(Collectors.toList());
            students = Files.lines(Paths.get(studentIdFilePath))
                    .map(Student::new)
                    .collect(Collectors.toList());
            tokens = Files.lines(Paths.get(tokenFilePath))
                    .map(Tokens::new)
                    .collect(Collectors.toList());

            if (tokens.stream()
                    .map(t -> t.getTokenList()
                            .stream()
                            .reduce(0, Integer::sum))
                    .anyMatch(tokenSum -> tokenSum != CORRECT_TOTAL_TOKEN_PER_STUDENT)) {
                System.err.println("At least one student used wrong number of tokens!");
                return;
            }

            Iterator<Tokens> tokensIterator = tokens.iterator();
            students.forEach(student -> student.setTokens(tokensIterator.next().getTokenList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        courses.stream().forEach(c -> System.out.println(c.getName() + ": " + c.getCapacity()));
//        students.stream().forEach(s -> System.out.println(s.getId() + ": " + s.getTokens()));
//        tokens.stream().forEach(t -> System.out.println(t.getTokenList()));

        // TATEC
        List<CourseAssignment> tatecCourseAssignmentList = new ArrayList<>();
        courses.forEach(c -> tatecCourseAssignmentList.add(new CourseAssignment(c)));

        IntStream.range(0, courses.size())
                .forEach(i -> tatecCourseAssignmentList.get(i)
                        .setStudentList(students.stream()
                                .filter(s -> s.getTokens().get(i) != 0)
                                .sorted((s1, s2) -> s2.getTokens().get(i) - s1.getTokens().get(i))
                                .limit(tatecCourseAssignmentList.get(i).getCourse().getCapacity())
                                .collect(Collectors.toList())));

//        for(CourseAssignment assignment : tatecCourseAssignmentList) {
//            System.out.println(assignment.getCourse().getName() + ":");
//            for (Student student : assignment.getStudentList()) {
//                System.out.println(student.getId() + ": " + student.getTokens());
//            }
//            System.out.println("-----");
//        }
        List<String> tatecAdmittedStudents = tatecCourseAssignmentList.stream()
                .map(courseAssignment -> courseAssignment.getCourse().getName()
                        + courseAssignment.getStudentList().stream()
                        .map(student -> String.valueOf(student.getId()))
                        .collect(Collectors.joining(", ", ", ", "")))
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(OUT_TATEC_ADMISSION), tatecAdmittedStudents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Random Assignment
        List<CourseAssignment> randomCourseAssignmentList = new ArrayList<>();
        courses.forEach(c -> randomCourseAssignmentList.add(new CourseAssignment(c)));

        IntStream.range(0, courses.size())
                .forEach(i -> randomCourseAssignmentList.get(i)
                        .setStudentList(students.stream()
                                .filter(s -> s.getTokens().get(i) != 0)
                                .collect(Collectors.toList())));

        randomCourseAssignmentList.forEach(a -> Collections.shuffle(a.getStudentList(), new Random()));

        randomCourseAssignmentList.forEach(a -> a.setStudentList(a.getStudentList()
                .stream()
                .limit(a.getCourse().getCapacity())
                .collect(Collectors.toList())));

//        for(CourseAssignment assignment : randomCourseAssignmentList) {
//            System.out.println(assignment.getCourse().getName() + ":");
//            for (Student student : assignment.getStudentList()) {
//                System.out.println(student.getId() + ": " + student.getTokens());
//            }
//            System.out.println("-----");
//        }
        List<String> randomlyAdmittedStudents = randomCourseAssignmentList.stream()
                .map(courseAssignment -> courseAssignment.getCourse().getName()
                        + courseAssignment.getStudentList().stream()
                        .map(student -> String.valueOf(student.getId()))
                        .collect(Collectors.joining(", ", ", ", "")))
                .collect(Collectors.toList());
        try {
            Files.write(Paths.get(OUT_RAND_ADMISSION), randomlyAdmittedStudents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(averageUnhappiness(students, tatecCourseAssignmentList, h));
//        System.out.println(averageUnhappiness(students, randomCourseAssignmentList, h));
    }

    public static Double averageUnhappiness(List<Student> studentList, List<CourseAssignment> courseAssignmentList, Double h) {
        return studentList.stream()
                .mapToDouble(s -> unhappiness(s, courseAssignmentList, h))
                .average()
                .orElse(0.0);
    }

    public static Double unhappiness(Student student, List<CourseAssignment> courseAssignmentList, Double h) {
        return IntStream.range(0, courseAssignmentList.size())
                .asDoubleStream()
                .map(j -> x(student, courseAssignmentList.get((int)Math.round(j)))
                        * (-100.0 * Math.log(1.0-(student.getTokens().get((int)Math.round(j))/100.0)) / h))
                .sum();
    }

    public static Integer x(Student student, CourseAssignment courseAssignment) {
        if (courseAssignment.getStudentList().contains(student)) {
            return 0;
        } else {
            return 1;
        }
    }
}
