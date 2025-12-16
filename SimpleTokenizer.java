package com.lexicon.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        // Replace any punctuation with space, then split on whitespace
        // \p{Punct} = any punctuation character
        String cleaned = text.replaceAll("\\p{Punct}+", " ");
        String[] tokens = cleaned.trim().split("\\s+");

        return new ArrayList<>(Arrays.asList(tokens));
    }
}