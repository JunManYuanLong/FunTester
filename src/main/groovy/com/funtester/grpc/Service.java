package com.funtester.grpc;

import com.funtester.frame.execute.ThreadPoolUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

public class Service {

    public static void main(String[] args) throws IOException, InterruptedException {
        ThreadPoolExecutor pool = ThreadPoolUtil.createFixedPool(10, "gRPC");
        Server server = ServerBuilder
                .forPort(12345)
                .executor(pool)
                .addService(new HelloServiceImpl())
                .build();

        server.start();
        server.awaitTermination();
    }

}