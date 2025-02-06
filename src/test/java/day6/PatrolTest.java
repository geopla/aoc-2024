package day6;

import day6.Lifecycle.Computed;
import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static day6.CardinalDirection.*;
import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;
import static org.assertj.core.api.Assertions.assertThat;

class PatrolTest {

    @Test
    @DisplayName("Should walk straight to border in empty room")
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

    @Test
    @DisplayName("Should provide positions in empty room")
    void shouldProvidePositionsOfWalkInEmptyRoom() {
        var room = Room.from("""
                ......
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);

        assertThat(patrol.positionsVisitedByGuard(0)).containsExactly(
          new Position(3, 2),
          new Position(3, 1),
          new Position(3, 0)
        );
    }

    @Test
    @DisplayName("Should layout walk with SINGLE obstruction in room")
    void shouldLayoutWalkWithSingleObstructionInRoom() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);

        assertThat(patrol.walkOfGuard(0)).containsExactly(
                new Leg<>(new Position(3, 2), NORTH, 1, new Computed(OBSTRUCTION)),
                new Leg<>(new Position(3, 1), EAST, 2, new Computed(BORDER))
        );
    }

    @Test
    @DisplayName("Should provide positions of walk in room with single obstruction")
    void shouldProvidePositionsOfWalkInRoomWithSingleObstruction() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);

        assertThat(patrol.positionsVisitedByGuard(0)).containsExactly(
                // leg 1
                new Position(3, 2),
                new Position(3, 1),
                // leg 2
                new Position( 4, 1),
                new Position( 5, 1)
        );
    }

    @Test
    @DisplayName("Should layout walk with MULTIPLE obstructions in room")
    void shouldLayoutWalkWithMultipleObstructionsInRoom() {
        var room = Room.from("""
                ._.#..
                ._.__#
                #__^_.
                ....#.""");

        var patrol = new Patrol(room);

        assertThat(patrol.walkOfGuard(0)).containsExactly(
                new Leg<>(new Position(3, 2), NORTH, 1, new Computed(OBSTRUCTION)),
                new Leg<>(new Position(3, 1), EAST,  1, new Computed(OBSTRUCTION)),
                new Leg<>(new Position(4, 1), SOUTH, 1, new Computed(OBSTRUCTION)),
                new Leg<>(new Position(4, 2), WEST,  3, new Computed(OBSTRUCTION)),
                new Leg<>(new Position(1, 2), NORTH, 2, new Computed(BORDER))
        );
    }

    @Test
    @DisplayName("Should provide positions of walk in room with multiple obstructions")
    void shouldProvidePositionsOfWalkInRoomWithMultipleObstructions() {
        var room = Room.from("""
                ._.#..
                ._.__#
                #__^_.
                ....#.""");

        var patrol = new Patrol(room);

        assertThat(patrol.positionsVisitedByGuard(0)).containsExactly(
                // leg 0 - north
                new Position(3, 2),
                new Position(3, 1),
                // leg 1 - east
                new Position( 4,1),
                // leg 2 - south
                new Position( 4,2),
                // leg 3 - west
                new Position(3, 2),
                new Position(2, 2),
                new Position(1, 2),
                // leg 5 - north
                new Position(1, 1),
                new Position(1, 0)
        );
    }
}