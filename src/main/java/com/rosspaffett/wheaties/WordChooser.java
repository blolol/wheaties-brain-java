package com.rosspaffett.wheaties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WordChooser {
    private static final List<String> BORING_WORDS = List.of(
        "a", "an", "and", "any", "as", "at", "be", "by", "can", "do", "for", "get", "go", "have", "i", "if", "in",
        "into", "is", "it", "its", "just", "like", "make", "me", "most", "my", "new", "no", "not", "now", "of", "on",
        "or", "other", "our", "out", "over", "so", "some", "than", "that", "the", "their", "them", "then", "there",
        "they", "this", "to", "us", "we", "well", "what", "when", "who", "will", "with", "would", "you", "your"
    );
    private static final String WORD_SPLIT_PATTERN = "\\s+";

    private final String botNick;
    private final Random random = new Random();

    public WordChooser(String botNick) {
        this.botNick = botNick;
    }

    public String interestingWordFrom(String sentence) {
        List<String> words = tokenize(sentence);
        List<String> interestingWords = new ArrayList<>();

        for (String word : words) {
            if (!word.equalsIgnoreCase(botNick)
                && !BORING_WORDS.contains(word.toLowerCase())
                && !word.equals(ConversationListener.URL_PLACEHOLDER)) {
                interestingWords.add(word);
            }
        }

        if (interestingWords.isEmpty()) return null;
        return interestingWords.get(random.nextInt(interestingWords.size()));
    }

    private List<String> tokenize(String sentence) {
        return Arrays.asList(sentence.split(WORD_SPLIT_PATTERN));
    }
}
