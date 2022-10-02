import java.util.List;

record Movie(String postLink, String seriesTitle, int releaseYear, String certificate,
             int runTime, List<String> genre, float IMDB_rating, String overview,
             int metaScore, String director, String star1, String star2,
             String star3, String star4, int noofvotes, int gross) {

}
