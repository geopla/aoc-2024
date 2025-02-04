package day6;

public sealed interface Lifecycle {
    record Planned() implements Lifecycle { }
    record Computed(int steps, Terminator terminator) implements Lifecycle { }
}

