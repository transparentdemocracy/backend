package be.tr.democracy.inmem;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlenaryMapperTest {
    @Test
    void basicMapping() {
        final var plenaryDTO = DataModelMother.buildPlenary();

        final var plenaries = PlenaryMapper.INSTANCE.mapThem(List.of(plenaryDTO));
        final var plenary = plenaries.getFirst();

        assertThat(plenary.plenaryDate(), is(plenaryDTO.date()));
        assertThat(plenary.id(), is(plenaryDTO.id()));
        assertThat(plenary.title(), is(plenaryDTO.number() + " (L" + plenaryDTO.legislature() + ")"));
        assertThat(plenary.motionsGroups().size(), is(plenaryDTO.motion_groups().size()));
        assertThat(plenary.motionsGroups().getFirst().motionGroupId(), is(plenaryDTO.motion_groups().getFirst().id()));
        assertThat(plenary.motionsGroups().getFirst().motions().getFirst().agendaSeqNr(), is(plenaryDTO.motion_groups().getFirst().motions().getFirst().sequence_number()));
    }
}