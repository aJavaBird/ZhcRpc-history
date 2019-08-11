package com.cd.will.rpc.netty.model;

import java.io.Serializable;

import org.msgpack.annotation.Message;

@Message
public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String error;

    private Object result;

    private boolean returnNotNull;

    public MessageResponse() {

    }

    public MessageResponse(String messageId, String error, Object result, boolean returnNotNull) {
        super();
        this.messageId = messageId;
        this.error = error;
        this.result = result;
        this.returnNotNull = returnNotNull;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    @Override
    public String toString() {
        return "MessageResponse [messageId=" + messageId + ", error=" + error + ", result=" + result
                + ", returnNotNull=" + returnNotNull + "]";
    }

}
