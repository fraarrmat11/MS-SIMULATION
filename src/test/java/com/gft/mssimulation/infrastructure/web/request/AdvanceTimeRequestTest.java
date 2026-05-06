package com.gft.mssimulation.infrastructure.web.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdvanceTimeRequestTest {

    @Test
    void constructor_WhenGivenDays_ShouldExposeDays() {
        AdvanceTimeRequest request = new AdvanceTimeRequest(4);

        assertThat(request.days()).isEqualTo(4);
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        AdvanceTimeRequest firstRequest = new AdvanceTimeRequest(2);
        AdvanceTimeRequest secondRequest = new AdvanceTimeRequest(2);

        assertThat(firstRequest).isEqualTo(secondRequest);
        assertThat(firstRequest.hashCode()).isEqualTo(secondRequest.hashCode());
        assertThat(firstRequest).hasToString("AdvanceTimeRequest[days=2]");
    }
}
