package com.gft.mssimulation.infrastructure.persistence.jpa.mapstate;

import com.gft.mssimulation.domain.mapstate.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TruckPositionJpaAdapterTest {

    TruckPositionJpaRepository repo = mock(TruckPositionJpaRepository.class);

    TruckPositionJpaAdapter adapter = new TruckPositionJpaAdapter(repo);

    @Test
    void shouldMapEntityToDomain() {

        TruckPositionEntity entity = new TruckPositionEntity();
        entity.setTruckId(UUID.randomUUID());
        entity.setXEdge(1);
        entity.setYEdge(2);

        when(repo.findAll()).thenReturn(List.of(entity));

        List<TruckPosition> result = adapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLocation().getX()).isEqualTo(1);
    }

    @Test
    void shouldSaveEntity() {

        TruckPosition domain = new TruckPosition(
                UUID.randomUUID(),
                new Location(1, 1)
        );

        adapter.save(domain);

        verify(repo).save(any());
    }
}