

package com.google.bharatnaganath.ghost2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class FastDictionary implements GhostDictionary {

    private TrieNode root;

    public FastDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        root = new TrieNode();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                root.add(line.trim());
        }
    }
    @Override
    public boolean isWord(String word) {
        return root.isWord(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        //if prefix is empty get any random word
        try {
            if (prefix.isEmpty()) {
                Random random = new Random();
                String alphabet = "abcdefghijklmnopqrstuvwxyz";
                char[] cArray = alphabet.toCharArray();
                return Character.toString(cArray[random.nextInt(26)]);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return root.getAnyWordStartingWith(prefix);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return root.getGoodWordStartingWith(prefix);
    }
}
