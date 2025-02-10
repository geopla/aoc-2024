package day6;

import day6.Lifecycle.Computed;

import java.util.LinkedHashSet;
import java.util.stream.Stream;

class Guard {

    static class Walk {
        private final LinkedHashSet<Leg<Computed>> legs = new LinkedHashSet<>();

        boolean add(Leg<Computed> leg) {
            tryValidateConnectivity(leg);

            return legs.add(leg);
        }

        Stream<Leg<Computed>> legs() {
            return legs.stream();
        }

        private void tryValidateConnectivity(Leg<Computed> leg) {
            if (isNotEmptyWalk() && isNotPartOfWalk(leg)) {
                var lastLegEnd = legs.getLast().end();
                var newLegStart = leg.start();

                if (! newLegStart.equals(lastLegEnd)) {
                    throw new IllegalArgumentException("end %s not connected to start %s".formatted(lastLegEnd, newLegStart));
                }
            }
        }

        private boolean isNotEmptyWalk() {
            return !legs.isEmpty();
        }
        private boolean isNotPartOfWalk(Leg<Computed> leg) {
            return !legs.contains(leg);
        }
    }

    private final Room room;
    private final Room.Position startPosition;
    private final CardinalDirection startFacing;

    private final TurnStrategy turnStrategy;


    Guard(Room room, Room.Position position, char facing) {
        this.room = room;
        this.startPosition = position;
        this.startFacing = CardinalDirection.from(facing);

        // may be configurable in future
        turnStrategy = new TurnRight();

        if (room == null) {
            throw new IllegalArgumentException("guard must be assigned to a room");
        }
        if (startPosition == null) {
            throw new IllegalArgumentException("guard must be placed on a start position");
        }
    }

    Walk walk() {
        // TODO compute walk dependent on the room layout including historian added obstructions
        // Step 1, copy implementation from Patrol
        // Step 2, integrate looping paths
        // Step 3, extend room to accept historian introduced obstacles too
        return null;
    }

    Room room() {
        return room;
    }

    Room.Position startPosition() {
        return startPosition;
    }

    CardinalDirection startFacing() {
        return startFacing;
    }

    TurnStrategy turnStrategy() {
        return turnStrategy;
    }
}
