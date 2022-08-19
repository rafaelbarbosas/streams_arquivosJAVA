package br.com.letscode.models;

public class UnsignedInteger extends Number {
    private int value;
    
    public UnsignedInteger(int value) {
        if(value < 0){
            value *= -1;
        }
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        if(value < 0){
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
}
