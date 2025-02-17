package day7;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.util.Arrays;

public class ToListOfIntegerConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (source instanceof String listRepresentation) {
            String[] integerRepresentation =  listRepresentation.replaceAll("[\\[\\]]", "").split(",");

            return Arrays.stream(integerRepresentation)
                    .map(String::trim)
                    .filter(s -> ! s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
        }

        throw new IllegalArgumentException("can't convert to List<Integer>");
    }
}
