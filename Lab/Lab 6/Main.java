package lab6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args){
        check1();
        check2();
    }

    private static void check1() {
        File zipFile = Path.of("src", "main", "java", "lab6", "src.zip").toFile();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            List<String> fileNames = new ArrayList<>();

            while (true) {
                ZipEntry ze = zis.getNextEntry();
                if (ze == null) {
                    break;
                }
                fileNames.add(ze.toString());
            }

            long count = fileNames.stream()
                    .filter(e -> e.startsWith("java/io") || e.startsWith("java/nio"))
                    .count();

            System.out.printf("# of .java files in java.io/java.nio: %d%n", count);
            fileNames.stream()
                    .filter(e -> e.startsWith("java/io") || e.startsWith("java/nio"))
                    .sorted().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void check2(){
        File jarFile = Path.of("src", "main", "java", "lab6", "rt.jar").toFile();
        try(JarFile jf = new JarFile(jarFile)){
            Enumeration<JarEntry> e = jf.entries();
            List<JarEntry> list = Collections.list(e);

            long count = list.stream()
                    .map(JarEntry::toString)
                    .filter(s -> s.startsWith("java/io") || s.startsWith("java/nio"))
                    .count();
            System.out.printf("# of .class files in java.io/java.nio: %d%n", count);

            list.stream()
                    .map(JarEntry::toString)
                    .filter(s -> s.startsWith("java/io") || s.startsWith("java/nio"))
                    .sorted().forEach(System.out::println);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
