package be.tr.democracy.inmem;

import java.util.Comparator;

enum PlenaryComparator implements Comparator<PlenaryDTO> {
    INSTANCE;

    @Override
    public int compare(PlenaryDTO o1, PlenaryDTO o2) {
        return StringDateComparator.INSTANCE.compare(o1.date(), o2.date());
    }
}
