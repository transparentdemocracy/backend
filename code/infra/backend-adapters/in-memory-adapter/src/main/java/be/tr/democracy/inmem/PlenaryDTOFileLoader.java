package be.tr.democracy.inmem;

import java.util.List;
import java.util.function.Supplier;

public class PlenaryDTOFileLoader implements Supplier<List<PlenaryDTO>> {
    private final String plenariesFileName;
    private List<PlenaryDTO> plenaryDTOS;

    public PlenaryDTOFileLoader(String plenariesFileName) {
        this.plenariesFileName = plenariesFileName;
    }

    @Override
    public synchronized List<PlenaryDTO> get() {
        if (plenaryDTOS == null) {
            this.plenaryDTOS = load();
        }
        return plenaryDTOS;
    }

    private List<PlenaryDTO> load() {
        final var dataFileLoader = new JSONDataFileLoader();
        return dataFileLoader.loadPlenaries(plenariesFileName);
    }

}
