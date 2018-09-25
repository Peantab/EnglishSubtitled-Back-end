package pl.edu.agh.ki.englishsubtitled.backend;

public class Configuration {
    private static int RENTED_LESSONS_LIMIT = 3;
    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    public int getRentedLessonsLimit(){
        return RENTED_LESSONS_LIMIT;
    }
}
