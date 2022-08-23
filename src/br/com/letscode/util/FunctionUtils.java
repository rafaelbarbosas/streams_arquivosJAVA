package br.com.letscode.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FunctionUtils {
    public static final Function<String, ?> SET_OF_STRINGS_CONVERSOR = text -> List.of(text.split(",")).stream()
            .map(genre -> genre.strip())
            .collect(Collectors.toSet());
}
