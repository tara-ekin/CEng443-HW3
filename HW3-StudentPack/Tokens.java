import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokens {
    private List<Integer> tokenList;

    public Tokens(String tokenList) {
        this.tokenList = Arrays.stream(tokenList.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public List<Integer> getTokenList() {
        return tokenList;
    }
}
