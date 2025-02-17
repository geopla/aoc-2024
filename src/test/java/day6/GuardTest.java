package day6;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class GuardTest {

    Lifecycle.Computed hitsObstruction = new Lifecycle.Computed(Terminator.OBSTRUCTION);
    Lifecycle.Computed hitsRoomBorder = new Lifecycle.Computed(Terminator.BORDER);

    @Test
    @DisplayName("Should walk straight to border in empty room")
    void shouldWalkStraightToBorderInEmptyRoom() {
        var room = Room.from("""
                ......
                ......
                ...^..
                ......""");

        var guard = room.firstGuard();

        assertThat(guard.walk().legs()).containsExactly(
                new Leg<>(new Room.Position(3, 2), NORTH, 2, hitsRoomBorder)
        );
    }

    @Test
    @DisplayName("Should provide position sequence of walk straight to border in empty room")
    void shouldProvidePositionsOfWalkStraightToBorderInEmptyRoom() {
        var room = Room.from("""
                ......
                ......
                ...^..
                ......""");

        var guard = room.firstGuard();

        assertThat(guard.walk().positionsVisited()).containsExactly(
                new Room.Position(3, 2),
                new Room.Position(3, 1),
                new Room.Position(3, 0)
        );
    }

    @Test
    @DisplayName("Should walk with a SINGLE obstruction on path")
    void shouldWalkAvoidingSingleObstruction() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var guard = room.firstGuard();

        assertThat(guard.walk().legs()).containsExactly(
                new Leg<>(new Room.Position(3, 2), NORTH, 1, hitsObstruction),
                new Leg<>(new Room.Position(3, 1), EAST, 2, hitsRoomBorder)
        );
    }

    @Test
    @DisplayName("Should provide position sequence of walk with a SINGLE obstruction on the path")
    void shouldProvidePositionsOfWalkWithSingleObstructionOnPath() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var guard = room.firstGuard();

        assertThat(guard.walk().positionsVisited()).containsExactly(
                // leg 1
                new Room.Position(3, 2),
                new Room.Position(3, 1),
                // leg 2
                new Room.Position(4, 1),
                new Room.Position(5, 1)
        );
    }

    @Test
    @DisplayName("Should walk with MULTIPLE obstructions on the path")
    void shouldWalkWithMultipleObstructionsOnPath() {
        var room = Room.from("""
                ...#..
                .....#
                #..^..
                ....#.""");

        var guard = room.firstGuard();

        assertThat(guard.walk().legs()).containsExactly(
                new Leg<>(new Room.Position(3, 2), NORTH, 1, hitsObstruction),
                new Leg<>(new Room.Position(3, 1), EAST, 1, hitsObstruction),
                new Leg<>(new Room.Position(4, 1), SOUTH, 1, hitsObstruction),
                new Leg<>(new Room.Position(4, 2), WEST, 3, hitsObstruction),
                new Leg<>(new Room.Position(1, 2), NORTH, 2, hitsRoomBorder)
        );
    }

    @Test
    @DisplayName("Should provide position sequence of walk with MULTIPLE obstructions on the path")
    void shouldProvidePositionsOfWalkWithMultipleObstructions() {
        var room = Room.from("""
                ...#..
                .....#
                #..^..
                ....#.""");

        var guard = room.firstGuard();

        assertThat(guard.walk().positionsVisited()).containsExactly(
                // leg 0 - north
                new Room.Position(3, 2),
                new Room.Position(3, 1),
                // leg 1 - east
                new Room.Position(4, 1),
                // leg 2 - south
                new Room.Position(4, 2),
                // leg 3 - west
                new Room.Position(3, 2),
                new Room.Position(2, 2),
                new Room.Position(1, 2),
                // leg 5 - north
                new Room.Position(1, 1),
                new Room.Position(1, 0)
        );
    }

    @Test
    @DisplayName("Should obey limit of maximum number of legs in a guard walk")
    void shouldObeyLegsLimitMax() {
        var room = Room.from("""
                ...#..
                .....#
                #..^..
                ....#.""");

        var guard = room.firstGuard();
        guard.legsLimit(2);

        assertThat(guard.walk().legs()).containsExactly(
                new Leg<>(new Room.Position(3, 2), NORTH, 1, hitsObstruction),
                new Leg<>(new Room.Position(3, 1), EAST, 1, hitsObstruction)
        );
    }

    @Test
    @DisplayName("Should TERMINATE walk with obstructions on path causing a loop")
    void shouldWalkWithObstructionsOnPathCausingLoop() {
        var room = Room.from("""
                ####.
                >...#
                .....
                .#...
                ...#.
                """);

        var legsUntilEnteringLoop = 4;
        var firstPartialLegInLoop = 1;
        var firstFullLengthLegInLoop = 1;

        var guard = room.firstGuard();
        guard.legsLimit(legsUntilEnteringLoop + firstPartialLegInLoop + firstFullLengthLegInLoop);

        assertThat(guard.walk().legs()).containsExactly(
                new Leg<>(new Room.Position(0, 1), EAST,  3, hitsObstruction),
                new Leg<>(new Room.Position(3, 1), SOUTH, 2, hitsObstruction),
                new Leg<>(new Room.Position(3, 3), WEST,  1, hitsObstruction),
                new Leg<>(new Room.Position(2, 3), NORTH, 2, hitsObstruction),

                // partial leg is in because loop detection needs repeated full depth leg
                new Leg<>(new Room.Position(2, 1), EAST,  1, hitsObstruction)
        );
    }

    @Test
    @DisplayName("Should force loop by placing a printing press next to guards start position")
    void shouldForceGuardIntoLoopByPlacingPrintingPress() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ....|..#|.
                ....|...|.
                .#.O^---+.
                ........#.
                #.........
                ......#...
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(3, 6);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should force loop by placing a stack of failed suit prototypes in the bottom right quadrant")
    void shouldForceGuardIntoLoopByPlacingStackOfFailedSuitPrototypes() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ..+-+-+#|.
                ..|.|.|.|.
                .#+-^-+-+.
                ......O.#.
                #.........
                ......#...
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(6, 7);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should force loop by placing a crate of chimney-squeeze prototype fabric next to the standing desk")
    void shouldForceGuardIntoLoopByPlacingCrateOfChimneySqueezePrototypeFabric() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ..+-+-+#|.
                ..|.|.|.|.
                .#+-^-+-+.
                .+----+O#.
                #+----+...
                ......#...
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(7, 7);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should force loop by placing an alchemical retroencabulator near the bottom left")
    void shouldForceGuardIntoLoopByPlacingAlchemicalRetroencabulator() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ..+-+-+#|.
                ..|.|.|.|.
                .#+-^-+-+.
                ..|...|.#.
                #O+---+...
                ......#...
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(1, 8);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should force loop by placing the alchemical retroencabulator a bit to the right")
    void shouldForceGuardIntoLoopByPlacingAlchemicalRetroencabulatorABitToTheRight() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ..+-+-+#|.
                ..|.|.|.|.
                .#+-^-+-+.
                ....|.|.#.
                #..O+-+...
                ......#...
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(3, 8);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should force loop by placing a tank of sovereign glue right next to the tank of universal solvent")
    void shouldForceGuardIntoLoopByPlacingTankOfSovereignGlue() {
        var originalRoom = Room.from("""
                ....#.....
                ....+---+#
                ....|...|.
                ..#.|...|.
                ..+-+-+#|.
                ..|.|.|.|.
                .#+-^-+-+.
                .+----++#.
                #+----++..
                ......#O..
                """);

        var guardInOriginalRoom = originalRoom.firstGuard();
        assertThat(guardInOriginalRoom.isRunningInLoop())
                .withFailMessage("expecting guard NOT to run into a loop")
                .isFalse();

        var historianPlacedObstruction = new Room.Position(7, 9);
        var manipulatedRoom = originalRoom.withAdditional(List.of(new Room.Obstruction('#', historianPlacedObstruction)));
        var guardInManipulatedRoom = manipulatedRoom.firstGuard();

        assertThat(guardInManipulatedRoom.isRunningInLoop())
                .withFailMessage("expecting guard to run into a loop")
                .isTrue();
    }

    @Test
    @DisplayName("Should recognize a loop walk")
    void shouldRecognizeLoopWalk() {
        var room = Room.from("""
                ####.
                >...#
                .....
                .#...
                ...#.
                """);

        var guard = room.firstGuard();

        assertThat(guard.isRunningInLoop()).isTrue();
    }

    @Test
    @DisplayName("Should not recognize a loop when leaving the room is possible")
    void shouldNotRecognizeLoop() {
        var room = Room.from("""
                ...#..
                ......
                ...^..
                ......""");

        var guard = room.firstGuard();

        assertThat(guard.isRunningInLoop()).isFalse();
    }

    @Test
    @DisplayName("Should accept a leg starting a guard walk")
    void shouldAcceptAnInitialLeg() {
        var room = Room.from("""
                v..
                ...
                ...""");

        var guard = room.firstGuard();
        var walk = new Guard.Walk(guard);
        var leg = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsRoomBorder);

        assertThat(walk.add(leg)).isTrue();
        assertThat(walk.legs()).containsExactly(leg);
    }

    @Test
    @DisplayName("Should accept another leg connected to a guard walk")
    void shouldAcceptAnotherConnectedLeg() {
        var room = Room.from("""
                v..
                ...
                #..""");

        var guard = room.firstGuard();
        var walk = new Guard.Walk(guard);

        var startLeg = new Leg<>(new Room.Position(0, 0), SOUTH, 1, hitsObstruction);
        walk.add(startLeg);

        var connectedLeg = new Leg<>(new Room.Position(0, 1), EAST, 2, hitsRoomBorder);
        walk.add(connectedLeg);

        assertThat(walk.legs()).containsExactly(
                startLeg,
                connectedLeg
        );
    }

    @Test
    @DisplayName("Should reject another NOT connected leg (for safety reasons)")
    void shouldRejectAnotherNotConnectedLeg() {
        var room = Room.from("""
                v..
                ...
                ...""");

        var guard = room.firstGuard();
        var walk = new Guard.Walk(guard);

        var startLeg = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsObstruction);
        walk.add(startLeg);

        var unconnectedLeg = new Leg<>(new Room.Position(1, 1), EAST, 1, hitsRoomBorder);

        assertThatIllegalArgumentException().isThrownBy(() ->
                        walk.add(unconnectedLeg)
                )
                .withMessage("end Position[x=0, y=2] not connected to start Position[x=1, y=1]");
    }

    @Test
    @DisplayName("Should add a (full) looping leg to the walk")
    void shouldIgnoreLoopingLeg() {
        var room = Room.from("""
                ####.
                >...#
                .....
                .#...
                ...#.
                """);

        var guard = room.firstGuard();
        var walk = new Guard.Walk(guard);

        var toEast = new Leg<>(new Room.Position(0, 1), EAST, 3, hitsObstruction);
        var toSouth = new Leg<>(new Room.Position(3, 1), SOUTH, 2, hitsObstruction);
        var toWest = new Leg<>(new Room.Position(3, 3), WEST, 1, hitsObstruction);
        var toNorth = new Leg<>(new Room.Position(2, 3), NORTH, 2, hitsObstruction);

        var toEastAgainPartial = new Leg<>(new Room.Position(2, 1), EAST, 1, hitsObstruction);
        var toSouthAgainComplete = new Leg<>(new Room.Position(3, 1), SOUTH, 2, hitsObstruction);

        assertThat(walk.add(toEast)).isTrue();
        assertThat(walk.add(toSouth)).isTrue();
        assertThat(walk.add(toWest)).isTrue();
        assertThat(walk.add(toNorth)).isTrue();

        assertThat(walk.add(toEastAgainPartial)).isTrue();
        assertThat(walk.add(toSouthAgainComplete)).isFalse();
    }

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