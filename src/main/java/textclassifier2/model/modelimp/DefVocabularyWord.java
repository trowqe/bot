package textclassifier2.model.modelimp;

import textclassifier2.model.VocabularyWord;
import textclassifier2.ngram.NGramStrategy;

import java.util.Objects;

public class DefVocabularyWord implements VocabularyWord {

    private String id;

    private final String value;

    private final NGramStrategy.NGRAM_TYPES ngram;

    public DefVocabularyWord(String id, String value) {
        this.id = id;
        this.value = value;
        this.ngram = null;
    }

    public DefVocabularyWord(String value, NGramStrategy.NGRAM_TYPES ngram) {
        this.value = value;
        this.ngram = ngram;
    }

    public DefVocabularyWord(String id, String value, NGramStrategy.NGRAM_TYPES ngram) {
        this.id = id;
        this.value = value;
        this.ngram = ngram;
    }

    public String getId() {return this.id;}

    public String getValue() {return this.value;}

    public String getNgram() {return (ngram == null) ? null :  this.ngram.toString();}

    public void setId(String id) {this.id = id; }

    public String toString() {return "DefVocabularyWord(id=" + this.getId() + ", value=" + this.getValue() + ", ngram=" + this.getNgram() + ")";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefVocabularyWord that = (DefVocabularyWord) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
