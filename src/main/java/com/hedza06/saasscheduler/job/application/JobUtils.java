package com.hedza06.saasscheduler.job.application;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobUtils {
  public static final String URL = "url";
  public static final String REQUEST_BODY = "requestBody";
  public static final String REQUEST_HEADERS = "requestHeaders";


  public static void putRequestBodyIfExists(JobDataMap jobDataMap,
                                            Map<String, Object> requestBody) {
    if (isNotEmpty(requestBody)) {
      jobDataMap.put(REQUEST_BODY, requestBody);
    }
  }

  public static void putRequestHeadersIfExists(JobDataMap jobDataMap,
                                               Map<String, String> requestHeaders) {
    if (isNotEmpty(requestHeaders)) {
      MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
      requestHeaders.forEach((key, value) -> headers.put(key, singletonList(value)));

      jobDataMap.put(REQUEST_HEADERS, headers);
    }
  }
}
