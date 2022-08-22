package br.com.letscode.model.movie;

import br.com.letscode.model.number.UnsignedInteger;

public class MetaScore extends UnsignedInteger {
    public MetaScore(int value) {
        super(value);
        if (value > 100) {
            throw new IllegalArgumentException("MetaScore cannot be greater than 100");
        }
    }

    @Override
    public void setValue(int value) {
        if (value > 100) {
            throw new IllegalArgumentException("MetaScore cannot be greater than 100");
        }
        super.setValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
