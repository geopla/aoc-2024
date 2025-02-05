package day6;

record Leg<State extends Lifecycle>(
        Room.Position start,
        CardinalDirection direction,
        int steps,
        State state) {


    // TODO Doesn't look good, how to deal with 'empty' states and defaults?
//    Leg(Room.Position start, CardinalDirection direction) {
//        this(start, direction, Integer.MAX_VALUE, (State) new Planned());
//    }
}

