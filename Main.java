package com.lexicon.cli;
import com.lexicon.core.DictionaryStore;
import com.lexicon.core.LexiconStore;
import com.lexicon.thesaurus.SynonymManager;
import java.io.File;
public class Main {
    private static final String DICT_FILE = "dictionary.csv";
    private static final String SYN_FILE = "synonyms.csv";
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Commands:");
            System.out.println(" index <corpus_dir>");
            System.out.println(" lookup <word>");
            System.out.println(" synonyms <word>");
            System.out.println(" export");
            return;
        }
        LexiconStore store = new DictionaryStore();
        SynonymManager synonymManager = new SynonymManager();

        String cmd = args[0].toLowerCase();

        try {
            switch (cmd) {

                case "index":
                    if (args.length < 2) {
                        System.out.println("Usage: index <corpus_dir>");
                        return;
                    }

                    File corpus = new File(args[1]);
                    if (!corpus.exists() || !corpus.isDirectory()) {
                        System.out.println("Invalid corpus directory: " + args[1]);
                        return;
                    }

                    store.index(args[1]);
                    store.exportDictionary(DICT_FILE);
                    System.out.println("Indexing completed. Dictionary saved.");
                    break;

                case "lookup":
                    if (args.length < 2) {
                        System.out.println("Usage: lookup <word>");
                        return;
                    }

                    store.exportDictionary(DICT_FILE);   // ✅ MUST load
                    System.out.println(store.lookup(args[1].toLowerCase()));
                    break;


			case "synonyms":
                    if (args.length < 2) {
                        System.out.println("Usage: synonyms <word>");
                        return;
                    }

                    synonymManager.loadSynonyms(SYN_FILE);
                    System.out.println(synonymManager.getSynonyms(args[1].toLowerCase()));
                    break;

                case "export":
                    store.exportDictionary(DICT_FILE);
                    synonymManager.loadSynonyms(SYN_FILE);

                    store.exportDictionary("dictionary.csv");
                    synonymManager.exportThesaurus("thesaurus.csv");

                    System.out.println("Export completed.");
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}