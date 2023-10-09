package com.demo.java_utilities;

import java.util.function.Function;
import java.util.function.Predicate;

public class JavaStreamUtils {

    public static <T, E> Predicate<T> equalsFirstSeenValue(
        Function<T, E> valueFunction
    ) {
        return new Predicate<T>() {
            private E firstValue = null;

            @Override
            public boolean test(T entry) {
                E value = valueFunction.apply(entry);
                if (firstValue == null) {
                    firstValue = value;
                    return true;
                }
                return value.equals(firstValue);
            }
        };
    }
}
