package textclassifier2.model;

import java.util.Set;

// ABSTRACT FACTORY
public interface ClassifiableFactory {

    Characteristic newCharacteristic(String name);

    CharacteristicValue newCharacteristicValue(String value, int orderNumber, Characteristic characteristic);

    VocabularyWord newVocabularyWord(String value);

    ClassifiableText newClassifiableText(String text, Set<CharacteristicValue> characteristics);

}
