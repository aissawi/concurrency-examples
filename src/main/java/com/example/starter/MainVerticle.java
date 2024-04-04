package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.net.PemKeyCertOptions;

import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final var httpOptions=new HttpServerOptions();
    httpOptions.setCompressionSupported(false);
    httpOptions.setUseAlpn(true);
    httpOptions.setSsl(true);
    httpOptions.setTcpFastOpen(true);
    httpOptions.setTcpQuickAck(true);
    httpOptions.setTcpKeepAlive(true);
    httpOptions.setTcpNoDelay(true);

    final var certPath="C:\\Users\\user\\Downloads\\starter\\src\\test\\java\\server-cert.pem";

    final var keyPath="C:\\Users\\user\\Downloads\\starter\\src\\test\\java\\server-key.pem";

    httpOptions.setKeyCertOptions(new PemKeyCertOptions()
              .setCertPath(certPath).setKeyPath(keyPath));

    final var http= vertx.createHttpServer(httpOptions);

    http.requestHandler(req -> {
      final var thread=Thread.currentThread().getName();
      final Map<String,Object> mp=new HashMap<>();
      mp.put("content-Type","application/json");
      mp.put("threadName",thread);
      mp.put("threadPriority",Thread.currentThread().getPriority());
      mp.put("threadId",Thread.currentThread().threadId());


      req.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodeToBuffer(mp));
    }).listen(8888, server -> {
      if (server.succeeded()) {
        startPromise.complete();

        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(server.cause());
      }
    });
  }
}
