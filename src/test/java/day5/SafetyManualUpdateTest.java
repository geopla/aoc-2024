package day5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SafetyManualUpdateTest {

    SafetyManualUpdate safetyManualUpdate;

    @BeforeEach
    void setUp() {
        safetyManualUpdate = new SafetyManualUpdate();
    }

    @Test
    @DisplayName("Should accumulate middle page numbers of VALID updates of challenge")
    void shouldAccumulateMiddlePageNumberOfValidUpdates() {
        safetyManualUpdate.printJobFromInput("day-5-input.txt");

        int middlePageNumberSum = safetyManualUpdate.middlePageNumberSumOfValidUpdates();

        assertThat(middlePageNumberSum).isEqualTo(5651);
    }

    @Test
    @DisplayName("Should accumulate middle page numbers INVALID updates of challenge")
    void shouldAccumulateMiddlePageNumberOfInput() {
        safetyManualUpdate.printJobFromInput("day-5-input.txt");

        int middlePageNumberSum = safetyManualUpdate.middlePageNumberSumOfReorderedUpdates();

        assertThat(middlePageNumberSum).isEqualTo(4743);
    }

    @Test
    @DisplayName("Should provide valid updates for example input")
    void shouldProvideValidUpdatesOfExampleInput() {
        safetyManualUpdate.printJobFromInput("day-5-example-input.txt");

        Stream<Update> validUpdates = safetyManualUpdate.validUpdates();

        assertThat(validUpdates).containsExactlyInAnyOrder(
                new Update(75, 47, 61, 53, 29),
                new Update(97, 61, 53, 29, 13),
                new Update(75, 29, 13)
        );
    }

    @Test
    @DisplayName("Should accumulate middle page number for example input")
    void shouldAccumulateMiddlePageNumbersOfExampleInput() {
        safetyManualUpdate.printJobFromInput("day-5-example-input.txt");

        int middlePageNumberSum = safetyManualUpdate.middlePageNumberSumOfValidUpdates();

        assertThat(middlePageNumberSum).isEqualTo(143);
    }

    @Test
    @DisplayName("Should provide INVALID updates of example input")
    void shouldProvideInvalidUpdatesOfExampleInput() {
        safetyManualUpdate.printJobFromInput("day-5-example-input.txt");

        Stream<Update> validUpdates = safetyManualUpdate.invalidUpdates();

        assertThat(validUpdates).containsExactlyInAnyOrder(
                new Update(75, 97, 47, 61, 53),
                new Update(61, 13, 29),
                new Update(97, 13, 75, 29, 47)
        );
    }

    @Test
    @DisplayName("Should accumulate middle page number for INVALID updates from example input")
    void shouldAccumulateMiddlePageNumbersOfInvalidUpdatesFromExampleInput() {
        safetyManualUpdate.printJobFromInput("day-5-example-input.txt");

        int middlePageNumberSum = safetyManualUpdate.middlePageNumberSumOfReorderedUpdates();

        assertThat(middlePageNumberSum).isEqualTo(123);
    }

    @Test
    @DisplayName("Should create print job")
    void shouldCreatePrintJob() {
        safetyManualUpdate.printJobFromInput("day-5-example-input.txt");

        assertThat(safetyManualUpdate.printJob()).isNotNull();
    }
}