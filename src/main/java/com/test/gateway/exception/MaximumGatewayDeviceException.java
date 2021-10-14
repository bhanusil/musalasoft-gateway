package com.test.gateway.exception;

public class MaximumGatewayDeviceException extends RuntimeException {

    public MaximumGatewayDeviceException(String gatewaySerialNumber, long existingDeviceCount) {
        super(MaximumGatewayDeviceException.generateMessage(gatewaySerialNumber, existingDeviceCount));
    }

    public static String generateMessage(String gatewaySerialNumber, long deviceCount) {
        return "Gateway(Serial Number : "+ gatewaySerialNumber +") already reached it's maximum device count["+deviceCount+"]";
    }

}
