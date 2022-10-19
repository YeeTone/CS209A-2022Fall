package tutorial.lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.stream.Stream;

public class FilesExample {

    public static void main(String[] args) throws IOException {

        System.out.println("==WORKING DIRECTORY==");
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);

        System.out.println("\n===CREATE PATH===");
        Path p1 = Paths.get("resources");
        //Path p2 = Paths.get(args[0]);
        Path p3 = Paths.get(URI.create("file:///D:/CS209A/sample123.txt"));

        System.out.println("\n===PATH SEQUENCE===");
        Path dir = Paths.get(workingDir);
        System.out.format("toString: %s%n", dir.toString());
        System.out.format("getFileName: %s%n", dir.getFileName());
        System.out.format("getName(0): %s%n", dir.getName(0));
        System.out.format("getNameCount: %d%n", dir.getNameCount());
        System.out.format("subpath(0,2): %s%n", dir.subpath(0,2));
        System.out.format("getParent: %s%n", dir.getParent());
        System.out.format("getRoot: %s%n", dir.getRoot());


        System.out.println("\n===DOT NOTATIONS===");
        Path rp1 = Paths.get(workingDir, "..", "..");
        Path rp2 = Paths.get(workingDir, ".", "..", ".");
        System.out.format("rp1 normalize: %s%n", rp1.normalize());
        System.out.format("rp2 normalize: %s%n", rp2.normalize());


        System.out.println("\n===CONVERTING PATH===");
        Path cp = Paths.get("src\\main\\resources\\..\\resources\\math.txt");
        // C:\Users\admin\CS209A_Lectures\resources\..\resources\math.txt
        System.out.println(cp.toAbsolutePath());
        // C:\Users\admin\CS209A_Lectures\resources\math.txt
        System.out.println(cp.toRealPath());

        Path cp2 = Paths.get("src\\main\\resources\\..\\resources\\notexist.txt");
        // C:\Users\admin\CS209A_Lectures\resources\..\resources\notexist.txt
        System.out.println(cp2.toAbsolutePath());
        // Throws NoSuchFileException
        try{
            System.out.println(cp2.toRealPath());
        } catch (NoSuchFileException e){
            System.out.println("NoSuchFileException");
        }

        System.out.println("\n===TRAVERSING DIRECTORY===");
        Path dir2 = Paths.get(workingDir);
        System.out.println("Not entering subdirectories");
        try(Stream<Path> entries = Files.list(dir2)){
            entries.forEach(p -> System.out.println(p.toAbsolutePath()));
        }

        System.out.println("Entering subdirectories");
        try(Stream<Path> entries = Files.walk(dir2)){
            entries.filter(Files::isRegularFile).forEach(System.out::println);
        }

        System.out.println("\n===FILE VISITOR===");
        Path path = Paths.get(".");
        Files.walkFileTree(path, new ListFileVisitor());

        System.out.println("\n===READING FILES LINE BY LINE===");
        Path file = Paths.get("src","main","resources","math.txt");
        System.out.println("Using Scanner:");
        Scanner in = new Scanner(file);
        while(in.hasNext()){
            System.out.println(in.nextLine());
        }

        System.out.println("Using Files.lines:");
        try (Stream<String> stream = Files.lines(file)) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Using BufferedReader:");
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class ListFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        System.out.println("Visiting file:" + file.toRealPath());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path directory, IOException e)
            throws IOException {
        System.out.println("Finished directory: "
                + directory.toRealPath());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path directory,
                                             BasicFileAttributes attributes) throws IOException {
        System.out.println("Start directory: "
                + directory.toRealPath());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc)
            throws IOException {
        System.out.println("An error occurred.");
        return FileVisitResult.SKIP_SUBTREE;
    }
}

