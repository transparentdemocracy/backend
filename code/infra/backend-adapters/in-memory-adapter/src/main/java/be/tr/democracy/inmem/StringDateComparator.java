package be.tr.democracy.inmem;

import java.time.LocalDate;
import java.util.Comparator;

enum StringDateComparator implements Comparator<String> {
    INSTANCE;

    @Override
    public int compare(String o1, String o2) {
        //Expected date format to be YYYY-MM-DD : "2021-09-23"
        final var first = LocalDate.parse(o1);
        final var second = LocalDate.parse(o2);
        return second.compareTo(first);
    }
}
