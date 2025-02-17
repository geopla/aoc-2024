package day7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PermutatorTest {

    @Test
    @DisplayName("Should provide single operator 'addition'")
    void shouldProvideSingleOperatorAddition() {
        var symbols = "+";

        var operatorsForSymbol = Permutator.operatorsFrom(symbols.chars());

        assertThat(operatorsForSymbol.map(Operator::symbol)).containsExactly('+');

        var additionResult = Permutator.operatorsFrom(symbols.chars())
                .findFirst()
                .get()
                .operator()
                .apply(3, 7);

        assertThat(additionResult).isEqualTo(10);
    }

    @Test
    @DisplayName("Should provide single operator 'multiplication'")
    void shouldProvideSingleOperatorMultiplication() {
        var symbols = "*";

        var operators = Permutator.operatorsFrom(symbols.chars());

        assertThat(operators.map(Operator::symbol)).containsExactly('*');

        var additionResult = Permutator.operatorsFrom(symbols.chars())
                .findFirst()
                .get()
                .operator()
                .apply(3, 7);

        assertThat(additionResult).isEqualTo(21);
    }

    @Test
    @DisplayName("Should provide multiple operators")
    void shouldProvideMultipleOperators() {
        var symbols = "++*+**";

        var operators = Permutator.operatorsFrom(symbols.chars());

        assertThat(operators.map(Operator::symbol)).containsExactly(
                '+',
                '+',
                '*',
                '+',
                '*',
                '*'
        );
    }

    @Test
    @DisplayName("Should reject unknown operator symbol")
    void shouldRejectUnknownOperatorSymbol() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                        Permutator.operatorsFrom("-".chars())
                                .findFirst()
                )
                .withMessage("unrecognized operator symbol: '-'");
    }
}