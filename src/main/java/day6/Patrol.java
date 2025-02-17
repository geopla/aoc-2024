package day6;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Patrol {

    private final Room room;

    Patrol(Room room) {
        this.room = room;
    }

    Stream<Room.Position> distinctPositionsVisitedBy(Guard guard) {
        return guard.walk().positionsVisited().distinct();
    }

    long distinctPositionsCountVisitedBy(Guard guard) {
         return distinctPositionsVisitedBy(guard).count();
    }

    Stream<Room.Position> stuckPlacementsPositions(Guard guard) {
        return distinctPositionsVisitedBy(guard)
                .skip(1)
                .filter(isStuckInLoopByObstructionPlacement(guard));
    }

    long stuckPlacementPositionsCount(Guard guard) {
        return stuckPlacementsPositions(guard).count();
    }

    private static Predicate<Room.Position> isStuckInLoopByObstructionPlacement(Guard guard) {
        return position -> {
            var manipulatedRoom = guard.room().withAdditional(List.of(new Room.Obstruction('#', position)));
            var firstGuard = manipulatedRoom.firstGuard();

            return firstGuard.isRunningInLoop();
        };
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

    String visitsInLinesBy(Guard guard) {
        return IntStream.range(0, room.size().depth())
                .boxed()
                .map(i -> visitsInLineByGuard(i, guard))
                .collect(Collectors.joining("\n"));
    }
}
