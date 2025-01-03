package day3;

record Multiplier(String x, String y) {

    public int apply() {
        return Integer.parseInt(x) * Integer.parseInt(y);
    }
}
