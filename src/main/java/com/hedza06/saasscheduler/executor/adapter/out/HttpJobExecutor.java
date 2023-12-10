package com.hedza06.saasscheduler.executor.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Slf4j
@Service
public class HttpJobExecutor extends QuartzJobBean {

  private final WebClient webClient;

  public HttpJobExecutor() {
    this.webClient = WebClient.create();
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    log.info("Executing job...");
    log.info("Context: {}", context);

    var jobData = context.getJobDetail().getJobDataMap();
    var targetUrl = jobData.getString("url");
    var requestBody = (HashMap<String, Object>) jobData.get("requestBody");

    log.info("Target Url: {}", targetUrl);
    log.info("Request Body: {}", requestBody);
  }
}
