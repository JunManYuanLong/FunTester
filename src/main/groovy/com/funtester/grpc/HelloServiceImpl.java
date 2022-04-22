package com.funtester.grpc;

import com.funtester.fungrpc.HelloRequest;
import com.funtester.fungrpc.HelloResponse;
import com.funtester.fungrpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    private static final Logger logger = LogManager.getLogger(HelloServiceImpl.class);

    @Override
    public void executeHi(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = HelloResponse.newBuilder()
                .setMsg("你好 " + request.getName())
                .build();
        logger.info("用户{}来了",request.getName());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
