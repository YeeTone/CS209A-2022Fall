import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked"})
public class MovieAnalyzerLocalTest {
    private static Class<?> movieAnalyzerClass;
    private static Object imdbTop;

    @BeforeAll
    static void setUp() {
        try {
            movieAnalyzerClass = Class.forName("MovieAnalyzer");
            //checkDeclarations();
            Constructor<?> constructor = movieAnalyzerClass.getDeclaredConstructor(String.class);
            if (constructor.getModifiers() != Modifier.PUBLIC) {
                throw new NoSuchMethodException("The constructor from class MovieAnalyzer is not public!");
            }
            imdbTop = constructor.newInstance("resources/imdb_top_500.csv");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            fail();
        }
    }

    static void checkDeclarations() {
        MethodEntity[] movieAnalyzerMethods = {
                new MethodEntity("getMovieCountByYear", Map.class),
                new MethodEntity("getMovieCountByGenre", Map.class),
                new MethodEntity("getCoStarCount", Map.class),
                new MethodEntity("getTopMovies", List.class, int.class, String.class),
                new MethodEntity("getTopStars", List.class, int.class, String.class),
                new MethodEntity("searchMovies", List.class, String.class, float.class, int.class)
        };
        List<String> errorMessages = new ArrayList<>();
        for (MethodEntity m : movieAnalyzerMethods) {
            if (!m.checkForClass(movieAnalyzerClass)) {
                errorMessages.add("The method [" + m + "] from class MovieAnalyzer does not exist!");
            }
        }
        assertTrue(errorMessages.isEmpty(), String.join(System.lineSeparator(), errorMessages));
    }

    <K, V> String mapToString(Object obj) {
        Map<K, V> map = (Map<K, V>) obj;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" == ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        if (sb.length() == 0) return "";
        return sb.substring(0, sb.length() - 1).strip();
    }

    Map<List<String>, Integer> stringToMap(String str) {
        Map<List<String>, Integer> map = new HashMap<>();
        String[] rows = str.split("\n");
        for (String row : rows) {
            String[] strings = row.split(" == ");
            String listStr = strings[0].substring(1, strings[0].length() - 1);
            String[] split = listStr.split(", ");
            List<String> list = new ArrayList<>(List.of(split));
            map.put(list, Integer.parseInt(strings[1]));
        }
        return map;
    }

    String listToString(Object obj) {
        List<String> list = (List<String>) obj;
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append("\n");
        }
        if (sb.length() == 0) return "";
        return sb.substring(0, sb.length() - 1).strip();
    }

    <K, V> boolean compareMapWithoutOrder(Object obj, String expected, int question) {
        Map<K, V> objMap = (Map<K, V>) obj;
        List<Item<K, V>> expectedList = new ArrayList<>();
        String[] rows = expected.split("\n");
        for (String row : rows) {
            String[] strings = row.split(" == ");
            switch (question) {
                case 1 -> {
                    int key1 = Integer.parseInt(strings[0]);
                    int value1 = Integer.parseInt(strings[1]);
                    expectedList.add((Item<K, V>) new Item<>(key1, value1));
                }
                case 2 -> {
                    String key2 = strings[0];
                    Integer value2 = Integer.parseInt(strings[1]);
                    expectedList.add((Item<K, V>) new Item<>(key2, value2));
                }
            }
        }
        if (objMap.size() != expectedList.size()) {
            return false;
        }
        for (Map.Entry<K, V> entry : objMap.entrySet()) {
            Item<K, V> item = new Item<>(entry.getKey(), entry.getValue());
            if (!expectedList.contains(item)) {
                return false;
            }
        }
        return true;
    }

    boolean compareListWithoutOrder(Object obj, String expected) {
        List<String> objList = (List<String>) obj;
        String[] strings = expected.split("\n");
        List<String> expectedList = List.of(strings);
        if (objList.size() != expectedList.size()) {
            return false;
        }
        return expectedList.containsAll(objList);
    }

    @Test
    void testGetMovieCountByYearWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getMovieCountByYear");
            Object res = method.invoke(imdbTop);
            assertTrue(res instanceof Map<?, ?>);
            String expected = Files.readString(Paths.get("resources", "answers_local", "Q1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareMapWithoutOrder(res, expected, 1));
        } catch (NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetMovieCountByYearWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getMovieCountByYear");
            Object res = method.invoke(imdbTop);
            assertTrue(res instanceof Map<?, ?>);
            String expected = Files.readString(Paths.get("resources", "answers_local", "Q1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected, mapToString(res));
        } catch (NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetMovieCountByGenreWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getMovieCountByGenre");
            Object res = method.invoke(imdbTop);
            assertTrue(res instanceof Map<?, ?>);
            String expected = Files.readString(Paths.get("resources", "answers_local", "Q2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareMapWithoutOrder(res, expected, 2));
        } catch (NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetMovieCountByGenreWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getMovieCountByGenre");
            Object res = method.invoke(imdbTop);
            assertTrue(res instanceof Map<?, ?>);
            String expected = Files.readString(Paths.get("resources", "answers_local", "Q2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected, mapToString(res));
        } catch (NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetCoStarCount() {
        try {
            Method method = movieAnalyzerClass.getMethod("getCoStarCount");
            Object res = method.invoke(imdbTop);
            assertTrue(res instanceof Map<?, ?>);
            Map<List<String>, Integer> resMap = (Map<List<String>, Integer>) res;
            String expected = Files.readString(Paths.get("resources", "answers_local", "Q3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            Map<List<String>, Integer> expectedMap = stringToMap(expected);
            assertEquals(expectedMap.size(), resMap.size());
            for (Map.Entry<List<String>, Integer> entry : resMap.entrySet()) {
                Integer integer = expectedMap.get(entry.getKey());
                assertEquals(integer, entry.getValue());
            }
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopMoviesRuntimeWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopMovies", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 20, "runtime");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q4_1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res1, expected1));
            Object res2 = method.invoke(imdbTop, 50, "runtime");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q4_2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res2, expected2));
            Object res3 = method.invoke(imdbTop, 100, "runtime");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q4_3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res3, expected3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopMoviesOverviewWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopMovies", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 20, "overview");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q4_4.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res1, expected1));
            Object res2 = method.invoke(imdbTop, 50, "overview");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q4_5.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res2, expected2));
            Object res3 = method.invoke(imdbTop, 100, "overview");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q4_6.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res3, expected3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopMoviesRuntimeWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopMovies", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 20, "runtime");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q4_1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected1, listToString(res1));
            Object res2 = method.invoke(imdbTop, 50, "runtime");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q4_2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected2, listToString(res2));
            Object res3 = method.invoke(imdbTop, 100, "runtime");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q4_3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected3, listToString(res3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopMoviesOverviewWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopMovies", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 20, "overview");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q4_4.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected1, listToString(res1));
            Object res2 = method.invoke(imdbTop, 50, "overview");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q4_5.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected2, listToString(res2));
            Object res3 = method.invoke(imdbTop, 100, "overview");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q4_6.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected3, listToString(res3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopStarsRatingWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopStars", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 15, "rating");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q5_1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res1, expected1));
            Object res2 = method.invoke(imdbTop, 40, "rating");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q5_2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res2, expected2));
            Object res3 = method.invoke(imdbTop, 80, "rating");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q5_3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res3, expected3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopStarsGrossWithoutOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopStars", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 15, "gross");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q5_4.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res1, expected1));
            Object res2 = method.invoke(imdbTop, 40, "gross");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q5_5.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res2, expected2));
            Object res3 = method.invoke(imdbTop, 80, "gross");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q5_6.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res3, expected3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopStarsRatingWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopStars", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 15, "rating");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q5_1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected1, listToString(res1));
            Object res2 = method.invoke(imdbTop, 40, "rating");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q5_2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected2, listToString(res2));
            Object res3 = method.invoke(imdbTop, 80, "rating");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q5_3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected3, listToString(res3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTopStarsGrossWithOrder() {
        try {
            Method method = movieAnalyzerClass.getMethod("getTopStars", int.class, String.class);
            Object res1 = method.invoke(imdbTop, 15, "gross");
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q5_4.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected1, listToString(res1));
            Object res2 = method.invoke(imdbTop, 40, "gross");
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q5_5.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected2, listToString(res2));
            Object res3 = method.invoke(imdbTop, 80, "gross");
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q5_6.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected3, listToString(res3));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void searchMoviesTest() {
        try {
            Method method = movieAnalyzerClass.getMethod("searchMovies", String.class, float.class, int.class);
            Object res1 = method.invoke(imdbTop, "Drama", 9.0f, 150);
            assertTrue(res1 instanceof List<?>);
            String expected1 = Files.readString(Paths.get("resources", "answers_local", "Q6_1.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res1, expected1));
            assertEquals(expected1, listToString(res1));
            Object res2 = method.invoke(imdbTop, "Adventure", 8.0f, 150);
            assertTrue(res2 instanceof List<?>);
            String expected2 = Files.readString(Paths.get("resources", "answers_local", "Q6_2.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res2, expected2));
            assertEquals(expected2, listToString(res2));
            Object res3 = method.invoke(imdbTop, "Sci-Fi", 8.2f, 200);
            assertTrue(res3 instanceof List<?>);
            String expected3 = Files.readString(Paths.get("resources", "answers_local", "Q6_3.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res3, expected3));
            assertEquals(expected3, listToString(res3));
            Object res4 = method.invoke(imdbTop, "Adventure", 8.6f, 120);
            assertTrue(res4 instanceof List<?>);
            String expected4 = Files.readString(Paths.get("resources", "answers_local", "Q6_4.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertEquals(expected4, listToString(res4));
            Object res5 = method.invoke(imdbTop, "Action", 7.7f, 200);
            assertTrue(res5 instanceof List<?>);
            String expected5 = Files.readString(Paths.get("resources", "answers_local", "Q6_5.txt"),
                            StandardCharsets.UTF_8)
                    .replace("\r", "").strip();
            assertTrue(compareListWithoutOrder(res5, expected5));
            assertEquals(expected5, listToString(res5));
        } catch (InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
