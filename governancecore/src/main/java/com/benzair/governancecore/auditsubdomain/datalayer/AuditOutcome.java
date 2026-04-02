package com.benzair.governancecore.auditsubdomain.datalayer;

public enum AuditOutcome {
    SUCCESS,
    FAILURE,
    DENIED
}

/*
SUCCESS = the action happened
FAILURE = the action was supposed to happen but failed
DENIED = the action was blocked by a rule
 */
