package day6;

import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static day6.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;

class PatrolTest {

    private Lifecycle.Computed hitsObstruction = new Lifecycle.Computed(Terminator.OBSTRUCTION);
    private Lifecycle.Computed hitsRoomBorder = new Lifecycle.Computed(Terminator.BORDER);

    @Test
    @DisplayName("Should provide DISTINCT positions visited")
    void shouldProvideDistinctPositionsVisited() {
        var room = Room.from("""
                ..#....
                ......#
                ..x..x.
                .......
                ..^..#.""");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        assertThat(patrol.distinctPositionsVisitedBy(guard)).containsExactly(
                // start
                new Position(2, 4),

                // to north
                new Position(2, 3),
                new Position(2, 2),
                new Position(2, 1),

                // to east
                new Position(3, 1),
                new Position(4, 1),
                new Position(5, 1),

                // to south
                new Position(5, 2),
                new Position(5, 3),

                // to west
                new Position(4, 3),
                new Position(3, 3),
                //     nope (   2,    3)  has been visited already
                new Position(1, 3),
                new Position(0, 3)
        );
    }

    @Test
    @DisplayName("Should provide DISTINCT positions from challenge example")
    void shouldProvideDistinctPositionsFromChallengeExample() {
        var room = roomFromResource("day6-example-input.txt");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        assertThat(patrol.distinctPositionsVisitedBy(guard)).containsExactly(
                // start
                new Position(4, 6),

                new Position(4, 5),
                new Position(4, 4),
                new Position(4, 3),
                new Position(4, 2),
                new Position(4, 1),

                new Position(5, 1),
                new Position(6, 1),
                new Position(7, 1),
                new Position(8, 1),

                new Position(8, 2),
                new Position(8, 3),
                new Position(8, 4),
                new Position(8, 5),
                new Position(8, 6),

                new Position(7, 6),
                new Position(6, 6),
                new Position(5, 6),
                new Position(3, 6),
                new Position(2, 6),

                new Position(2, 5),
                new Position(2, 4),

                new Position(3, 4),
                new Position(5, 4),
                new Position(6, 4),

                new Position(6, 5),
                new Position(6, 7),
                new Position(6, 8),

                new Position(5, 8),
                new Position(4, 8),
                new Position(3, 8),
                new Position(2, 8),
                new Position(1, 8),

                new Position(1, 7),
                new Position(2, 7),
                new Position(3, 7),
                new Position(4, 7),
                new Position(5, 7),
                new Position(7, 7),

                new Position(7, 8),
                new Position(7, 9)
        );
    }

    @Test
    @DisplayName("Should provide guard walk from challenge example")
    void shouldProvideGuardWalkFromChallengeExample() {
        var room = roomFromResource("day6-example-input.txt");
        var guard = room.guards().getFirst();

        var legs = guard.walk().legs();

        assertThat(legs).containsExactly(
                new Leg<>(new Position(4, 6), NORTH, 5, hitsObstruction),
                new Leg<>(new Position(4, 1), EAST, 4, hitsObstruction),
                new Leg<>(new Position(8, 1), SOUTH, 5, hitsObstruction),
                new Leg<>(new Position(8, 6), WEST, 6, hitsObstruction),
                new Leg<>(new Position(2, 6), NORTH, 2, hitsObstruction),
                new Leg<>(new Position(2, 4), EAST, 4, hitsObstruction),
                new Leg<>(new Position(6, 4), SOUTH, 4, hitsObstruction),
                new Leg<>(new Position(6, 8), WEST, 5, hitsObstruction),
                new Leg<>(new Position(1, 8), NORTH, 1, hitsObstruction),
                new Leg<>(new Position(1, 7), EAST, 6, hitsObstruction),
                new Leg<>(new Position(7, 7), SOUTH, 2, hitsRoomBorder)
        );
    }

    @Test
    @DisplayName("Should count distinct positions from challenge example")
    void shouldCountDistinctPositionsFromChallengeExample() {
        var room = roomFromResource("day6-example-input.txt");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        assertThat(patrol.distinctPositionsCountVisitedBy(guard)).isEqualTo(41);
    }

    @Test
    @DisplayName("Should count distinct positions from challenge")
    void shouldCountDistinctPositionsFromChallenge() {
        var room = roomFromResource("day6-input.txt");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        assertThat(patrol.distinctPositionsCountVisitedBy(guard)).isEqualTo(4776);
    }

    @ParameterizedTest
    @CsvSource({
            "0, '...#..'",
            "1, '...XXX'",
            "2, '...X..'",
            "3, '......'",
    })
    @DisplayName("Should mark visits in within a line")
    void shouldMarkVisitsInLine(int lineNumber, String expectedLine) {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        assertThat(patrol.visitsInLineByGuard(lineNumber, guard)).isEqualTo(expectedLine);
    }

    @Test
    @DisplayName("Should format visited positions to readable output")
    void shouldFormatVisitedPositions() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        String visits = patrol.visitsInLinesBy(guard);

        assertThat(visits).isEqualTo("""
                ...#..
                ...XXX
                ...X..
                ......""");
    }

    @Test
    @DisplayName("Should format visited positions from challenge example")
    void shouldFormatVisitedPositionsFromChallengeExample() {
        var room = roomFromResource("day6-example-input.txt");

        var patrol = new Patrol(room);
        var guard = room.guards().getFirst();

        String visits = patrol.visitsInLinesBy(guard);

        assertThat(visits).isEqualTo("""
                ....#.....
                ....XXXXX#
                ....X...X.
                ..#.X...X.
                ..XXXXX#X.
                ..X.X.X.X.
                .#XXXXXXX.
                .XXXXXXX#.
                #XXXXXXX..
                ......#X..""");
    }

    Room roomFromResource(String name) {
        Room room = null;

        try (var roomInputStream = Room.class.getResourceAsStream(name)) {
            room = Room.from(new InputStreamReader(roomInputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return room;
    }
}