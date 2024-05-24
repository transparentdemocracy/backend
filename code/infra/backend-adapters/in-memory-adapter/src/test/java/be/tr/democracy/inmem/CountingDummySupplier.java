package be.tr.democracy.inmem;

import java.util.List;
import java.util.function.Supplier;

class CountingDummySupplier implements Supplier<List<PlenaryDTO>> {
    private final List<PlenaryDTO> plenaryDTOS = DataModelMother.buildPlenaries();
    private int counter = 0;

    @Override
    public List<PlenaryDTO> get() {
        counter++;
        return plenaryDTOS;
    }

    public int nrOfInvocations() {
        return counter;
    }

    public List<PlenaryDTO> getSuppliedPlenaryDTOS() {
        return plenaryDTOS;
    }
}
