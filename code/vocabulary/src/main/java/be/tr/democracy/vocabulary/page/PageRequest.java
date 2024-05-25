package be.tr.democracy.vocabulary.page;

public record PageRequest(
        int pageNr,
        int pageSize
) {
    public PageRequest {
        validatePositive(pageNr);
        validatePositive(pageSize);
    }

    private void validatePositive(int number) {
        if (number < 1) throw new IllegalArgumentException("Page number must be greater than 0");
    }
}
