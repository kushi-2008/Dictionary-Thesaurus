package com.lexicon.tokenizer;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UnicodeTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String text) {
        List<String> result = new ArrayList<>();
        BreakIterator it = BreakIterator.getWordInstance(Locale.ROOT);
        it.setText(text);
        int start = it.first();
        for (int end = it.next(); end != BreakIterator.DONE; start = end, end = it.next()) {
            String w = text.substring(start, end).trim();
            if (w.matches("\\p{L}[\\p{L}\\p{Nd}]*")) {
                result.add(w);
            }
        }
        return result;
    }
}