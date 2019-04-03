package textclassifier2.model.modelimp;


import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;

import java.util.HashSet;
import java.util.Set;

public class DefCharacteristic implements Characteristic {

    private String name;

    private Set<CharacteristicValue> possibleValues = new HashSet<>();

    @java.beans.ConstructorProperties({"name"})
    public DefCharacteristic(String name) {
        this.name = name;
    }

    @Override
    public void setPossibleValues(Set<CharacteristicValue> charVals) {
        this.possibleValues = possibleValues;
    }

    public void addPossibleValue(CharacteristicValue value) {
        possibleValues.add(value);
    }

    @Override
    public boolean equals(Object o) {
        return ((o instanceof Characteristic) && (this.name.equals(((Characteristic) o).getName())));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String getName() {return this.name;}

    public Set<CharacteristicValue> getPossibleValues() {return this.possibleValues;}

    public void setName(String name) {this.name = name; }

    public String toString() {return "DefCharacteristic(name=" + this.getName() + ", possibleValues=" + this.getPossibleValues() + ")";}
}
