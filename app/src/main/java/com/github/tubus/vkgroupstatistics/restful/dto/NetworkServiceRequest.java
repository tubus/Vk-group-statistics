package com.github.tubus.vkgroupstatistics.restful.dto;

public class NetworkServiceRequest {

    private String data;

    private NETWORK_SERVICE_ACTION action;

    private NetworkServiceRequest() {
    }

    public NETWORK_SERVICE_ACTION getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public static NetworServiceRequestBuilder builder() {
        return new NetworServiceRequestBuilder();
    }

    public static class NetworServiceRequestBuilder {

        private NetworkServiceRequest instance;

        public NetworServiceRequestBuilder() {
            instance = new NetworkServiceRequest();
        }

        public NetworServiceRequestBuilder setAction(NETWORK_SERVICE_ACTION action) {
            instance.action = action;
            return this;
        }

        public NetworServiceRequestBuilder setDataString(String dataString) {
            instance.data = dataString;
            return this;
        }

        public NetworkServiceRequest build() {
            return instance;
        }
    }
}