package com.giampaolo.pattern_recognition.utils;

import com.giampaolo.pattern_recognition.dto.PointDTO;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static Point randomPoint() {
        return new Point(ThreadLocalRandom.current().nextInt(),
                ThreadLocalRandom.current().nextInt());
    }

    public static PointDTO randomPointDto() {
        return new PointDTO(ThreadLocalRandom.current().nextInt(),
                ThreadLocalRandom.current().nextInt());
    }
}
