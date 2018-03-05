package com.morrisons.base.dropwizard.exceptions;

import lombok.Getter;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

@Getter
public class ApplicationException extends RuntimeException {
    private ErrorSequences errorSequences;

    public ApplicationException(ErrorSequences errorSequences) {
        super(errorSequences.getMessage());
        this.errorSequences = errorSequences;
    }

    public ApplicationException(Throwable cause, ErrorSequences errorSequences) {
        super(errorSequences.getMessage(), cause);
        this.errorSequences = errorSequences;
    }

    public static void handleCallBackFailedException(CallbackFailedException cfe ){
        if( cfe.getCause() instanceof ApplicationException ){
            throw (ApplicationException) cfe.getCause();
        }
        else{
            throw new ApplicationException( cfe.getCause(), ErrorSequences.AN_UNEXPECTED_ERROR_HAS_OCCURRED );
        }
    }
}
