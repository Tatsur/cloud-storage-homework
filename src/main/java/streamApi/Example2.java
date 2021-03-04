package streamApi;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class Example2 {

    public static void main(String[] args) {

        Integer result = Stream.of(1, 2, 3, 4, 5)
                .reduce(0, Integer::sum);
        Integer multiply = Stream.of(1, 2, 3, 4, 5)
                .reduce(1, (a, b) -> a * b);

        HashMap<Integer, Integer> reduce = Stream.of(1, 2, 3, 4, 5)
                .reduce(new HashMap<>(),
                        (map, integer) -> {
                            map.put(integer, integer);
                            return map;
                        },
                        (m1, m2) -> {
                            m1.putAll(m2);
                            return m1;
                        });

        Optional<Integer> any = Stream.of(1, 2, 3, 4, 5,7).filter(x -> x > 5)
                .findAny();

        Integer orElse = any.orElseThrow(() -> new IllegalArgumentException("12424124"));
        System.out.println(orElse);
        System.out.println(reduce);
        System.out.println(result);
        System.out.println(multiply);
    }
}
