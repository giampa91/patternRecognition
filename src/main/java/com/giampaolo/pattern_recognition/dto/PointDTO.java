package com.giampaolo.pattern_recognition.dto;

import jakarta.validation.constraints.NotNull;

public record PointDTO(@NotNull Integer x, @NotNull Integer y) {
}
