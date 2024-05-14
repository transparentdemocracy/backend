package be.tr.democracy.rest;

import java.util.List;

public record PageViewDTO<T>(int pageNr,
                             int pageSize,
                             int totalPages,
                             List<T> values
) {
}
