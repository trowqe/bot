package textclassifier2.model;

import java.util.Set;

public interface Characteristic {

    void setName(String name);

    String getName();

    Set<CharacteristicValue> getPossibleValues();

    void setPossibleValues(Set<CharacteristicValue> charVals);

    void addPossibleValue(CharacteristicValue value);
}