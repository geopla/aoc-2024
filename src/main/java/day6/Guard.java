package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;
import day6.Room.Position;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;

class Guard {

    static class Walk {

        private final LinkedHashSet<Leg<Computed>> legs;
        private final Guard guard;

        Walk(Guard guard) {
            this.guard = guard;
            this.legs = new LinkedHashSet<>();
        }

        boolean add(Leg<Computed> leg) {
            // mainly useful for unit test but may increase confidence ;-)
            tryValidateConnectivity(leg);
            return legs.add(leg);
        }

        void clear() {
            legs.clear();
        }

        Stream<Leg<Computed>> legs() {
            return legs.stream();
        }

        Stream<Position> positionsVisited() {
            var firstElement = 1;

            return Stream.concat(
                    Stream.of(guard.startPosition),
                    legs.stream()
                            .flatMap(leg -> leg.positions().skip(firstElement))
            );
        }

        boolean endsInLoop() {
            if (isNotEmptyWalk()) {
                var lastLeg = legs.getLast();

                // walk calculation stops on obstruction when a loop is detected
                if (lastLeg.state().terminator() == OBSTRUCTION) {
                    return true;
                }
            }

            return false;
        }

        private void tryValidateConnectivity(Leg<Computed> leg) {
            if (isNotEmptyWalk() && isNotPartOfWalk(leg)) {
                var lastLegEnd = legs.getLast().end();
                var newLegStart = leg.start();

                if (!newLegStart.equals(lastLegEnd)) {
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

    private final Position startPosition;
    private final CardinalDirection startFacing;
    private final TurnStrategy turnStrategy;

    private int legsLimitMax = Integer.MAX_VALUE;
    private final Walk walk;

    Guard(Room room, Position position, char facing) {
        this.room = room;
        this.startPosition = position;
        this.startFacing = CardinalDirection.from(facing);

        // may be configurable in future
        turnStrategy = new TurnRight();
        walk = new Walk(this);

        if (room == null) {
            throw new IllegalArgumentException("guard must be assigned to a room");
        }
        if (startPosition == null) {
            throw new IllegalArgumentException("guard must be placed on a start position");
        }
    }

    public boolean isRunningInLoop() {
        return walk().endsInLoop();

    }

    Walk walk() {
        // TODO while loop instead of streaming doesn't look good to me ...

        Leg<Computed> currentLeg = firstLeg();
        walk.clear();
        walk.add(currentLeg);

        var noLoopDetected = true;

        while (hasNext(currentLeg) && noLoopDetected) {
            currentLeg = next(currentLeg);
            noLoopDetected = walk.add(currentLeg);
        }

        return walk;
    }

    private boolean hasNext(Leg<Computed> leg) {
            // safety catch, limited number of legs because of possible loops
            if (walk.legs.size() >= legsLimitMax) {
                return false;
            }
            return nextNotLeavingRoom(leg).isPresent();
    }

    private Optional<Leg<Computed>> nextNotLeavingRoom(Leg<Computed> leg) {
        var nextLeg = next(leg);

        if (nextLeg.steps() == 0 && nextLeg.state().terminator() == BORDER) {
            return Optional.empty();
        }
        else {
            return Optional.of(nextLeg);
        }
    }

    private Leg<Computed> next(Leg<Computed> leg) {
        var start = leg.end();
        var direction = turnStrategy
                .changeDirectionOn(leg)
                .orElse(keepDirectionWhenGuardIsStuckOn(leg));

        return room.realize(new Leg<>(start, direction, Leg.stepsUnlimited(), new Planned()));
    }

    private Leg<Computed> firstLeg() {
        return room.realize(new Leg<>(startPosition, startFacing, Leg.stepsUnlimited(), new Planned()));
    }

    private static CardinalDirection keepDirectionWhenGuardIsStuckOn(Leg<Computed> leg) {
        return leg.direction();
    }

    Room room() {
        return room;
    }

    Position startPosition() {
        return startPosition;
    }

    CardinalDirection startFacing() {
        return startFacing;
    }

    TurnStrategy turnStrategy() {
        return turnStrategy;
    }

    void legsLimit(int max) {
        legsLimitMax = max;
    }
}
