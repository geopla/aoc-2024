package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationFactoryTest {

    @Test
    @DisplayName("Should create multiplier 'mul'")
    void shouldCreateMultiplier() {
        Operation multiplier = OperationFactory.create(of("mul", "4", "2"));
        assertThat(multiplier).isEqualTo(new Operation.Multiplier(List.of("4", "2")));
    }

    @Test
    @DisplayName("Should create activator 'do'")
    void shouldCreateActivator() {
        Operation multiplier = OperationFactory.create(of("do"));
        assertThat(multiplier).isInstanceOf(Operation.Do.class);
    }

    @Test
    @DisplayName("Should create deactivator 'dont'")
    void shouldCreateDeactivator() {
        Operation multiplier = OperationFactory.create(of("don't"));
        assertThat(multiplier).isInstanceOf(Operation.Dont.class);
    }

    @ParameterizedTest
    @MethodSource("detectComputationalOperation")
    @DisplayName("Should detect computational operations")
    void shouldDetectComputationalOperations(Operation operation, boolean expectedConditional) {
        assertThat(OperationFactory.isComputing(operation)).isEqualTo(expectedConditional);
    }

    static Stream<Arguments> detectComputationalOperation() {
        return Stream.of(
                arguments(new Operation.Multiplier("4", "2"), true),
                arguments(new Operation.Do(), false),
                arguments(new Operation.Dont(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("detectConditionalOperation")
    @DisplayName("Should detect conditional operations")
    void shouldDetectConditionalOperations(Operation operation, boolean expectedConditional) {
        assertThat(OperationFactory.isConditional(operation)).isEqualTo(expectedConditional);
    }

    static Stream<Arguments> detectConditionalOperation() {
        return Stream.of(
                arguments(new Operation.Do(), true),
                arguments(new Operation.Dont(), true),
                arguments(new Operation.Multiplier("4", "2"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("verifyOperationBinarity")
    @DisplayName("Should verify binarity")
    void shouldVerifyBinarity(List<String> operands, boolean expectedBinarity) {
        var operation = new Operation.Multiplier(operands);
        assertThat(OperationFactory.isBinary(operation)).isEqualTo(expectedBinarity);
    }

    static Stream<Arguments> verifyOperationBinarity() {
        return Stream.of(
                arguments(List.of("1"), false),
                arguments(List.of("2", "3"), true),
                arguments(List.of("3", "4", "5"), false)
        );
    }

    @Test
    @DisplayName("Should create an unknown operation")
    void shouldCreateAnUnknownOperation() {
        final List<String> tokensForUnknownOperation = of("xxx", "42", "1000");
        assertThat(OperationFactory.create(tokensForUnknownOperation)).isInstanceOf(Operation.Unknown.class);
    }

}