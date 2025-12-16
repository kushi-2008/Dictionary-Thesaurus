package com.lexicon.core;
import java.util.List;
import com.lexicon.tokenizer.SimpleTokenizer;
import com.lexicon.tokenizer.Tokenizer;
import com.lexicon.util.LoggerUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DictionaryStore extends LexiconStore {

    private final Map<String, Entry> entries = new LinkedHashMap<>();
    private final Tokenizer tokenizer = new SimpleTokenizer();
    private long totalTokens = 0L;

    @Override
    public void index(String corpusDirPath) {
        File dir = new File(corpusDirPath);
        if (!dir.isDirectory()) {
            System.out.println("Not a directory: " + corpusDirPath);
            return;
        }
        LoggerUtil.log("Indexing started: " + corpusDirPath);

        for (File file : dir.listFiles((d, name) -> name.endsWith(".txt"))) {
            indexFile(file);
        }
        LoggerUtil.log("Indexing finished. Total tokens: " + totalTokens);
    }

    private void indexFile(File file) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
            List<String> tokens = tokenizer.tokenize(line);
                for (String token : tokens) {
                    if (token.isEmpty()) continue;
                    totalTokens++;
                    String w = token.toLowerCase();
                    Entry e = entries.computeIfAbsent(w, Entry::new);
                    e.incrementFrequency();
                    e.addSample(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.log("Unsupported encoding for file: " + file.getName());
        } catch (IOException e) {
            LoggerUtil.log("IO error: " + e.getMessage());
        }
    }

    @Override
    public Entry lookup(String word) {
        return entries.get(word.toLowerCase());
    }

    public Map<String, Entry> getTopK(int k) {
        return entries.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(
                        b.getValue().getFrequency(),
                        a.getValue().getFrequency()))
                .limit(k)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new));
    }

    @Override
    public void exportDictionary(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("word,freq,samples,ratio\n");
            for (Entry e : entries.values()) {
                double ratio = (totalTokens == 0)
                        ? 0.0
                        : (double) e.getFrequency() / totalTokens;
                String samples = String.join(" || ", e.getSamples())
                        .replace("\"", "'");
                bw.write(e.getWord() + "," + e.getFrequency() + ",\"" +
                        samples + "\"," + ratio + "\n");
            }
        } catch (IOException e) {
            LoggerUtil.log("Error writing dictionary: " + e.getMessage());
        }
    }
}