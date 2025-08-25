package speech_analyzer.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import speech_analyzer.backend.Dto.SpeechAnalysisDto;

@Service
public class AudioAnalyzerService {

    public SpeechAnalysisDto generateResponse(String transcript) {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        Client client = new Client.Builder()
                .apiKey(apiKey)
                .build();
        String prompt =
                "You are an AI speech analysis expert. \n" +
                        "You will receive a transcript of a spoken speech. \n" +
                        "Analyze it strictly and return ONLY a JSON object in the exact structure below.\n\n" +
                        "JSON structure:\n" +
                        "{\n" +
                        "  \"confidence_analysis\": {\n" +
                        "    \"score\": 0,\n" +
                        "    \"feedback\": \"\"\n" +
                        "  },\n" +
                        "  \"improvement_suggestions\": [\n" +
                        "    \"\"\n" +
                        "  ],\n" +
                        "  \"better_words_sentences\": [\n" +
                        "    {\n" +
                        "      \"original\": \"\",\n" +
                        "      \"suggestion\": \"\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"overall_rating\": 0\n" +
                        "}\n\n" +
                        "Instructions:\n" +
                        "- \"confidence_analysis.score\" should be an integer from 1-10 based on tone and assertiveness in the transcript.\n" +
                        "- \"confidence_analysis.feedback\" should be 1-3 sentences describing confidence level and delivery style.\n" +
                        "- \"improvement_suggestions\" should be 3-5 short actionable tips to improve delivery, pacing, or engagement.\n" +
                        "- \"better_words_sentences\" should replace weak words or awkward phrases with stronger, more concise versions.\n" +
                        "- \"overall_rating\" is an integer 1-10 reflecting the speech’s overall quality.\n" +
                        "- Do not include any text outside the JSON object.\n\n" +
                        "Here is the transcript: " + transcript;

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null);
        String rawJson = response.text();
        String cleanedJson = rawJson
                .replaceAll("^```[a-zA-Z]*", "")
                .replaceAll("```$", "")
                .trim();
        ObjectMapper mapper = new ObjectMapper();
        try {
            SpeechAnalysisDto dto = mapper.readValue(cleanedJson, SpeechAnalysisDto.class);
            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

