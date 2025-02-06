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
        final Guard guard = room.guards().get(number);
        final Leg<Computed> legSeed = firstLegOf(guard);

        return Stream.iterate(legSeed,
                leg -> {
                    var steps = Integer.MAX_VALUE;
                    var planned = new Planned();
                    var start = leg.end();
                    var direction = guard.turnStrategy()
                            .changeDirectionOn(leg.state().terminator(), leg.direction())
                            .orElse(leg.direction());
                    var legPlanned = new Leg<>(start, direction, steps, planned);

                    return room.realize(legPlanned);
                })
                .takeWhile(computedLeg -> computedLeg.steps() != 0);
    }

    private Leg<Computed> firstLegOf(Guard guard) {
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();
        var legPlanned = new Leg<>(guard.startPosition(), guard.startFacing(), steps, planned);

        return room.realize(legPlanned);
    }


}
