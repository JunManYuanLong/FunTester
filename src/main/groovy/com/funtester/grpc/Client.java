package com.funtester.grpc;

import com.funtester.fungrpc.HelloRequest;
import com.funtester.fungrpc.HelloResponse;
import com.funtester.fungrpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);

        HelloRequest orderRequest = HelloRequest.newBuilder()
                .setName("FunTester")
                .build();

        HelloResponse orderResponse = helloServiceBlockingStub.executeHi(orderRequest);

        System.out.println("收到响应: " + orderResponse.getMsg());

        managedChannel.shutdown();
    }

}
