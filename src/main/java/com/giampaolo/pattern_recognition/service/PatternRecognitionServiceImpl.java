package com.giampaolo.pattern_recognition.service;

import com.giampaolo.pattern_recognition.dto.PointDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatternRecognitionServiceImpl implements PatternRecognitionService {
    private final Set<Point> points;
    private final Map<String, Set<Point>> lineGroupsMap;

    public PatternRecognitionServiceImpl() {
        points = new HashSet<>();
        lineGroupsMap = new HashMap<>();
    }

    @Override
    public Set<Point> getAllPoints() {
        return points;
    }

    @Override
    public List<Set<Point>> getLines(int n) {
        if (n <= 1) {
            /* If a line containing only one point has no meaning in the context of the problem,
             (since it doesn't really represent a "line" in the geometric sense),
             then return an empty list because there are no lines with a single point. */
            return List.of();
        }

        return lineGroupsMap.values().stream()
                .filter(set -> set.size() >= n)
                .collect(Collectors.toList());
    }

    @Override
    public void clearPoints() {
        points.clear();
        lineGroupsMap.clear();
    }

    @Override
    public boolean addPoint(PointDTO dto) {
        Point newPoint = new Point(dto.x(), dto.y());
        points.forEach(p2 -> {
            String key;
            if (p2.x == newPoint.x) {
                // Handle vertical lines
                key = "VERTICAL_" + p2.x;
            } else {
                // Compute slope and intercept
                // round for minimize floating-point precision
                double slope = Math.round(((double) (p2.y - newPoint.y) / (p2.x - newPoint.x)) * 10000.0) / 10000.0;
                double intercept = Math.round(((newPoint.y - slope * newPoint.x)) * 10000.0) / 10000.0;

                //double intercept = newPoint.y - slope * newPoint.x;
                key = slope + "_" + intercept;
            }
            // Add new points to the corresponding line group
            lineGroupsMap.computeIfAbsent(key, k -> new HashSet<>())
                    .add(newPoint);
            lineGroupsMap.get(key).add(p2);
        });
        return points.add(newPoint);
    }
}