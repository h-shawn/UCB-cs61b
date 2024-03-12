public class OffByN implements CharacterComparator {
    private final int N;
    public OffByN(int N0) {
        N = N0;
    }
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
