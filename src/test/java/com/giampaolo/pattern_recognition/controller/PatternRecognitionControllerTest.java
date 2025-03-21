package com.giampaolo.pattern_recognition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giampaolo.pattern_recognition.dto.PointDTO;
import com.giampaolo.pattern_recognition.service.PatternRecognitionService;
import com.giampaolo.pattern_recognition.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.giampaolo.pattern_recognition.controller.PatternRecognitionController.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatternRecognitionController.class)
class PatternRecognitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatternRecognitionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenPoint_whenAddPoint_thenStatusIsOk() throws Exception {
        // Given
        Point point = Utils.randomPoint();

        String pointJson = objectMapper.writeValueAsString(point);

        // When // Then
        mockMvc.perform(post(POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pointJson))
                .andExpect(status().isOk());

        verify(service).addPoint(Mockito.any(PointDTO.class));
    }

    @Test
    void givenInvalidPoint_whenAddPoint_thenStatusIsBadRequest() throws Exception {
        String invalidPointJson = "{}";

        // When // Then
        mockMvc.perform(post(POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPointJson))
                .andExpect(status().isBadRequest());

        verify(service, never()).addPoint(Mockito.any(PointDTO.class));
    }

    @Test
    void givenPoints_whenGetAllPoints_thenReturnsAllPoints() throws Exception {
        // Given
        Set<Point> points = new HashSet<>(Arrays.asList(Utils.randomPoint(), Utils.randomPoint()));

        when(service.getAllPoints()).thenReturn(points);

        // When // Then
        mockMvc.perform(get(SPACE))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Set<Point> responsePoints = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(Set.class, Point.class));
                    assertThat(responsePoints).hasSize(points.size());
                    assertThat(responsePoints).isEqualTo(points);
                });
        verify(service).getAllPoints();
    }

    @Test
    void givenLines_whenGetLines_thenReturnsLines() throws Exception {
        // Given
        Set<Point> line1 = new HashSet<>(Arrays.asList(Utils.randomPoint(), Utils.randomPoint()));
        Set<Point> line2 = new HashSet<>(Arrays.asList(Utils.randomPoint(), Utils.randomPoint()));

        List<Set<Point>> lines = Arrays.asList(line1, line2);

        final int points = 2;
        when(service.getLines(points)).thenReturn(lines);

        final String path = String.format("%s/%d", LINES, points);

        // When // Then
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(result -> {
                    List<Set<Point>> responseLines = objectMapper.readValue(result.getResponse().getContentAsString(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class,
                                    objectMapper.getTypeFactory().constructCollectionType(Set.class, Point.class)));
                    assertThat(responseLines).hasSize(lines.size());
                    assertThat(responseLines).isEqualTo(lines);
                });
        verify(service).getLines(points);
    }

    @Test
    void givenPoints_whenClearSpace_thenStatusIsOk() throws Exception {
        // Given // When
        mockMvc.perform(delete(SPACE))
                .andExpect(status().isOk());
        // Then
        verify(service).clearPoints();
    }
}