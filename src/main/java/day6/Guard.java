package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;
import day6.Room.Position;

import java.util.LinkedHashSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static day6.Terminator.BORDER;

class Guard {

    static class Walk {
        private final LinkedHashSet<Leg<Computed>> legs;

        private final Guard guard;

        Walk(Guard guard) {
            this.guard = guard;
            this.legs = new LinkedHashSet<>();
        }

        boolean add(Leg<Computed> leg) {
            tryValidateConnectivity(leg);
            return legs.add(leg);
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

    Walk walk() {
        // TODO compute walk dependent on the room layout including historian added obstructions
        // Step -, copy implementation from Patrol
        // Step 2, integrate looping paths
        // Step 3, extend room to accept historian introduced obstacles too
        // Step -, delete walk construction from Patrol

        Leg<Computed> firstLeg = firstLeg();

        Stream.iterate(firstLeg,
                        hasNextLeg(),  // TODO extend for loops: 'and legs end is NOT a border'
                        leg -> {
                            var start = leg.end();
                            var direction = turnStrategy
                                    .changeDirectionOn(leg)
                                    .orElse(keepDirectionWhenGuardIsStuckOn(leg));

                            return room.realize(new Leg<>(start, direction, Leg.stepsUnlimited(), new Planned()));
                        })
                .forEach(walk::add);

        return walk;
    }

    private Predicate<Leg<Computed>> hasNextLeg() {

        return computedLeg -> {
            // safety catch, limited number of legs because of possible loops
            if (walk.legs.size() >= legsLimitMax) {
                return false;
            }

            if (computedLeg.steps() == 0) {
                if (computedLeg.state().terminator() == BORDER) {
                    return false;
                }
            }

            // TODO loop entered

            return true;
        };
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
