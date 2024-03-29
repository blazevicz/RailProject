package org.pl.deenes.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.deenes.infrastructure.repositories.TerrainProfileRepository;
import org.pl.deenes.model.TerrainProfile;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TerrainProfileServiceImplTest {

    @Mock
    private TerrainProfileRepository terrainProfileJpaRepository;

    @InjectMocks
    private TerrainProfileServiceImpl terrainProfileService;

    @Test
    void testCalculateSlope_EmptyList() {
        when(terrainProfileJpaRepository.findAllByLineNumberIsAndKilometerBetween(anyInt(), anyDouble(), anyDouble()))
                .thenReturn(new LinkedList<>());

        List<TerrainProfile> result = terrainProfileService.calculateSlope(1, 0.0, 10.0);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateSlope_SingleElementList() {
        TerrainProfile terrainProfile = TerrainProfile.builder()
                .height(15.5)
                .kilometer(22)
                .lineNumber(2)
                .build();

        LinkedList<TerrainProfile> singleElementList = new LinkedList<>(List.of(terrainProfile));
        when(terrainProfileJpaRepository.findAllByLineNumberIsAndKilometerBetween(anyInt(), anyDouble(), anyDouble()))
                .thenReturn(singleElementList);

        List<TerrainProfile> result = terrainProfileService.calculateSlope(1, 0.0, 10.0);

        assertEquals(singleElementList, result);
        assertEquals(0.0, terrainProfile.getSlope());
    }

    @Test
    void testCalculateSlope_MultipleElementsList() {
        TerrainProfile terrainProfile = TerrainProfile.builder()
                .height(15.5)
                .kilometer(22)
                .lineNumber(2)
                .build();
        TerrainProfile terrainProfile1 = TerrainProfile.builder()
                .height(15.5)
                .kilometer(22)
                .lineNumber(2)
                .build();

        LinkedList<TerrainProfile> terrainProfileLinkedList = new LinkedList<>(List.of(terrainProfile, terrainProfile1));
        when(terrainProfileJpaRepository.findAllByLineNumberIsAndKilometerBetween(anyInt(), anyDouble(), anyDouble()))
                .thenReturn(terrainProfileLinkedList);

        List<TerrainProfile> terrainProfileList = terrainProfileService.calculateSlope(1, 0.0, 10.0);

        assertEquals(terrainProfileLinkedList, terrainProfileList);
        assertEquals(0.0, terrainProfile.getSlope());
        assertEquals(terrainProfile.getSlope(), terrainProfile1.getSlope());
    }
}
