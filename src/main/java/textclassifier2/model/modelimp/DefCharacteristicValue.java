package textclassifier2.model.modelimp;

import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;

public class DefCharacteristicValue implements CharacteristicValue {

    private String id;
    private String value;
    // It is used to decode classifier value from vector when classify method() is invoked
    private int orderNumber;
    private Characteristic characteristic;

    @java.beans.ConstructorProperties({"id", "value", "orderNumber", "characteristic"})
    public DefCharacteristicValue(String id, String value, int orderNumber, Characteristic characteristic) {
        this.id = id;
        this.value = value;
        this.orderNumber = orderNumber;
        this.characteristic = characteristic;
    }

    public DefCharacteristicValue() {}

    @Override
    public boolean equals(Object o) {
        return ((o instanceof DefCharacteristicValue) && (this.value.equals(((DefCharacteristicValue) o).getValue())));
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return String.format("value: %s, orderNumber %s",  value, orderNumber);
    }

    public String getId() {return this.id;}

    public String getValue() {return this.value;}

    public int getOrderNumber() {return this.orderNumber;}

    public Characteristic getCharacteristic() {return this.characteristic;}

    public void setId(String id) {this.id = id; }

    public void setValue(String value) {this.value = value; }

    public void setOrderNumber(int orderNumber) {this.orderNumber = orderNumber; }

    public void setCharacteristic(Characteristic characteristic) {this.characteristic = characteristic; }
}
