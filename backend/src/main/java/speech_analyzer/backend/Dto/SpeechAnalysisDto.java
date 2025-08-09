package speech_analyzer.backend.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpeechAnalysisDto {
    private ConfidenceAnalysis confidence_analysis;
    private List<String> improvement_suggestions;
    private List<BetterWordSentence> better_words_sentences;
    private int overall_rating;

    // inner classes
    @Getter
    @Setter
    public static class ConfidenceAnalysis {
        private int score;
        private String feedback;
        // getters & setters
    }

    @Getter
    @Setter
    public static class BetterWordSentence {
        private String original;
        private String suggestion;
        // getters & setters
    }
}
