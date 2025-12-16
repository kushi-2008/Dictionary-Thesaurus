package com.lexicon.thesaurus;

import java.util.HashMap;
import java.util.Map;

public class SynonymUnionFind {

    // parent map: word -> parent word
    private final Map<String, String> parent = new HashMap<>();

    // find with path compression
    private String find(String w) {
        parent.putIfAbsent(w, w);
        String p = parent.get(w);
        if (!p.equals(w)) {
            p = find(p);
            parent.put(w, p);
        }
        return p;
    }

    // union two words; smallest (lexicographically) becomes root
    public void union(String a, String b) {
        String ra = find(a);
        String rb = find(b);
        if (ra.equals(rb)) {
            return;
        }
        // choose lexicographically smaller root
        if (ra.compareTo(rb) <= 0) {
            parent.put(rb, ra);
        } else {
            parent.put(ra, rb);
        }
    }

    // external getter used by SynonymManager.buildGroups()
    public String getRoot(String w) {
        return find(w);
    }

    public Map<String, String> getParents() {
        return parent;
    }
}