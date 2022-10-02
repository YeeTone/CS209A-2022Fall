import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class MyCSVReader {
    private final String PATH;

    private static final int NONE_SENSE = -1;

    public MyCSVReader(String path) {
        PATH = path;
    }

    public List<Movie> readData() {
        assert PATH != null;
        List<Movie> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH, StandardCharsets.UTF_8))) {
            String noneSense = br.readLine();

            String x;
            while ((x = br.readLine()) != null) {
                try {
                    String[] line = mySplit(x);

                    /*if (Arrays.stream(line).anyMatch(Objects::isNull) || Arrays.stream(line).anyMatch(String::isEmpty)) {
                        throw new DataTransformException();
                    }*/

                    String postLink = line[0].substring(1, line[0].length() - 1);
                    String seriesTitle = line[1].replace("\"", "");
                    int releaseYear = Integer.parseInt(line[2]);
                    String certificate = line[3];
                    int runTime = Integer.parseInt(line[4].replace("min", "").trim());
                    List<String> genre = Arrays.stream(line[5].replace("\"", "").split(","))
                            .map(String::trim).collect(Collectors.toList());
                    float imdb = Float.parseFloat(line[6]);
                    String overview = line[7].startsWith("\"") && line[7].endsWith("\"") ?
                            line[7].substring(1, line[7].length() - 1) :
                            line[7];
                    int metaScore = line[8].isEmpty() ? NONE_SENSE : Integer.parseInt(line[8]);
                    String director = line[9];
                    String star1 = line[10], star2 = line[11], star3 = line[12], star4 = line[13];
                    int noofvotes = Integer.parseInt(line[14]);
                    int gross = line[15].isEmpty() ? NONE_SENSE : Integer.parseInt(String.join("", line[15].substring(1, line[15].length() - 1).split(",")));

                    Movie m = new Movie(postLink, seriesTitle, releaseYear, certificate, runTime, genre
                            , imdb, overview, metaScore, director, star1, star2, star3, star4, noofvotes, gross);
                    data.add(m);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private static String[] mySplit(String line) {
        // src: https://qa.1r1g.com/sf/ask/1118578191/

        int startIndex = 0;

        List<String> temp = new ArrayList<>();
        boolean notInside = true;
        for (int currentIndex = 0; currentIndex < line.length() - 1; currentIndex++) {
            if (line.charAt(currentIndex) == ',' && notInside) {
                temp.add(line.substring(startIndex, currentIndex));
                startIndex = currentIndex + 1;
            } else if (line.charAt(currentIndex) == '\"') {
                notInside = !notInside;
            }
        }

        if (line.endsWith(",")) {
            temp.add(line.substring(startIndex, line.length() - 1));
            temp.add("");
        } else {
            temp.add(line.substring(startIndex));
        }

        return temp.toArray(new String[0]);
    }
}