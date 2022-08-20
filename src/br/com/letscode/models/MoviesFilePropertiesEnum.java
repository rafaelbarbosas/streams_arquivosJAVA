package br.com.letscode.models;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Year;
import java.util.function.Function;

import br.com.letscode.utils.FunctionUtils;

public enum MoviesFilePropertiesEnum {
    RANK(0,
            text -> new UnsignedInteger(Integer.parseInt(text))),
    TITLE(1,
            Function.identity()),
    GENRE(2,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    DESCRIPTION(3,
            Function.identity()),
    DIRECTORS(4,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    ACTORS(5,
            FunctionUtils.SET_OF_STRINGS_CONVERSOR),
    YEAR(6,
            text -> Year.of(Integer.parseInt(text))),
    RUNTIME(7,
            text -> Duration.ofMinutes(Integer.parseInt(text))),
    RATING(8,
            text -> Float.parseFloat(text)),
    VOTES(9,
            text -> new UnsignedInteger(Integer.parseInt(text))),
    REVENUE(10,
            text -> BigDecimal.valueOf(Double.parseDouble(text)).multiply(Movie.REVENUE_MULTIPLIER)),
    METASCORE(11,
            text -> new MetaScore(Integer.parseInt(text)));

    private int index;
    private Function<String, ? extends Object> conversor;

    public int getIndex() {
        return index;
    }

    public Object convert(String text) {
        if (text == null || text.strip().length() == 0) {
            return null;
        }
        if (text.charAt(0) == '"') {
            text = text.substring(1);
        }
        if (text.charAt(text.length() - 1) == '"') {
            text = text.substring(0, text.length() - 1);
        }
        return conversor.apply(text);
    }

    private <T> MoviesFilePropertiesEnum(int index, Function<String, ? extends Object> conversor) {
        this.index = index;
        this.conversor = conversor;
    }
}
