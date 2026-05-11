package com.gft.msmap.infraestructure.persistence.jpa;

import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.WarehousePosition;
import com.gft.msmap.domain.WarehouseType;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarehousePositionJpaAdapterTest {

    WarehousePositionJpaRepository repo = mock(WarehousePositionJpaRepository.class);

    WarehousePositionJpaAdapter adapter = new WarehousePositionJpaAdapter(repo);

    @Test
    void shouldMapEntityToDomain() {

        WarehousePositionEntity entity = new WarehousePositionEntity();
        entity.setWarehouseId(UUID.randomUUID());
        entity.setName("w1");
        entity.setXEdge(1);
        entity.setYEdge(2);
        entity.setWarehouseType(WarehouseType.FACTORY);

        when(repo.findAll()).thenReturn(List.of(entity));

        List<WarehousePosition> result = adapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("w1");
    }

    @Test
    void shouldSaveEntity() {

        WarehousePosition domain = new WarehousePosition(
                UUID.randomUUID(),
                "w1",
                new Location(1, 1),
                WarehouseType.FACTORY
        );

        adapter.save(domain);

        verify(repo).save(any());
    }
}
