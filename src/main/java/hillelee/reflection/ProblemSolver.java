package hillelee.reflection;

import java.lang.reflect.Method;

import lombok.SneakyThrows;

/**
 * Created by User on 28.10.2017.
 */
public class ProblemSolver {
    @SneakyThrows
    public String solve(Object problem) {
        Class<?> aClass = problem.getClass();
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(CorrectAnswer.class)) {
                return (String) method.invoke(problem);
            }
        }
        throw new RuntimeException("There is no CorrectAnswer annotation");
    }
}
