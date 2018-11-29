package com.lib.socketiomanager;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class Option {
    private long timeOut;
    private boolean isReconnect;
    private String[] transportType;

    private Option(Builder builder) {
        timeOut = builder.timeOut;
        isReconnect = builder.isReconnect;
        transportType = builder.transportType;
    }

    long getTimeOut() {
        return timeOut;
    }

    boolean isReconnect() {
        return isReconnect;
    }

    String[] getTransportType() {
        return transportType;
    }

    public static final class Builder {
        private long timeOut = 30000;
        private boolean isReconnect = true;
        private String[] transportType = new String[]{Constants.TYPE_WEBSOCKET};

        public Builder() {
        }

        public Builder withTimeOut(long val) {
            timeOut = val;
            return this;
        }

        public Builder withIsReconnect(boolean val) {
            isReconnect = val;
            return this;
        }

        public Builder withTransportType(String[] val) {
            transportType = val;
            return this;
        }

        public Option build() {
            return new Option(this);
        }
    }
}
