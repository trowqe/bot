package textclassifier2.model.modelimp;

import textclassifier2.model.*;

import java.util.Set;
import java.util.UUID;
// Remark about GUID. Normally you would implement auto-incremental / generated ID and enforced it by database property.
public class DefClassifiableFactory implements ClassifiableFactory {

    public Characteristic newCharacteristic(String name) {
        return new DefCharacteristic(name);
    }

    public CharacteristicValue newCharacteristicValue(String value, int orderNumber, Characteristic characteristic) {
        return new DefCharacteristicValue(UUID.randomUUID().toString(), value, orderNumber, characteristic);
    }

    public VocabularyWord newVocabularyWord(String value) {
        return new DefVocabularyWord(UUID.randomUUID().toString(), value);
    }

    public ClassifiableText newClassifiableText(String text, Set<CharacteristicValue> characteristics) {
        return new DefClassifiableText(UUID.randomUUID().toString(), text, characteristics);
    }

}
