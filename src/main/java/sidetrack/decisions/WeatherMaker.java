package sidetrack.decisions;

class WeatherMaker {

    // Just a silly example of how a decision can be decoupled from its execution

    static String execute(Decision decision) {
        return switch (decision) {
            case Decision.BeingJustMurky beingJustMurky -> beingJustMurky.mood();
            case Decision.LetTheSunShine letTheSunShine -> letTheSunShine.mood();
            case Decision.MakeItRain makeItRain -> makeItRain.mood();
        };
    }

}
