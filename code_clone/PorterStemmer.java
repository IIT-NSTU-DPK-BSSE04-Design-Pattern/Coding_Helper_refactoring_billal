package code_clone;

import java.util.Locale;

public class PorterStemmer {

    public String stemWord(String word) {
        if (word.length() < 3) return word.toLowerCase(Locale.getDefault());
        return applySteps(word.toLowerCase(Locale.getDefault()));
    }

    private String applySteps(String word) {
        word = step1a(word);
        word = step1b(word);
        word = step1c(word);
        word = step2(word);
        word = step3(word);
        word = step4(word);
        word = step5a(word);
        return step5b(word);
    }


    private String step1a(String word) {
        if (word.endsWith("sses") || word.endsWith("ies")) return word.substring(0, word.length() - 2);
        if (word.endsWith("s") && !word.endsWith("ss")) return word.substring(0, word.length() - 1);
        return word;
    }

    private String step1b(String word) {
        if (word.endsWith("eed") && getMeasure(stem(word, 1)) > 0) {
            return word.substring(0, word.length() - 1);
        }

        if (endsWithAny(word, "ed", "ing")) {
            String suffix = word.endsWith("ed") ? "ed" : "ing";
            String stem = word.substring(0, word.length() - suffix.length());
            if (containsVowel(stem)) return step1bSecondPart(stem);
        }
        return word;
    }

    private String step1bSecondPart(String word) {
        if (endsWithAny(word, "at", "bl", "iz")) return word + "e";
        if (isDoubleConsonant(word) && !endsWithAny(word, "l", "s", "z")) {
            return word.substring(0, word.length() - 1);
        }
        if (getMeasure(word) == 1 && isStarO(word)) return word + "e";
        return word;
    }

    private String step1c(String word) {
        if (word.endsWith("y") && containsVowel(word.substring(0, word.length() - 1))) {
            return word.substring(0, word.length() - 1) + "i";
        }
        return word;
    }

    private String step2(String word) {
        return replaceSuffix(word, new String[][]{
            {"ational", "ate"}, {"tional", "tion"}, {"enci", "ence"}, {"anci", "ance"},
            {"izer", "ize"}, {"bli", "ble"}, {"alli", "al"}, {"entli", "ent"},
            {"eli", "e"}, {"ousli", "ous"}, {"ization", "ize"}, {"ation", "ate"},
            {"ator", "ate"}, {"alism", "al"}, {"iveness", "ive"}, {"fulness", "ful"},
            {"ousness", "ous"}, {"aliti", "al"}, {"iviti", "ive"}, {"biliti", "ble"},
            {"logi", "log"}
        }, 0);
    }

    private String step3(String word) {
        return replaceSuffix(word, new String[][]{
            {"icate", "ic"}, {"ative", ""}, {"alize", "al"}, {"iciti", "ic"},
            {"ical", "ic"}, {"ful", ""}, {"ness", ""}
        }, 0);
    }

    private String step4(String word) {
        for (String suffix : new String[]{
            "al", "ance", "ence", "er", "ic", "able", "ible", "ant", "ement", 
            "ment", "ent", "ion", "ou", "ism", "ate", "iti", "ous", "ive", "ize"
        }) {
            if (word.endsWith(suffix)) {
                String stem = word.substring(0, word.length() - suffix.length());
                if (getMeasure(stem) > 1) {
                    return suffix.equals("ion") && endsWithAny(stem, "s", "t") ? stem : stem;
                }
                return word;
            }
        }
        return word;
    }

    private String step5a(String word) {
        if (word.endsWith("e")) {
            String stem = word.substring(0, word.length() - 1);
            if (getMeasure(stem) > 1 || (getMeasure(stem) == 1 && !isStarO(stem))) return stem;
        }
        return word;
    }

    private String step5b(String word) {
        return (word.endsWith("ll") && getMeasure(word) > 1) ? word.substring(0, word.length() - 1) : word;
    }

    private String replaceSuffix(String word, String[][] suffixPairs, int mThreshold) {
        for (String[] pair : suffixPairs) {
            if (word.endsWith(pair[0])) {
                String stem = word.substring(0, word.length() - pair[0].length());
                if (getMeasure(stem) > mThreshold) return stem + pair[1];
            }
        }
        return word;
    }

    // Utility Methods
    private String stem(String word, int removeChars) {
        return word.substring(0, word.length() - removeChars);
    }

    private boolean containsVowel(String word) {
        for (char c : word.toCharArray()) {
            if ("aeiou".indexOf(c) != -1) return true;
        }
        return false;
    }

    private boolean isDoubleConsonant(String word) {
        int len = word.length();
        return len > 1 && word.charAt(len - 1) == word.charAt(len - 2) && isConsonant(word.charAt(len - 1));
    }

    private boolean isStarO(String word) {
        int len = word.length();
        return len >= 3 && isConsonant(word.charAt(len - 3)) &&
               !isConsonant(word.charAt(len - 2)) &&
               isConsonant(word.charAt(len - 1)) && !"wxy".contains(String.valueOf(word.charAt(len - 1)));
    }

    private boolean endsWithAny(String word, String... suffixes) {
        for (String suffix : suffixes) {
            if (word.endsWith(suffix)) return true;
        }
        return false;
    }

    private boolean isConsonant(char c) {
        return "aeiou".indexOf(c) == -1;
    }

    private int getMeasure(String word) {
        StringBuilder pattern = new StringBuilder();
        boolean lastWasVowel = false;
        for (char c : word.toCharArray()) {
            boolean isVowel = "aeiou".indexOf(c) != -1;
            if (isVowel != lastWasVowel) {
                pattern.append(isVowel ? 'v' : 'c');
                lastWasVowel = isVowel;
            }
        }
        return (pattern.length() - 1) / 2;
    }
}
