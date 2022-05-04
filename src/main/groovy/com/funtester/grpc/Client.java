package com.funtester.grpc;

import com.funtester.fungrpc.HelloRequest;
import com.funtester.fungrpc.HelloResponse;
import com.funtester.fungrpc.HelloServiceGrpc;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ExecutionException;

public class Client {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
        HelloServiceGrpc.HelloServiceFutureStub helloServiceFutureStub = HelloServiceGrpc.newFutureStub(managedChannel);
        HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);
        HelloRequest helloRequest = HelloRequest.newBuilder()
                .setName("FunTester")
                .build();

        HelloResponse orderResponse = helloServiceBlockingStub.executeHi(helloRequest);

        System.out.println("收到响应: " + orderResponse.getMsg());
        StreamObserver<HelloResponse> helloResponseStreamObserver = new StreamObserver<HelloResponse>() {

            @Override
            public void onNext(HelloResponse value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        };
        helloServiceStub.executeHi(helloRequest, helloResponseStreamObserver);
        ListenableFuture<HelloResponse> helloResponseListenableFuture = helloServiceFutureStub.executeHi(helloRequest);
        HelloResponse helloResponse = helloResponseListenableFuture.get();

        managedChannel.shutdown();
    }

}
