package com.funtester.grpc.pool

import com.funtester.fungrpc.HelloRequest
import com.funtester.fungrpc.HelloResponse
import com.funtester.fungrpc.HelloServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException

import java.util.concurrent.TimeUnit

class HelloClient {

    private final ManagedChannel channel;
    //一个gRPC信道
    private HelloServiceGrpc.HelloServiceBlockingStub greeterBlockingStub;
    //阻塞/同步 存根

    //初始化信道和存根
    public HelloClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    //客户端方法
    public void hi(String name) {
        //需要用到存根时创建,不可复用

        def compression = HelloServiceGrpc.newBlockingStub(channel).withCompression("gzip")
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloResponse response;
        try {
            response = compression.e(request);
        } catch (StatusRuntimeException e) {
            System.out.println("RPC调用失败:" + e.getMessage());
            return;
        }
        System.out.println("服务器返回信息:" + response.getMsg());
    }

}
