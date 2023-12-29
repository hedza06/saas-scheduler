package com.hedza06.saasscheduler.job.adapter.out;

import com.hedza06.saasscheduler.job.application.port.out.HttpJobExecutorUseCase;
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

import static com.hedza06.saasscheduler.job.application.JobUtils.REQUEST_BODY;
import static com.hedza06.saasscheduler.job.application.JobUtils.REQUEST_HEADERS;
import static com.hedza06.saasscheduler.job.application.JobUtils.URL;

@Slf4j
@Service
public class HttpJobExecutorAdapter extends QuartzJobBean implements HttpJobExecutorUseCase {
  private final WebClient webClient;

  public HttpJobExecutorAdapter() {
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
