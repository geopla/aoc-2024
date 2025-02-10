package day6;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static day6.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class GuardTest {

    Lifecycle.Computed hitsObstruction = new Lifecycle.Computed(Terminator.OBSTRUCTION);

    @Test
    @DisplayName("Should accept a leg starting a guard walk")
    void shouldAcceptAnInitialLeg() {
        var walk = new Guard.Walk();
        var leg = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsObstruction);

        assertThat(walk.add(leg)).isTrue();
        assertThat(walk.legs()).containsExactly(leg);
    }

    @Test
    @DisplayName("Should accept another leg connected to a guard walk")
    void shouldAcceptAnotherConnectedLeg() {
        var walk = new Guard.Walk();

        var startLeg = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsObstruction);
        walk.add(startLeg);

        var connectedLeg = new Leg<>(new Room.Position(0, 2), EAST, 3, hitsObstruction);
        walk.add(connectedLeg);

        assertThat(walk.legs()).containsExactly(
                startLeg,
                connectedLeg
        );
    }

    @Test
    @DisplayName("Should reject another NOT connected leg (for safety reasons)")
    void shouldRejectAnotherNotConnectedLeg() {
        var walk = new Guard.Walk();

        var startLeg = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsObstruction);
        walk.add(startLeg);

        var unconnectedLeg = new Leg<>(new Room.Position(1, 3), EAST, 2, hitsObstruction);

        assertThatIllegalArgumentException().isThrownBy(() ->
                        walk.add(unconnectedLeg)
                )
                .withMessage("end Position[x=0, y=2] not connected to start Position[x=1, y=3]");
    }

    @Test
    @DisplayName("Should ignore a looping leg (without throwing an exception)")
    void shouldIngnoreLoopingLeg() {
        var walk = new Guard.Walk();

        var twoStepsSouth = new Leg<>(new Room.Position(0, 0), SOUTH, 2, hitsObstruction);
        walk.add(twoStepsSouth);

        var twoStepsEast = new Leg<>(new Room.Position(0, 2), EAST, 2, hitsObstruction);
        walk.add(twoStepsEast);

        var twoStepsNorth = new Leg<>(new Room.Position(2, 2), NORTH, 2, hitsObstruction);
        walk.add(twoStepsNorth);

        var twoStepsWest = new Leg<>(new Room.Position(2, 0), WEST, 3, hitsObstruction);
        walk.add(twoStepsWest);

        assertThat(walk.add(twoStepsSouth)).isFalse();
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