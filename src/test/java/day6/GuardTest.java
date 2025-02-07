package day6;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static day6.CardinalDirection.NORTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class GuardTest {

    @Test
    @DisplayName("Should always be assigned to a room")
    void shouldAlwaysBeAssignedToARoom() {
        var startPosition = Instancio.create(Room.Position.class);

        assertThatIllegalArgumentException().isThrownBy(() ->
                        new Guard(null, startPosition, '^'))
                .withMessage("guard must be assigned to a room");
    }

    @Test
    @DisplayName("Should have a start position assigned")
    void shouldHaveAStartPosition() {
        Room room = Instancio.create(Room.class);

        assertThatIllegalArgumentException().isThrownBy(() ->
                        new Guard(room, null, '^'))
                .withMessage("guard must be placed on a start position");
    }

    @Test
    @DisplayName("Should have a direction facing at start")
    void shouldHaveAStartFacing() {
        var room = Instancio.create(Room.class);
        var startPosition = Instancio.create(Room.Position.class);

        var guard = new Guard(room, startPosition, '^');

        assertThat(guard.startFacing()).isEqualTo(NORTH);
    }

    @Test
    @DisplayName("Should have turn right on obstruction strategy by default")
    void shouldHaveTurnRightStrategyByDefault() {
        var room = Instancio.create(Room.class);
        var startPosition = Instancio.create(Room.Position.class);

        var guard = new Guard(room, startPosition, '^');

        assertThat(guard.turnStrategy()).isInstanceOf(TurnRight.class);
    }
}