package com.lexicon.core;

public abstract class LexiconStore {

    public abstract void index(String corpusDirPath);

    public abstract Entry lookup(String word);

    public abstract void exportDictionary(String filePath);
}