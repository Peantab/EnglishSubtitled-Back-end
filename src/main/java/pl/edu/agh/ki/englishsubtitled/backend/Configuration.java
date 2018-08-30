package pl.edu.agh.ki.englishsubtitled.backend;

public class Configuration {
    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    public int getRentedLessonsLimit(){
        return 3;
    }
}
