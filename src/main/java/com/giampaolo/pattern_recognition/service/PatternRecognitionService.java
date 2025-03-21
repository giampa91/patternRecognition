package com.giampaolo.pattern_recognition.service;

import com.giampaolo.pattern_recognition.dto.PointDTO;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * This interface defines the contract for a service that handles pattern recognition for points in a 2D plane.
 * It includes operations for adding points, retrieving points, identifying lines that contain a minimum number of points,
 * and clearing all stored points.
 */
public interface PatternRecognitionService {

    /**
     * Retrieves all points currently stored in the service.
     *
     * @return a Set of all points stored in the service.
     */
    Set<Point> getAllPoints();

    /**
     * Retrieves all lines containing at least 'n' points. The lines are represented as sets of collinear points.
     *
     * @param n the minimum number of points that a line must contain to be included in the result.
     * @return a List of sets, where each set represents a group of points that form a line.
     */
    List<Set<Point>> getLines(int n);

    /**
     * Clears all points stored in the service.
     */
    void clearPoints();

    /**
     * Adds a new point to the service and updates the lines that the point may belong to.
     *
     * @param dto the new point to be added to the service.
     */
    boolean addPoint(PointDTO dto);
}