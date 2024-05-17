package be.tr.democracy.vocabulary;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static <T> Page<T> slicePageFromList(PageRequest pageRequest, List<T> list) {
        final var size = pageRequest.pageSize();
        int totalPages = (int) Math.ceil((double) list.size() / size);
        int requestedPage = Math.max(1, Math.min(totalPages, pageRequest.pageNr()));
        int startIndex = (requestedPage - 1) * size;
        final var pageResult = list.subList(startIndex, Math.min(startIndex + size, list.size()));
        return new Page<>(requestedPage, size, totalPages, pageResult);
    }

    public Page<T> sortedPage(Comparator<T> comparator) {
        return new Page(pageNr, pageSize, totalPages, values.stream().sorted(comparator).toList());
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        final var list = values.stream().map(mapper::apply).toList();
        return new Page<>(pageNr, pageSize, totalPages, list);
    }

    private void validateInput(int pageNr, int pageSize, int totalPages) {
        if (pageNr < 1) throw new IllegalArgumentException("Page number must be at least 1.");
        if (pageSize < 1) throw new IllegalArgumentException("Page size must be at least 1.");
        if (totalPages < 1) throw new IllegalArgumentException("Total pages must be at least 1.");
    }


}
