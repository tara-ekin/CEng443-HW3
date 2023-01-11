import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Student {
    private String id;
    private List<Integer> tokens;

    public Student(String id) {
        this.id = id;
        this.tokens = new ArrayList<>();
    }

    public void setTokens(String tokens) {
        this.tokens = Arrays.stream(tokens.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public void setTokens(List<Integer> tokens) {
        this.tokens = tokens;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getTokens() {
        return tokens;
    }
}
