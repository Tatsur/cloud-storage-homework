package nio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class FilesExamples {

    public static void main(String[] args) throws IOException {

        StandardCopyOption co;
        StandardOpenOption oo;
        StandardCharsets sc;
        // CREATE - пересоздание
        // APPEND - дописывание

        Files.write(Paths.get("client/2.txt"),
                "Hello world!".getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);

        Files.copy(
                Paths.get("client/2.txt"),
                Paths.get("client/4.txt"),
                StandardCopyOption.REPLACE_EXISTING);

        Files.walkFileTree(Paths.get("./"), new HashSet<>(),1,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        System.out.println(file);
                        return super.visitFile(file, attrs);
                    }
                });
    }
}
