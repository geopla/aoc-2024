package day6;

import day6.Lifecycle.Computed;
import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static day6.CardinalDirection.NORTH;
import static day6.Terminator.BORDER;
import static org.assertj.core.api.Assertions.assertThat;

class PatrolTest {

    @Test
    @DisplayName("Should walk straight to border in empty room (single guard)")
    void shouldWalkStraightToBorderInEmptyRoom() {
        var room = Room.from("""
                ......
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);

        assertThat(patrol.walkOfGuard(0)).containsExactly(
           new Leg<>(new Position(3, 2), NORTH, 2, new Computed(BORDER))
        );
    }
}