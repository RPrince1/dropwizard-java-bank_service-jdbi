package com.morrisons.base.dropwizard.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstanceException extends ApplicationException {
    public InstanceException(Throwable cause, ErrorSequences errorSequence) {
        super(cause, errorSequence);
        log.error(errorSequence.getMessage(), cause);
    }
}
