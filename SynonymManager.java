package com.lexicon.thesaurus;

import com.lexicon.exception.CorruptSynonymrowException;
import com.lexicon.util.LoggerUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SynonymManager {

    private final SynonymUnionFind uf = new SynonymUnionFind();
    private final Map<String, Set<String>> groups = new LinkedHashMap<>();

    public void loadSynonyms(String csvPath) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(csvPath), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    processRow(line);   // ✅ fixed method call
                } catch (CorruptSynonymrowException e) {
                    LoggerUtil.log("Corrupt synonym row: " + line);
                }
            }
            buildGroups();
        } catch (IOException e) {
            LoggerUtil.log("Error reading synonyms: " + e.getMessage());
        }
    }

    // ✅ Method name fixed (case-sensitive)
    private void processRow(String line) throws CorruptSynonymrowException {
        String[] parts = line.split(",");
        if (parts.length < 2) {
            throw new CorruptSynonymrowException("Not enough columns");
        }

        String w1 = parts[0].trim().toLowerCase();
        String w2 = parts[1].trim().toLowerCase();

        if (w1.isEmpty() || w2.isEmpty()) {
            throw new CorruptSynonymrowException("Empty word");
        }

        uf.union(w1, w2);
    }

    private void buildGroups() {
        Map<String, String> parents = uf.getParents();
        for (String word : parents.keySet()) {
            String root = uf.getRoot(word);
            groups.computeIfAbsent(root, k -> new TreeSet<>()).add(word);
        }
    }

    public Set<String> getSynonyms(String word) {
        if (word == null) return Collections.emptySet();

        String root = uf.getRoot(word.toLowerCase());
        Set<String> set = groups.get(root);

        if (set == null) return Collections.emptySet();

        Set<String> result = new LinkedHashSet<>(set);
        result.remove(word.toLowerCase());
        return result;
    }

    public void exportThesaurus(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("group,words");
            bw.newLine();

            for (Map.Entry<String, Set<String>> entry : groups.entrySet()) {
                String line = entry.getKey() + "," + String.join(";", entry.getValue());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            LoggerUtil.log("Error writing thesaurus: " + e.getMessage());
        }
    }
}
