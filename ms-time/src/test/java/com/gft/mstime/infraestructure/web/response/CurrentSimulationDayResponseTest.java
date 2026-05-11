package com.gft.mstime.infraestructure.web.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentSimulationDayResponseTest {

    @Test
    void constructor_WhenGivenCurrentDay_ShouldExposeCurrentDay() {
        CurrentSimulationDayResponse response = new CurrentSimulationDayResponse(9);

        assertThat(response.currentDay()).isEqualTo(9);
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        CurrentSimulationDayResponse firstResponse = new CurrentSimulationDayResponse(4);
        CurrentSimulationDayResponse secondResponse = new CurrentSimulationDayResponse(4);

        assertThat(firstResponse).isEqualTo(secondResponse);
        assertThat(firstResponse.hashCode()).isEqualTo(secondResponse.hashCode());
        assertThat(firstResponse).hasToString(secondResponse.toString());
    }
}
