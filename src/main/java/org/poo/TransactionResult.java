package org.poo;

import lombok.Getter;
import org.poo.jsonobject.JsonObject;

@Getter
public class TransactionResult {
    private boolean success;
    private String reason;
    private JsonObject result;

    public TransactionResult(boolean success, String reason, JsonObject result) {
        this.success = success;
        this.reason = reason;
        this.result = result;
    }
}
