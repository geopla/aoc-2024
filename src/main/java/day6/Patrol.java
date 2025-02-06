package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;

import java.util.stream.Stream;

class Patrol {

    private final Room room;

    Patrol(Room room) {
        this.room = room;
    }

    Stream<Leg<Computed>> walkOfGuard(int number) {
        Guard guard = room.guards().get(number);
        Leg<Computed> firstLeg = firstLegOf(guard);

        return Stream.iterate(
                firstLeg,
                computedLeg -> computedLeg.steps() != 0,
                leg -> {
                    var start = leg.end();
                    var direction = guard.turnStrategy()
                            .changeDirectionOn(leg)
                            .orElse(keepDirectionWhenGuardIsStuckOn(leg));

                    return room.realize(new Leg<>(start, direction, Leg.stepsUnlimited(), new Planned()));
                });
    }

    Stream<Room.Position> positionsVisitedByGuard(int number) {
        var guard = room.guards().get(number);
        var firstElement = 1;

        return Stream.concat(
                Stream.of(guard.startPosition()),
                walkOfGuard(number)
                    .flatMap(leg -> leg.positions().skip(firstElement))
        );
    }

    private static CardinalDirection keepDirectionWhenGuardIsStuckOn(Leg<Computed> leg) {
        return leg.direction();
    }

    private Leg<Computed> firstLegOf(Guard guard) {
        var legPlanned = new Leg<>(guard.startPosition(), guard.startFacing(), Leg.stepsUnlimited(), new Planned());

        return room.realize(legPlanned);
    }
}
