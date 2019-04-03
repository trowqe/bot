package textclassifier2.ngram;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

class Unigram implements NGramStrategy {

    private final NGRAM_TYPES ngramType;

    Unigram(NGRAM_TYPES ngramType) {
        this.ngramType = ngramType;
    }

    @Override
    public Set<String> getNGram(String text) {
        if (text == null) {
            text = "";
        }

        // get all words and digits
        String[] words = text.toLowerCase().split("[ \\pP\n\t\r$+<>№=]");

        Set<String> uniqueValues = new LinkedHashSet<>(Arrays.asList(words));
        uniqueValues.removeIf(s -> s.equals(""));

        return uniqueValues;
    }

    @Override
    public NGRAM_TYPES getNGramType() {
        return ngramType;
    }

    @Override
    public String toString() {
        return NGRAM_TYPES.UNIGRAM.toString();
    }
}
