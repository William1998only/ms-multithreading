package com.other.multithreading.controller;

import com.other.multithreading.service.StockPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class StockPriceController {

  @Autowired
  StockPriceService stockPriceService;

//  @Autowired
//  private ThreadPoolTaskExecutor taskExecutor;

  Logger logger = LoggerFactory.getLogger(StockPriceController.class);

  @GetMapping(value="/getAllStockPrices", produces="application/json")
  public ResponseEntity getAllStockPrices() {

    List<Double> stockPrices  = getAllThreeStockPrices();
    List<String> stockPriceStrings = stockPrices.stream().map(Object::toString).collect(Collectors.toList());

    return ResponseEntity.ok().body(String.join(",", stockPriceStrings));
  }

  public List<Double> getAllThreeStockPrices() {
    List<Double> stockPriceList = new ArrayList<>();
    CompletableFuture<Double> cf1, cf2, cf3;
    try {
//      logger.info("Calling async getStockPrice for company1, active count: {}, Pool size: {}, Queue Size: {}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize(), taskExecutor.getThreadPoolExecutor().getQueue().size());
      cf1 = stockPriceService.getStockPrice("company1");
//      logger.info("Calling async getStockPrice for company2, active count: {}, Pool size: {}, Queue Size: {}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize(), taskExecutor.getThreadPoolExecutor().getQueue().size());

      cf2 = stockPriceService.getStockPrice("company2");
//      logger.info("Calling async getStockPrice for company3, active count: {}, Pool size: {}, Queue Size: {}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize(), taskExecutor.getThreadPoolExecutor().getQueue().size());

      cf3 = stockPriceService.getStockPrice("company3");
//      logger.info("After three calls to async getStockPrice, active count: {}, Pool size: {}, Queue Size: {}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize(), taskExecutor.getThreadPoolExecutor().getQueue().size());

      // Monitor the threads as they complete.
//      int sleeptime = 0;
//      for(int i = 0; i < 20; i++) {
//        Thread.sleep(100);
//        sleeptime=(i+1)*100;
//        logger.info("After {} milliseconds, active count: {}, Pool size: {}, Queue Size: {}", sleeptime, taskExecutor.getActiveCount(), taskExecutor.getPoolSize(), taskExecutor.getThreadPoolExecutor().getQueue().size());
//      }
      stockPriceList.add(cf1.get());
      logger.info("Data 1 has been fetched");
      stockPriceList.add(cf2.get());
      logger.info("Data 2 has been fetched");
      stockPriceList.add(cf3.get());
      logger.info("Data 3 has been fetched");
    }
    catch (Exception e) {
      System.out.println("error " + e);
    }

    return stockPriceList;

  }
}
