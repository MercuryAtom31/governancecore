package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
// This class represents the duration of privileged access in minutes.
// It defines three possible durations: 15, 30, and 60 minutes.
// The class uses Jackson annotations to facilitate JSON serialization and deserialization, allowing it to be easily used in API requests and responses.
// The fromMinutes method allows for converting an integer value back into the corresponding enum constant, ensuring that only valid durations are accepted.
public enum PrivilegedAccessDuration {
    MINUTES_15(15),
    MINUTES_30(30),
    MINUTES_60(60);

    private final int minutes;

    PrivilegedAccessDuration(int minutes) {
        this.minutes = minutes;
    }

    @JsonValue
    public int getMinutes() {
        return minutes;
    }

    @JsonCreator
    public static PrivilegedAccessDuration fromMinutes(int minutes) {
        for (PrivilegedAccessDuration duration : values()) {
            if (duration.minutes == minutes) {
                return duration;
            }
        }
        throw new IllegalArgumentException("Privileged access duration must be 15, 30, or 60 minutes.");
    }
}
