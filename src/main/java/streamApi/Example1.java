package streamApi;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Example1 {


    public static void main(String[] args) {

        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(x -> x % 2 == 0)
                .map(x -> {
                    String s = "a";
                    for (int i = 0; i < x; i++) {
                        s += s;
                    }
                    return s;
                })
                .forEach(x -> System.out.print(x + " "));

        Consumer<String> consumer = System.out::println;
        Predicate<Integer> predicate = x -> x > 10;
        Function<String, Integer> function = String::length;
        Function<String, Integer> bigFunc = x -> {
            int length = x.length();
            length += 2;
            return length;
        };
        System.out.println();
        System.out.println(function.andThen(x -> x + 2).apply("123"));
        System.out.println(function.compose(x -> "1223").apply(123));
        Supplier<HashMap<String, Integer>> supplier = HashMap::new;

        Consumer<String> c1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s + "!");
            }
        };

        Consumer<String> c2 = s -> System.out.println(s + "!");

        // filter
        // map
        // sorted, distinct
        // collect
    }
}
