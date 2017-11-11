package hillelee.apple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by User on 28.10.2017.
 */
public class AppleSelectorTest {
    List<Apple> apples = ImmutableList.of(new Apple("RED", 100),
                                          new Apple("RED", 120),
                                          new Apple("RED", 110),
                                          new Apple("GREEN", 130),
                                          new Apple("GREEN", 101),
                                          new Apple("RED", 150));

    @Test
    public void selectHeaviest() throws Exception {
        List<Apple> apples = ImmutableList.of(new Apple("RED", 100),
                new Apple("RED", 120),
                new Apple("RED", 110),
                new Apple("GREEN", 130),
                new Apple("GREEN", 100),
                new Apple("RED", 150));

        Optional<Apple> mayBeHeaviest = AppleSelector.getHeaviest(apples);

        if (mayBeHeaviest.isPresent()) {
            Apple heaviest = mayBeHeaviest.get();
            assertThat(heaviest.getWeight(), is(150));
        } else {
            fail();
        }


    }

    @Test
    public void selectHeaviestFromEmptyList() throws Exception {
        List<Apple> apples = ImmutableList.of();

        Optional<Apple> mayBeApple = AppleSelector.getHeaviest(apples);
        if (mayBeApple.isPresent()) {
            fail();
        }
    }

    @Test
    public void sort() throws Exception {
        List<Apple> apples = ImmutableList.of(new Apple("RED", 100),
                new Apple("RED", 120),
                new Apple("RED", 110),
                new Apple("GREEN", 130),
                new Apple("GREEN", 100),
                new Apple("RED", 150));
        apples = new ArrayList<>(apples);

        apples.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        System.out.println(apples);
    }

    @Test
    public void mapDefault() throws Exception {
        Map<String, Integer> nameToSalary = ImmutableMap.of("Ivan", 200);

        Integer salary = nameToSalary.getOrDefault("Stepan", 0);

    }

    @Test
    public void filterByPredicate() throws Exception {
        List<Apple> apples = ImmutableList.of(new Apple("RED", 100),
                new Apple("RED", 120),
                new Apple("RED", 110),
                new Apple("GREEN", 130),
                new Apple("GREEN", 100),
                new Apple("RED", 150));

        List<Apple> filtered = AppleSelector.filter(apples, new ColorPredicate());

        assertThat(filtered, hasSize(2));
    }

    @Test
    public void filterByAnanymousPredicate() throws Exception {
        List<Apple> apples = ImmutableList.of(new Apple("RED", 100),
                new Apple("RED", 120),
                new Apple("RED", 110),
                new Apple("GREEN", 130),
                new Apple("GREEN", 100),
                new Apple("RED", 150));

        // List<Apple> filtered = AppleSelector.filter(apples, apple -> apple.getWeight() > 120);

        Predicate<Apple> heavierThan120 = apple -> apple.getWeight() > 120;
        Predicate<Apple> isRed = apple -> apple.getColor().equals("RED");
        Predicate<Apple> heavyAndRed = isRed.and(heavierThan120);

/*        heavyAndRed = ((Predicate<Apple>) (apple -> apple.getWeight() > 120))
                .and(apple -> apple.getColor().equals("RED"));*/

        List<Apple> filtered = apples.stream()
                                     .filter(heavyAndRed)
                                     .collect(toList());

        assertThat(filtered, hasSize(1));
    }

    @Test
    public void mapToColor() throws Exception {
        List<String> colors = apples.stream()
                                    .map(Apple::getColor)
                                    .collect(toList());

        assertThat(colors, hasSize(6));
        assertThat(colors.get(0), is("RED"));
    }

    @Test
    public void printApples() throws Exception {
        apples.stream()
              .forEach(System.out::println);
    }

    @Test
    public void adjustColor() throws Exception {
        ColorAdjuster colorAdjuster = new ColorAdjuster();

        Function<String, String> adjust = colorAdjuster::adjust;

        BiFunction<ColorAdjuster, String , String > adjustWithAdjuster = ColorAdjuster::adjust;

        apples.stream()
              .map(Apple::getColor)
              .map(colorAdjuster::adjust)
              .forEach(System.out::println);
    }

    @Test
    public void executionFlow() throws Exception {
        List<String> collect = apples.stream()
                                     .filter(apple -> apple.getWeight() > 59)
                                     .map(Apple::getColor)
                                     .peek(System.out::println)
                                     .limit(3)
                                     .collect(toList());
    }

    @Test
    public void findFirst() throws Exception {
        apples.stream()
              .filter(apple -> apple.getWeight() > 200)
              .findFirst()
              .map(Apple::getColor)
              .ifPresent(System.out::println);
    }

    @Test
    public void intStream() throws Exception {
       IntSummaryStatistics longSummaryStatistics = apples.stream()
                                                          .map(Apple::getWeight)
                                                          .mapToInt(Integer::intValue)
                                                          .summaryStatistics();
        System.out.println(longSummaryStatistics);
    }

    @Test
    public void name() throws Exception {
        Map<Integer, Apple> weightToApple = apples.stream()
                                                  .collect(toMap(Apple::getWeight, identity()));

       assertThat( weightToApple.get(100), is(apples.get(0)));
    }

    @Test
    public void groupingBy() throws Exception {
        Map<String, List<Apple>> collect = apples.stream()
                                                 .collect(Collectors.groupingBy(Apple::getColor, Collectors.toList()));

        System.out.println(collect.get("RED"));
    }

    @Test
    public void getAllWorms() throws Exception {
        apples.stream()
              .flatMap(apple -> apple.getWorms().stream())

         /*   .map(Apple::getWorms)
              .flatMap(List::stream)*/

              .distinct()
              .forEach(System.out::println);
    }
}