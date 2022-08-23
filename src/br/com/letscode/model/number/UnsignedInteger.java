package br.com.letscode.model.number;

public class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {
    private int value;

    public UnsignedInteger(int value) {
        if (value < 0) {
            value *= -1;
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0) {
            value *= -1;
        }
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return Integer.valueOf(this.value).equals((Integer) obj);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(UnsignedInteger o) {
        return Integer.valueOf(value).compareTo(o.getValue());
    }
}
