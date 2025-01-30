package day6;

class Room {

    record Size(int width, int length) { }

    private final Size size;

    public Room(String roomMap) {
        size = new Size(0, 0);
    }

    public Size size() {
        return size;
    }


}
