package sidetrack.decisions;

sealed interface Decision {
    record LetTheSunShine(String mood) implements Decision{ };
    record MakeItRain(String mood) implements Decision { };
    record BeingJustMurky(String mood) implements Decision { };
}
