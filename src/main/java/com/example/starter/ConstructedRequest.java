

package com.example.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


public class ConstructedRequest {

  final static AtomicBoolean hasBeenFound=new AtomicBoolean(false);

  public static void main(String[] args) throws InterruptedException {


    final var numbers=CountDownLatch.fetchNumbers();

    final var factory = Thread.ofVirtual().name("virtualThread ", 0L).factory();

    final var startTime=System.currentTimeMillis();

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Boolean>("Scope",factory)) {
       for(final List<Long>number:numbers){
          scope.fork(new CountDownLatch.ListSearcher(number,72500,hasBeenFound));

      }
      scope.join();
      System.out.println(scope.result());
      System.out.printf("Total Time is %d \n",System.currentTimeMillis()-startTime);

    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }


}
