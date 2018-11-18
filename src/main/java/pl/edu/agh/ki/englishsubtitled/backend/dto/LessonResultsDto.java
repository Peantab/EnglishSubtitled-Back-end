package pl.edu.agh.ki.englishsubtitled.backend.dto;

public class LessonResultsDto {
    public LessonResultsDto() {
    } // For Jackson

    // number of mistakes in entire lesson
    public int mistakes = 0;

    // max number of correct answers in a row
    public int correctAnswersInRow = 0;

    // how many times correct answer occured as first
    public int correctAnswersAsFirst = 0;

    // number of dictionary additions
    public int dictionaryAdditions = 0;

    // number of games of all types
    public int crosswordGames = 0;
    public int abcdGames = 0;
    public int wordGames = 0;
}
