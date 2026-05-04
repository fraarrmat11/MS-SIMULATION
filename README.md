# Time + Map — Ruben

Upstream of all services. Publishes `time.advanced.v1` when the user advances time.
Builds the map by listening to registration events from other services.

## Modules

### Module: simulation-clock

```mermaid
classDiagram
    class SimulationClock {
        <<aggregate root>>
        +SimulationDay currentDay
        +advanceDay(days) DomainEvent[]
        +getCurrentDay() SimulationDay
    }
    class SimulationDay {
        <<value object>>
        +int value
    }

    class TimeAdvancedEvent {
    <<domain event>>
    +UUID eventId
    +int previousDay
    +int currentDay
    +int daysAdvanced
    +Instant occurredAt
}
    SimulationClock --> SimulationDay
    SimulationClock ..> TimeAdvancedEvent : publishes
```

### Module: map-state

```mermaid
classDiagram
    class MapState {
        List<TruckPosition> trucks
        List<WarehousePosition> warehouses
        <<aggregate root>>
        +updateTruckPosition(TruckPosition, TruckId)
        +registerWarehouse(warehouseId, name, location)
    }
    class TruckPosition {
        <<value object>>
        +UUID truckId
        +Location location
    }

    class WarehousePosition {
        <<value object>>
        +UUID warehouseId
        +String name
        +Location location
        +WarehouseType type
    }
    class Location {
        <<value object>>
        +int x
        +int y
    }
    MapState "1" --> "*" TruckPosition
    MapState "1" --> "*" WarehousePosition
```

## Use cases

```mermaid
flowchart TD
    UC1([POST /tick]) --> A1[AdvanceTime]
    A1 --> A2[SimulationClock.advanceDay]
    A2 --> A3[Publishes time.advanced.v1]
    A3 --> A4[Payload: previousDay, currentDay, daysAdvanced]

    UC2([GET /map]) --> B1[GetMapState]
    B1 --> B2[Returns current positions of all elements]

    EV1([truck.position.updated.v1 received]) --> C1[Updates TruckPosition in MapState]
    EV3([warehouse.registered.v1 received]) --> C4[Adds warehouse to map]
```

## Contracts with other microservices

| Microservice | Publishes | Consumes | Purpose |
|---|---|---|---|
| **Transport** | `time.advanced.v1` | `truck.position.updated.v1 (TruckPosition, TruckId)` | Transport uses the time advance event to move trucks. Time + Map uses Transport events to display trucks on the map and update their positions. |
| **Production** | `time.advanced.v1` | `None` | Production uses the time advance event to progress production orders. |
| **Warehouse** | `time.advanced.v1` | `warehouse.registered.v1 (WarehouseLocation)`<br>`warehouse.updated.v1 (WarehouseLocation)` *(optional)* | Warehouse may use the time advance event to check stock, consumption or replenishment needs. Time + Map uses Warehouse events to display warehouses on the map. |
| **Reporting** | `time.advanced.v1` | `None` | Reporting records time advances for history, monitoring and statistics. |
