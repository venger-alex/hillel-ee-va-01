package hillelee.reflection;

/**
 * Created by User on 28.10.2017.
 */
public class Puzzle {
    @CorrectAnswer
    public String trickySolution() {
        return "Correct answer";
    }

    public String simpleSolution() {
        return "Wrong answer";
    }
}
