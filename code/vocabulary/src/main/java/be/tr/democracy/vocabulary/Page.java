package be.tr.democracy.vocabulary;

import java.util.List;

public record Page<T>(int pageNr,
                   int pageSize,
                   int totalPages,
                   List<T> values
) {
    public Page {
        validateInput(pageNr, pageSize, totalPages);
        if (totalPages < pageNr) {
            pageNr = totalPages;
        }
    }

    private void validateInput(int pageNr, int pageSize, int totalPages) {
        if (pageNr < 1) throw new IllegalArgumentException("Page number must be at least 1.");
        if (pageSize < 1) throw new IllegalArgumentException("Page size must be at least 1.");
        if (totalPages < 1) throw new IllegalArgumentException("Total pages must be at least 1.");
    }
}
