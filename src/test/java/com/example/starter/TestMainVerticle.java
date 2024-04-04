package com.example.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.locks.LockSupport;

public class TestMainVerticle {


  public static void  main(String[]args) {

    final var vertx=Vertx.vertx();
    vertx.deployVerticle(new MainVerticle(),new DeploymentOptions().setThreadingModel(ThreadingModel.VIRTUAL_THREAD));

    while (vertx!=null) {
      LockSupport.park();
    }
  }


}
