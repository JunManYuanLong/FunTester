package com.funtester.grpc.pool

import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject

class HelloFactory extends BasePooledObjectFactory<HelloClient> {

    private String host = "127.0.0.1";

    private int port = 12345;

    @Override
    public HelloClient create() throws Exception {
        return new HelloClient(this.host, this.port);
    }

    @Override
    public PooledObject<HelloClient> wrap(HelloClient client) {
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<HelloClient> p) throws Exception {
        p.getObject().shutdown();
        super.destroyObject(p);
    }
}
