package streamApi;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IoUtils {

    public static void main(String[] args) throws Exception {

        IoUtils utils = new IoUtils();
//        HashMap<String, String> map = new IoUtils().readAsMap("data.kv");
//        map.get("banana");
//        map.get("банан");
//        System.out.println(
//                new IoUtils().readAsMap("data.kv"));
        System.out.println(utils.getCountMap("text.txt"));

        System.out.println(utils.getWordsString("text.txt"));

        utils.getCountMap("text.txt").entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

        System.out.println(utils.getFiles(Paths.get(IoUtils.class.getResource("").toURI())));
    }

    public HashMap<String, String> readAsMap(String path) throws Exception {
        // str - str
        HashMap<String, String> map = Files.lines(Paths.get(getClass().getResource(path).toURI()))
                .map(str -> str.split(" - "))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr[1],
                        (oldVal, val) -> val,
                        HashMap::new
                ));
        HashMap<String, String> tmp = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            tmp.put(entry.getValue(), entry.getKey());
        }
        map.putAll(tmp);
        return map;
    }

    public Set<String> getWords(String path) throws Exception {
        return Files.lines(Paths.get(getClass().getResource(path).toURI()))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .filter(str -> !str.isEmpty())
                .map(str -> str.toLowerCase().replaceAll("\\W+", ""))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toSet());
    }

    public String getWordsString(String path) throws Exception {
        return Files.lines(Paths.get(getClass().getResource(path).toURI()))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .filter(str -> !str.isEmpty())
                .map(str -> str.toLowerCase().replaceAll("\\W+", ""))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining(", "));
    }

    public HashMap<String, Integer> getCountMap(String path) throws Exception {
        return Files.lines(Paths.get(getClass().getResource(path).toURI()))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .filter(str -> !str.isEmpty())
                .map(str -> str.toLowerCase().replaceAll("\\W+", ""))
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toMap(
                        Function.identity(),
                        word -> 1,
                        Integer::sum,
                        HashMap::new
                ));
    }

    public List<String> getFiles(Path path) throws Exception {
        return Files.list(path)
                .filter(p -> !Files.isDirectory(p))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }
}
