package com.funtester.grpc;

import com.funtester.fungrpc.QueryRequest;
import com.funtester.fungrpc.QueryResponse;
import com.funtester.fungrpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void queryUser(QueryRequest request, StreamObserver<QueryResponse> responseObserver) {
        QueryResponse response = QueryResponse.newBuilder()
                .setName("FunTester")
                .setAge(18)
                .setAddress("小八超市2层管理部")
                .setRegion("小八超市")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
