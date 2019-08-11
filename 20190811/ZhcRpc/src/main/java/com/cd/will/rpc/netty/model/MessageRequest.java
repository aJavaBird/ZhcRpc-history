package com.cd.will.rpc.netty.model;

import java.io.Serializable;
import java.util.Arrays;

import org.msgpack.annotation.Message;

@Message
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    private String className;

    private String methodName;

    private String[] classNames;

    private Object[] parametersVal;

    public MessageRequest() {

    }

    public MessageRequest(String messageId, String className, String methodName, String[] classNames,
            Object[] parametersVal) {
        super();
        this.messageId = messageId;
        this.className = className;
        this.methodName = methodName;
        this.classNames = classNames;
        this.parametersVal = parametersVal;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getClassNames() {
        return classNames;
    }

    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    public Object[] getParametersVal() {
        return parametersVal;
    }

    public void setParametersVal(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }

    @Override
    public String toString() {
        return "MessageRequest [messageId=" + messageId + ", className=" + className + ", methodName=" + methodName
                + ", classNames=" + Arrays.toString(classNames) + ", parametersVal="
                + Arrays.toString(parametersVal) + "]";
    }
}
