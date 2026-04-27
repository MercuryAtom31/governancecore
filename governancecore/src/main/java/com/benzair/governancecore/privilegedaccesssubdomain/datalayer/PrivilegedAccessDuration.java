package com.benzair.governancecore.privilegedaccesssubdomain.datalayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
