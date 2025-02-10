package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Patrol {

    private final Room room;

    Patrol(Room room) {
        this.room = room;
    }

    Stream<Leg<Computed>> walkOf(Guard guard) {
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

    Stream<Room.Position> positionsVisitedBy(Guard guard) {
        var firstElement = 1;

        return Stream.concat(
                Stream.of(guard.startPosition()),
                walkOf(guard).flatMap(leg -> leg.positions().skip(firstElement))
        );
    }

    Stream<Room.Position> distinctPositionsVisitedBy(Guard guard) {
        return positionsVisitedBy(guard).distinct();
    }

    long distinctPositionsCountVisitedBy(Guard guard) {
        return distinctPositionsVisitedBy(guard).count();
    }

    private static CardinalDirection keepDirectionWhenGuardIsStuckOn(Leg<Computed> leg) {
        return leg.direction();
    }

    private Leg<Computed> firstLegOf(Guard guard) {
        var legPlanned = new Leg<>(guard.startPosition(), guard.startFacing(), Leg.stepsUnlimited(), new Planned());

        return room.realize(legPlanned);
    }

    String visitsInLineByGuard(int lineNumber, Guard guard) {
        var obstructionPositions = room.obstructions().stream()
                .map(Room.Obstruction::position)
                .filter(position -> position.y() == lineNumber)
                .toList();

        var visitedPositions = distinctPositionsVisitedBy(guard)
                .filter(position -> position.y() == lineNumber)
                .toList();


        return IntStream.range(0, room.size().width())
                .boxed()
                .map(i -> new Room.Position(i, lineNumber))
                .map(position -> {
                    String marker = ".";

                    if (visitedPositions.contains(position)) {
                        marker = "X";
                    }
                    else if (obstructionPositions.contains(position)) {
                        marker = "#";
                    }

                    return marker;
                })
                .collect(Collectors.joining());
    }


    public String visitsInLinesBy(Guard guard) {
        return IntStream.range(0, room.size().length())
                .boxed()
                .map(i -> visitsInLineByGuard(i, guard))
                .collect(Collectors.joining("\n"));
    }
}
