package com.hedza06.saasscheduler.executor.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class HttpJobExecutor extends QuartzJobBean {
  public static final String URL = "url";
  public static final String REQUEST_BODY = "requestBody";
  public static final String REQUEST_HEADERS = "requestHeaders";

  private final WebClient webClient;

  public HttpJobExecutor() {
    this.webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    log.info("Executing job with id: {}", context.getJobDetail().getKey());

    var jobData = context.getJobDetail().getJobDataMap();
    var targetUrl = jobData.getString(URL);
    var requestBody = getRequestBodyOrEmpty(jobData);
    var requestHeaders = getRequestHeadersOrEmpty(jobData);

    WebClient.ResponseSpec response = webClient.post()
        .uri(targetUrl)
        .headers(httpHeaders -> httpHeaders.addAll(requestHeaders))
        .body(Mono.just(requestBody), HashMap.class)
        .retrieve();

    var httpStatusCode = Objects.requireNonNull(
        response.toBodilessEntity().block()
    ).getStatusCode();

    log.info("Retrieved Http Status Code: {}", httpStatusCode);
  }

  @SuppressWarnings("unchecked")
  private MultiValueMap<String, String> getRequestHeadersOrEmpty(JobDataMap jobData) {
    return jobData.containsKey(REQUEST_HEADERS)
        ? (MultiValueMap<String, String>) jobData.get(REQUEST_HEADERS)
        : new LinkedMultiValueMap<>();
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getRequestBodyOrEmpty(JobDataMap jobData) {
    return jobData.containsKey(REQUEST_BODY)
        ? (Map<String, Object>) jobData.get(REQUEST_BODY)
        : Collections.emptyMap();
  }
}
