package textclassifier2.testdata;

import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableText;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

public interface TestDataReader extends AutoCloseable {

    boolean hasNext();

    ClassifiableData next() throws IOException;

    ClassifiableData readAll() throws IOException;

    static Set<CharacteristicValue> getCharacteristicValueTetData(List<ClassifiableText> texts) {
        return texts.stream()
                .flatMap(text ->  text.getCharacteristics().stream())
                .collect(toSet());
    }

    static Set  <Characteristic> getCharacteristicTestData(Set<CharacteristicValue> charVals) {
        //TODO: may get possible CharacteristicValue set from database
        Map<Characteristic, Set<CharacteristicValue>> map = charVals.stream()
            .collect(Collectors.groupingBy(
                    CharacteristicValue::getCharacteristic,
                    HashMap::new,
                    mapping((charVal) -> charVal, toSet())
        ));
        Set<Characteristic> chars = map.entrySet().stream()
            .map((entry) -> {
                    Characteristic entryChar = entry.getKey();
                    entryChar.setPossibleValues(entry.getValue());
                    return entryChar;
                })
            .collect(Collectors.toSet());
        // if you take a slice from characteristic val set you need to recalculate order
        for(Characteristic characteristic : chars) {
            int order = 1;
            for(CharacteristicValue charVal : characteristic.getPossibleValues())
                charVal.setOrderNumber(order++);
        }
        return chars;
    }

    public final class ClassifiableData{
        private final List<ClassifiableText> classifiableTexts;
        private final Set<Characteristic> characteristics;
        private final Set<CharacteristicValue> characteristicValues;

        @java.beans.ConstructorProperties({"classifiableTexts", "characteristics", "characteristicValues"})
        public ClassifiableData(List<ClassifiableText> classifiableTexts, Set<Characteristic> characteristics, Set<CharacteristicValue> characteristicValues) {
            this.classifiableTexts = classifiableTexts;
            this.characteristics = characteristics;
            this.characteristicValues = characteristicValues;
        }

        public boolean isEmpty() {
            return classifiableTexts.size() == 0
                && characteristics.size() == 0
                && characteristicValues.size() == 0;
        }

        public static ClassifiableData empty() {
            return new ClassifiableData(new ArrayList<>(),new HashSet<>(), new HashSet<>());
        }

        public List<ClassifiableText> getClassifiableTexts() {return this.classifiableTexts;}

        public Set<Characteristic> getCharacteristics() {return this.characteristics;}

        public Set<CharacteristicValue> getCharacteristicValues() {return this.characteristicValues;}
    }
}

