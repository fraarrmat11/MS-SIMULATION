package com.gft.mssimulation.infrastructure.web.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdvanceTimeRequest(
        @NotNull
        @Positive
        Integer days
)
        {
}
