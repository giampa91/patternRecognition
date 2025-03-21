package com.giampaolo.pattern_recognition.controller;

import com.giampaolo.pattern_recognition.dto.PointDTO;
import com.giampaolo.pattern_recognition.service.PatternRecognitionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * This controller provides REST endpoints for managing points in a space and identifying lines
 * that contain a specific number of points. The controller interacts with the PatternRecognitionServiceImpl
 * to add points, retrieve all points, and identify lines.
 */
@RestController
public class PatternRecognitionController {

    public static final String POINT = "/point";
    public static final String SPACE = "/space";
    public static final String LINES = "/lines";

    @Autowired
    private PatternRecognitionService service;

    /**
     * Adds a new point to the space.
     *
     * @param pointDTO the point to be added, cannot be null.
     * @return ResponseEntity with status 200 OK if the point was successfully added.
     */
    @PostMapping(POINT)
    public ResponseEntity<Boolean> addPoint(@Valid @NotNull @RequestBody PointDTO pointDTO) {
        return service.addPoint(pointDTO) ? ResponseEntity.status(HttpStatus.CREATED).body(true)
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(false);
    }


    /**
     * Retrieves all points currently stored in the space.
     *
     * @return ResponseEntity containing a set of all points in the space.
     */
    @GetMapping(SPACE)
    public ResponseEntity<Set<Point>> getAllPoints() {
        return ResponseEntity.ok(service.getAllPoints());
    }

    /**
     * Retrieves all lines containing at least 'n' points. The lines are returned as groups of collinear points.
     *
     * @param n the minimum number of points a line must contain to be included in the response.
     * @return ResponseEntity containing a list of sets, where each set represents a group of points that form a line.
     */
    @GetMapping(LINES + "/{n}")
    public ResponseEntity<List<Set<Point>>> getLines(@NotNull @PathVariable int n) {
        return ResponseEntity.ok(service.getLines(n));
    }

    /**
     * Clears all points from the space.
     *
     * @return ResponseEntity with status 200 OK if the points were successfully cleared.
     */
    @DeleteMapping(SPACE)
    public ResponseEntity<Void> clearSpace() {
        service.clearPoints();
        return ResponseEntity.ok().build();
    }
}