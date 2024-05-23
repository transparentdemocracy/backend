package be.tr.democracy.inmem;

import java.util.List;
import java.util.function.Supplier;

public class PlenaryDTOFileLoader implements Supplier<List<PlenaryDTO>> {
    private final List<PlenaryDTO> plenaryDTOS;

    public PlenaryDTOFileLoader(String plenariesFileName) {
        final var dataFileLoader = new JSONDataFileLoader();
        this.plenaryDTOS = dataFileLoader.loadPlenaries(plenariesFileName);
    }

    @Override
    public List<PlenaryDTO> get() {
        return plenaryDTOS;
    }
}
