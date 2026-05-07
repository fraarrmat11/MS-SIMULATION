package com.gft.mssimulation.application.simulationclock;

import com.gft.mssimulation.application.port.out.SimulationClockRepository;
import com.gft.mssimulation.application.port.out.TimeAdvancedEventPublisher;
import com.gft.mssimulation.application.simulationclock.command.AdvanceTimeCommand;
import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;
import com.gft.mssimulation.domain.simulationclock.SimulationClock;
import com.gft.mssimulation.domain.simulationclock.SimulationDay;
import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AdvanceTimeServiceTest {

    private final SimulationClockRepository simulationClockRepository = mock(SimulationClockRepository.class);
    private final TimeAdvancedEventPublisher timeAdvancedEventPublisher = mock(TimeAdvancedEventPublisher.class);

    private final AdvanceTimeService advanceTimeService = new AdvanceTimeService(
            simulationClockRepository,
            timeAdvancedEventPublisher
    );

    @Test
    void advanceTime_WhenGivenOneDay_ShouldLoadClockAdvanceSavePublishAndReturnResult() {
        SimulationClock simulationClock = SimulationClock.startingAtDayZero();
        when(simulationClockRepository.load()).thenReturn(simulationClock);

        TimeAdvancedResult result = advanceTimeService.advanceTime(new AdvanceTimeCommand(1));

        assertThat(result.eventId()).isNotNull();
        assertThat(result.previousDay()).isZero();
        assertThat(result.currentDay()).isEqualTo(1);
        assertThat(result.daysAdvanced()).isEqualTo(1);
        assertThat(result.occurredAt()).isNotNull();

        ArgumentCaptor<SimulationClock> savedClockCaptor = ArgumentCaptor.forClass(SimulationClock.class);
        verify(simulationClockRepository).save(savedClockCaptor.capture());
        assertThat(savedClockCaptor.getValue().getCurrentDay().dayNumber()).isEqualTo(1);

        ArgumentCaptor<TimeAdvancedEvent> publishedEventCaptor = ArgumentCaptor.forClass(TimeAdvancedEvent.class);
        verify(timeAdvancedEventPublisher).publish(publishedEventCaptor.capture());

        TimeAdvancedEvent publishedEvent = publishedEventCaptor.getValue();
        assertThat(publishedEvent.eventId()).isEqualTo(result.eventId());
        assertThat(publishedEvent.previousDay()).isEqualTo(result.previousDay());
        assertThat(publishedEvent.currentDay()).isEqualTo(result.currentDay());
        assertThat(publishedEvent.daysAdvanced()).isEqualTo(result.daysAdvanced());
        assertThat(publishedEvent.occurredAt()).isEqualTo(result.occurredAt());

        InOrder inOrder = inOrder(simulationClockRepository, timeAdvancedEventPublisher);
        inOrder.verify(simulationClockRepository).load();
        inOrder.verify(simulationClockRepository).save(simulationClock);
        inOrder.verify(timeAdvancedEventPublisher).publish(publishedEvent);

        verifyNoMoreInteractions(simulationClockRepository, timeAdvancedEventPublisher);
    }

    @Test
    void advanceTime_WhenGivenSeveralDays_ShouldAdvanceFromLoadedClockCurrentDay() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(4));
        when(simulationClockRepository.load()).thenReturn(simulationClock);

        TimeAdvancedResult result = advanceTimeService.advanceTime(new AdvanceTimeCommand(3));

        assertThat(result.previousDay()).isEqualTo(4);
        assertThat(result.currentDay()).isEqualTo(7);
        assertThat(result.daysAdvanced()).isEqualTo(3);
        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(7);

        verify(simulationClockRepository).load();
        verify(simulationClockRepository).save(simulationClock);
        verify(timeAdvancedEventPublisher).publish(any(TimeAdvancedEvent.class));
        verifyNoMoreInteractions(simulationClockRepository, timeAdvancedEventPublisher);
    }

    @Test
    void advanceTime_WhenGivenZeroDays_ShouldThrowExceptionWithoutSavingNorPublishing() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(5));
        when(simulationClockRepository.load()).thenReturn(simulationClock);

        assertThatThrownBy(() -> advanceTimeService.advanceTime(new AdvanceTimeCommand(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days to advance must be greater than zero");

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(5);

        verify(simulationClockRepository).load();
        verifyNoMoreInteractions(simulationClockRepository);
        verifyNoInteractions(timeAdvancedEventPublisher);
    }

    @Test
    void advanceTime_WhenGivenNegativeDays_ShouldThrowExceptionWithoutSavingNorPublishing() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(5));
        when(simulationClockRepository.load()).thenReturn(simulationClock);

        assertThatThrownBy(() -> advanceTimeService.advanceTime(new AdvanceTimeCommand(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days to advance must be greater than zero");

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(5);

        verify(simulationClockRepository).load();
        verifyNoMoreInteractions(simulationClockRepository);
        verifyNoInteractions(timeAdvancedEventPublisher);
    }

    @Test
    void advanceTime_WhenGivenNullCommand_ShouldThrowExceptionAndNotCallDependencies() {
        assertThatThrownBy(() -> advanceTimeService.advanceTime(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("command cannot be null");

        verifyNoInteractions(simulationClockRepository, timeAdvancedEventPublisher);
    }

    @Test
    void constructor_WhenGivenNullRepository_ShouldThrowException() {
        assertThatThrownBy(() -> new AdvanceTimeService(null, timeAdvancedEventPublisher))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("simulationClockRepository cannot be null");
    }

    @Test
    void constructor_WhenGivenNullPublisher_ShouldThrowException() {
        assertThatThrownBy(() -> new AdvanceTimeService(simulationClockRepository, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("timeAdvancedEventPublisher cannot be null");
    }
}
