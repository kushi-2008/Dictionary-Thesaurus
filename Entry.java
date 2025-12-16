package com.lexicon.core;

import java.util.ArrayList;
import java.util.List;

public class Entry {
    private final String word;
    private int frequency;
    private final List<String> samples = new ArrayList<>();

    public Entry(String word) {
        this.word = word;
    }

    public String getWord() { return word; }

    public int getFrequency() { return frequency; }

    public void incrementFrequency() { this.frequency++; }

    public List<String> getSamples() { return samples; }

    public void addSample(String line) {
        if (samples.size() < 5) {
            samples.add(line);
        }
    }

    @Override
    public String toString() {
        return word + " (" + frequency + ") " + samples;
    }
}