import java.util.*;
import java.util.stream.Collectors;

public class MovieAnalyzer {
    private final List<Movie> movies;

    public MovieAnalyzer(String dataset_path) {
        MyCSVReader reader = new MyCSVReader(dataset_path);
        movies = reader.readData();
    }

    public Map<Integer, Integer> getMovieCountByYear() {
        Map<Integer, Long> map = movies.stream().collect(Collectors.groupingBy(Movie::releaseYear, Collectors.counting()));
        Map<Integer, Integer> result = new TreeMap<>(Comparator.reverseOrder());
        map.forEach((key, value) -> result.put(key, Math.toIntExact(value)));
        return result;
    }

    public Map<String, Integer> getMovieCountByGenre() {

        Map<String, Integer> map = new HashMap<>();
        movies.forEach(m ->
                m.genre().forEach(g -> {
                    map.put(g, map.getOrDefault(g, 0) + 1);
                }));

        // reference: https://blog.csdn.net/qq_45055165/article/details/119282576
        return map.entrySet().stream().sorted(Collections.reverseOrder((o1, o2) -> {
            int compareByValue = o1.getValue().compareTo(o2.getValue());
            return compareByValue != 0 ? compareByValue : o1.getKey().compareTo(o2.getKey()) * -1;
        })).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new
        ));
    }

    public Map<List<String>, Integer> getCoStarCount() {
        Map<List<String>, Integer> map = new HashMap<>();
        movies.forEach(m -> {
            String[] stars = stars(m);
            for (int i = 0; i < stars.length; i++) {
                for (int j = i + 1; j < stars.length; j++) {
                    List<String> twoStars = Arrays.asList(stars[i], stars[j]);
                    twoStars.sort(Comparator.naturalOrder());
                    map.put(twoStars, map.getOrDefault(twoStars, 0) + 1);
                }
            }
        });

        // reference: https://blog.csdn.net/qq_45055165/article/details/119282576
        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldVal, newVal) -> oldVal,
                LinkedHashMap::new
        ));

    }

    public List<String> getTopMovies(int top_k, String by) {
        return movies.stream().sorted((o1, o2) -> {
            int compare = by.equals("runtime") ?
                    Integer.compare(o1.runTime(), o2.runTime()) * -1 :
                    Integer.compare(o1.overview().length(), o2.overview().length()) * -1;
            return compare != 0 ? compare : o1.seriesTitle().compareTo(o2.seriesTitle());
        }).map(Movie::seriesTitle).limit(top_k).collect(Collectors.toList());
    }

    public List<String> getTopStars(int top_k, String by) {
        Map<String, Double> sum = new HashMap<>(), times = new HashMap<>();

        movies.stream().filter(mv -> !by.equals("gross") || mv.gross() > 0).forEach(m -> {
            String[] stars = stars(m);
            Arrays.stream(stars).forEach(s -> {
                sum.put(s, sum.getOrDefault(s, 0d) + (by.equals("gross") ? m.gross() : m.IMDB_rating()));
                times.put(s, times.getOrDefault(s, 0d) + 1);
            });
        });

        Map<String, Double> average = new HashMap<>();
        sum.keySet().forEach(k -> average.put(k, sum.get(k) / times.get(k)));

        average.entrySet().stream().sorted(Collections.reverseOrder((o1, o2) -> {
            if (!Objects.equals(o1.getValue(), o2.getValue())) {
                return Double.compare(o1.getValue(), o2.getValue());
            } else {
                return o1.getKey().compareTo(o2.getKey()) * -1;
            }
        })).forEach(System.out::println);

        return average.entrySet().stream().sorted(Collections.reverseOrder((o1, o2) -> {
                    if (!Objects.equals(o1.getValue(), o2.getValue())) {
                        return Double.compare(o1.getValue(), o2.getValue());
                    } else {
                        return o1.getKey().compareTo(o2.getKey()) * -1;
                    }
                })).limit(top_k)
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private static String[] stars(Movie m) {
        return new String[]{m.star1(), m.star2(), m.star3(), m.star4()};
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        return movies.stream().filter(m ->
                        m.genre().contains(genre)
                                && m.IMDB_rating() >= min_rating
                                && m.runTime() <= max_runtime)
                .map(Movie::seriesTitle)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

}