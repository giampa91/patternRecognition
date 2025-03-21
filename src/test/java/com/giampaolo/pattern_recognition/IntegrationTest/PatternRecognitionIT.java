package com.giampaolo.pattern_recognition.IntegrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Set;

import static com.giampaolo.pattern_recognition.controller.PatternRecognitionController.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatternRecognitionIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int POINTS_NUMBER = 3;
    private static final int EXPECTED_LINE_COUNT = 2;
    private static final int EXPECTED_POINT_COUNT = 3;
    private static final Set<Point> points = Set.of(
            new Point(5, 5),
            new Point(6, 6),
            new Point(5, 1),
            new Point(3, 3),
            new Point(4, 2)
    );

    @Test
    @Order(1)
    void shouldAddPoint() {
        // When // Then
        points.forEach(point -> {
            try {
                mvc.perform(post(POINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(point)))
                        .andExpect(status().isOk());
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException("Failed to convert point to JSON: " + point, e);
            } catch (Exception e) {
                throw new IllegalStateException("MockMvc request failed for point: " + point, e);
            }
        });
    }

    @Test
    @Order(2)
    void shouldGetAllPoints() throws Exception {
        // When // Then
        mvc.perform(get(SPACE))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Set<Point> responsePoints = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(Set.class, Point.class));
                    assertThat(responsePoints).hasSize(points.size());
                    assertThat(responsePoints).isEqualTo(points);
                });
    }

    @Test
    @Order(3)
    void shouldGetLines() throws Exception {
        final String path = String.format("%s/%d", LINES, POINTS_NUMBER);

        // When // Then
        mvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(result -> {
                    List<Set<Point>> responseLines = objectMapper.readValue(result.getResponse().getContentAsString(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class,
                                    objectMapper.getTypeFactory().constructCollectionType(Set.class, Point.class)));
                    assertThat(responseLines).hasSize(EXPECTED_LINE_COUNT);
                    int maxSetSize = responseLines.stream()
                            .mapToInt(Set::size)
                            .max()
                            .orElse(0);
                    assertThat(maxSetSize).isEqualTo(EXPECTED_POINT_COUNT);
                });
    }

    @Test
    @Order(4)
    void shouldClearPoints() throws Exception {
        // When // Then
        mvc.perform(delete(SPACE))
                .andExpect(status().isOk());
    }
}
