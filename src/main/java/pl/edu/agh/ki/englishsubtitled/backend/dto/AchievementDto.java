package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.Objects;

public class AchievementDto {
    String achievement;
    long timestamp;

    public AchievementDto(String achievement, long timestamp){
        this.achievement = achievement;
        this.timestamp = timestamp;
    }

    public String getAchievement() {
        return achievement;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementDto that = (AchievementDto) o;
        return timestamp == that.timestamp &&
                Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievement, timestamp);
    }
}
