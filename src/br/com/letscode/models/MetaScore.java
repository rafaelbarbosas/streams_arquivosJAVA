package br.com.letscode.models;

public class MetaScore extends UnsignedInteger{

    public MetaScore(int value) {
        super(value);
        if(value > 100){
            throw new IllegalArgumentException("MetaScore cannot be greater than 100");
        }
    }

    @Override
    public void setValue(int value) {
        if(value > 100){
            throw new IllegalArgumentException("MetaScore cannot be greater than 100");
        }
        super.setValue(value);
    }
    
}
