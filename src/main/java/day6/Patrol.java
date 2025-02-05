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
        var guard = room.guards().get(number);
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        var legPlanned = new Leg<>(guard.startPosition(), guard.startFacing(), steps, planned);

        return Stream.of(room.realize(legPlanned));
    }
}
