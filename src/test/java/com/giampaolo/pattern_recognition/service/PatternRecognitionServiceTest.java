package com.giampaolo.pattern_recognition.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giampaolo.pattern_recognition.configuration.PatternRecognitionServiceTestConfiguration;
import com.giampaolo.pattern_recognition.dto.PointDTO;
import com.giampaolo.pattern_recognition.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(PatternRecognitionServiceTestConfiguration.class)
class PatternRecognitionServiceTest {

    public static final String TEST_CASES_JSON = "test_cases.json";

    @Autowired
    private PatternRecognitionService service;

    @AfterEach
    void tearDown() {
        service.clearPoints();
    }

    static Stream<Arguments> inputProvider() throws IOException {
        File file = new File(Objects.requireNonNull(PatternRecognitionServiceTest.class.getClassLoader().getResource(TEST_CASES_JSON)).getFile());

        List<TestCase> testCases = List.of(new ObjectMapper().readValue(file, TestCase[].class));

        return testCases.stream().map(testCase ->
                Arguments.of(
                        testCase.pointsNumber,
                        testCase.points,
                        testCase.expectedLineCount,
                        testCase.expectedPointCount
                )
        );
    }

    @ParameterizedTest
    @MethodSource("inputProvider")
    void givenPointsAndThreshold_whenCalculatingLines_thenReturnExpectedLineCountAndPointCount(int pointsNumber, List<Point> points, int expectedLineCount, int expectedPointCount) {
        points.forEach(point -> {
            PointDTO dto = new PointDTO(point.x, point.y);
            service.addPoint(dto);
        });

        List<Set<Point>> lines = service.getLines(pointsNumber);

        assertEquals(expectedLineCount, lines.size());  // Assert the number of lines
        assertEquals(expectedPointCount, Optional.of(lines)
                .filter(l -> !l.isEmpty())
                .map(l -> l.getFirst().size())
                .orElse(0));
    }

    @Test
    void givenPoints_whenGettingAllPoints_thenReturnAllPoints() {
        // Given
        PointDTO point1 = Utils.randomPointDto();
        PointDTO point2 = Utils.randomPointDto();
        PointDTO point3 = Utils.randomPointDto();

        service.addPoint(point1);
        service.addPoint(point2);
        service.addPoint(point3);

        // When
        Set<Point> allPoints = service.getAllPoints();

        assertThat(allPoints).hasSize(3);

        assertThat(allPoints).hasSize(3);
        assertThat(allPoints).containsExactlyInAnyOrderElementsOf(Stream.of(point1, point2, point3)
                .map(dto -> new Point(dto.x(), dto.y())).toList());
    }

    static class TestCase {
        public int pointsNumber;
        public List<Point> points;
        public int expectedLineCount;
        public int expectedPointCount;
    }

}