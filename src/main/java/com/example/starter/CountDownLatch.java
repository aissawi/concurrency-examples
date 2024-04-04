package com.example.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

public class CountDownLatch {

  final static AtomicBoolean hasBeenFound=new AtomicBoolean(false);


  public static List<List<Long>>fetchNumbers(){
     final List<Long>myList= new ArrayList<>(LongStream.range(1, 1000000L).boxed().toList());

     //Collections.shuffle(myList,new Random(1500));


     final var partitionList=new ArrayList<List<Long>>();

     final int partitionSize=10000;

     for (int i=0; i<myList.size(); i += partitionSize) {
       partitionList.add(myList.subList(i, Math.min(i + partitionSize, myList.size())));
     }
     return partitionList;
   }
  public  static void main(String[] args) throws InterruptedException {

    final var ls=new ArrayList<ListSearcher>();
    final var partitionList=fetchNumbers();
    final var factory = Thread.ofVirtual().name("virtualThread ", 0L).factory();
    final var startTime=System.currentTimeMillis();

    try (final var executor=Executors.newThreadPerTaskExecutor(factory)){
        for (final List<Long> part : partitionList) {

            final var listSearcher = new ListSearcher(part, 72500, hasBeenFound);
            ls.add(listSearcher);
        }

      executor.invokeAll(ls);
    }

    System.out.printf("Total Time is %d \n",System.currentTimeMillis()-startTime);
  }

  static class ListSearcher implements Callable<Boolean> {

    public ListSearcher(final List<Long> numbers, int searchCriteria, AtomicBoolean hasBeenFound) {
      this.numbers = numbers;
      this.searchCriteria = searchCriteria;
      this.hasBeenFound = hasBeenFound;
}

    private final  List<Long> numbers;

    private final int searchCriteria;

    private final AtomicBoolean hasBeenFound;

    @Override
    public Boolean call() {

     return searchNumber();

    }
    public boolean searchNumber() {
      if(hasBeenFound.get())
        return true;

      for (int count=0;count<numbers.size()-1;count++){

          if (numbers.get(count)==searchCriteria){
            System.out.printf("The first thread to find the elements is %s \n",Thread.currentThread().getName());
            hasBeenFound.compareAndSet(false,true);
            return true;
          }

        }
      return false;

    }
  }
}
