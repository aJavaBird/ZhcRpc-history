package com.cd.will.rpc.netty.model;

public class MessageClientWait {
    String address;

    Boolean isWait;

    public MessageClientWait() {

    }

    public MessageClientWait(String address, Boolean isWait) {
        super();
        this.address = address;
        this.isWait = isWait;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsWait() {
        return isWait;
    }

    public void setIsWait(Boolean isWait) {
        this.isWait = isWait;
    }

    public synchronized void waitAMinute(long msNum) throws InterruptedException {
        if (msNum > 0) {
            this.wait(msNum);
        }
    }

    @Override
    public String toString() {
        return "MessageClientWait [address=" + address + ", isWait=" + isWait + "]";
    }
}
