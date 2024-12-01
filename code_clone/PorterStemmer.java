package code_clone;

import java.util.Locale;

public class PorterStemmer {
    
    public String stemWord(String word) {
        String stem = word.toLowerCase(Locale.getDefault());
        if (stem.length() < 3) return stem;

        stem = applySteps(stem);
        return stem;
    }

    private String applySteps(String word) {
        word = step1a(word);
        word = step1b(word);
        word = step1c(word);
        word = step2(word);
        word = step3(word);
        word = step4(word);
        word = step5a(word);
        word = step5b(word);
        return word;
    }

    private String step1a(String word) {
        if (word.endsWith("sses")) return word.substring(0, word.length() - 2);
        if (word.endsWith("ies")) return word.substring(0, word.length() - 2);
        if (word.endsWith("ss")) return word;
        if (word.endsWith("s")) return word.substring(0, word.length() - 1);
        return word;
    }

    private String step1b(String word) {
        if (word.endsWith("eed")) {
            String stem = word.substring(0, word.length() - 1);
            if (getMeasure(getLetterTypes(stem)) > 0) return stem;
            return word;
        }

        if (word.endsWith("ed") || word.endsWith("ing")) {
            String suffix = word.endsWith("ed") ? "ed" : "ing";
            String stem = word.substring(0, word.length() - suffix.length());
            if (containsVowel(stem)) return step1bSecondPart(stem);
        }
        return word;
    }

    private String step1bSecondPart(String word) {
        if (word.endsWith("at") || word.endsWith("bl") || word.endsWith("iz")) {
            return word + "e";
        }
        if (isDoubleConsonant(word) && !endsWithOneOf(word, "l", "s", "z")) {
            return word.substring(0, word.length() - 1);
        }
        if (getMeasure(getLetterTypes(word)) == 1 && isStarO(word)) {
            return word + "e";
        }
        return word;
    }

    private String step1c(String word) {
        if (word.endsWith("y")) {
            String stem = word.substring(0, word.length() - 1);
            if (containsVowel(stem)) return stem + "i";
        }
        return word;
    }

    private String step2(String word) {
        String[][] suffixPairs = {
            {"ational", "ate"}, {"tional", "tion"}, {"enci", "ence"}, {"anci", "ance"},
            {"izer", "ize"}, {"bli", "ble"}, {"alli", "al"}, {"entli", "ent"},
            {"eli", "e"}, {"ousli", "ous"}, {"ization", "ize"}, {"ation", "ate"},
            {"ator", "ate"}, {"alism", "al"}, {"iveness", "ive"}, {"fulness", "ful"},
            {"ousness", "ous"}, {"aliti", "al"}, {"iviti", "ive"}, {"biliti", "ble"},
            {"logi", "log"}
        };

        return replaceSuffix(word, suffixPairs, 0);
    }

    private String step3(String word) {
        String[][] suffixPairs = {
            {"icate", "ic"}, {"ative", ""}, {"alize", "al"}, {"iciti", "ic"},
            {"ical", "ic"}, {"ful", ""}, {"ness", ""}
        };

        return replaceSuffix(word, suffixPairs, 0);
    }

    private String step4(String word) {
        String[] suffixes = {
            "al", "ance", "ence", "er", "ic", "able", "ible", "ant", "ement",
            "ment", "ent", "ion", "ou", "ism", "ate", "iti", "ous", "ive", "ize"
        };

        for (String suffix : suffixes) {
            if (word.endsWith(suffix)) {
                String stem = word.substring(0, word.length() - suffix.length());
                if (getMeasure(getLetterTypes(stem)) > 1) {
                    if (suffix.equals("ion")) {
                        char lastChar = stem.charAt(stem.length() - 1);
                        if (lastChar == 's' || lastChar == 't') return stem;
                    } else {
                        return stem;
                    }
                }
                return word;
            }
        }
        return word;
    }

    private String step5a(String word) {
        if (word.endsWith("e")) {
            String stem = word.substring(0, word.length() - 1);
            int measure = getMeasure(getLetterTypes(stem));
            if (measure > 1 || (measure == 1 && !isStarO(stem))) return stem;
        }
        return word;
    }

    private String step5b(String word) {
        if (word.endsWith("ll") && getMeasure(getLetterTypes(word)) > 1) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    private String replaceSuffix(String word, String[][] suffixPairs, int mThreshold) {
        for (String[] pair : suffixPairs) {
            if (word.endsWith(pair[0])) {
                String stem = word.substring(0, word.length() - pair[0].length());
                if (getMeasure(getLetterTypes(stem)) > mThreshold) return stem + pair[1];
            }
        }
        return word;
    }

    private boolean containsVowel(String word) {
        for (char c : word.toCharArray()) {
            if ("aeiou".indexOf(c) != -1) return true;
        }
        return false;
    }

    private boolean isDoubleConsonant(String word) {
        if (word.length() < 2) return false;
        char last = word.charAt(word.length() - 1);
        char secondLast = word.charAt(word.length() - 2);
        return last == secondLast && getLetterType(last) == 'c';
    }

    private boolean isStarO(String word) {
        if (word.length() < 3) return false;
        char last = word.charAt(word.length() - 1);
        if ("wxy".indexOf(last) != -1) return false;

        char secondLast = word.charAt(word.length() - 2);
        char thirdLast = word.charAt(word.length() - 3);

        return getLetterType(secondLast) == 'c' && getLetterType(thirdLast) == 'v';
    }

    private boolean endsWithOneOf(String word, String... chars) {
        for (String c : chars) {
            if (word.endsWith(c)) return true;
        }
        return false;
    }

    private String getLetterTypes(String word) {
        StringBuilder letterTypes = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char type = getLetterType(word.charAt(i));
            if (letterTypes.length() == 0 || letterTypes.charAt(letterTypes.length() - 1) != type) {
                letterTypes.append(type);
            }
        }
        return letterTypes.toString();
    }

    private char getLetterType(char letter) {
        if ("aeiou".indexOf(letter) != -1) return 'v'; 
        return 'c'; 
    }

    private int getMeasure(String letterTypes) {
        return (letterTypes.length() - 1) / 2;
    }
}
