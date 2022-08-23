package br.com.letscode.model.system;

import java.util.Comparator;

public enum Conditional {
    EQUAL, LESS_THAN, MORE_THAN, LESS_EQUAL, MORE_EQUAL, DIFFERENT;

    public <T> boolean test(Comparator<T> comparator, T object1, T object2) {
        final int result = comparator.compare(object1, object2);
        switch (this) {
            case EQUAL:
                return result == 0;
            case LESS_THAN:
                return result < 0;
            case MORE_THAN:
                return result > 0;
            case LESS_EQUAL:
                return result <= 0;
            case MORE_EQUAL:
                return result >= 0;
            case DIFFERENT:
                return result != 0;
        }
        return false;
    }

    public static Conditional valueOfSymbol(String conditionalSymbol) {
        switch (conditionalSymbol) {
            case "=":
                return EQUAL;
            case "<":
                return LESS_THAN;
            case ">":
                return MORE_THAN;
            case "<=":
                return LESS_EQUAL;
            case ">=":
                return MORE_EQUAL;
            case "!=":
                return DIFFERENT;
            default:
                return null;
        }
    }
}
