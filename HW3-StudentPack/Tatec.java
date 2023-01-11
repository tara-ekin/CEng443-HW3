import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
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

            Iterator<Tokens> tokensIterator = tokens.iterator();
            students.forEach(student -> student.setTokens(tokensIterator.next().getTokenList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        courses.stream().forEach(c -> System.out.println(c.getName() + ": " + c.getCapacity()));
//        students.stream().forEach(s -> System.out.println(s.getId() + ": " + s.getTokens()));
//        tokens.stream().forEach(t -> System.out.println(t.getTokenList()));
    }

}