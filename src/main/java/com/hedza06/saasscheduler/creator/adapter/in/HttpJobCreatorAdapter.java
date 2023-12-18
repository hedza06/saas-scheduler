package com.hedza06.saasscheduler.creator.adapter.in;

import com.hedza06.saasscheduler.creator.application.port.in.JobCreatorUseCase;
import com.hedza06.saasscheduler.executor.adapter.out.HttpJobExecutor;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
class HttpJobCreatorAdapter implements JobCreatorUseCase {

  private final Scheduler scheduler;

  @Override
  public String create(JobCreateCommand createCommand) throws SchedulerException {
    var jobDetail = createJobDetail(createCommand);
    var jobTrigger = createJobTrigger(createCommand, jobDetail);

    scheduler.scheduleJob(jobDetail, jobTrigger);

    return jobDetail.getKey().getName();
  }

  private JobDetail createJobDetail(JobCreateCommand createCommand) {
    var jobData = new JobDataMap();
    jobData.put(HttpJobExecutor.URL, createCommand.url());
    putRequestBodyIfExists(jobData, createCommand.requestBody());
    putRequestHeadersIfExists(jobData, createCommand.requestHeaders());

    return JobBuilder.newJob(HttpJobExecutor.class)
        .withIdentity(UUID.randomUUID().toString(), "app-name-here")
        .withDescription(createCommand.description() + "_JOB_DETAIL")
        .setJobData(jobData)
        .storeDurably()
        .build();
  }

  private Trigger createJobTrigger(JobCreateCommand createCommand, JobDetail jobDetail) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(jobDetail.getKey().getName(), "app-name-here")
        .withDescription(createCommand.description() + "_CRON_TRIGGER")
        .startAt(Date.from(Instant.now()))
        .withSchedule(CronScheduleBuilder.cronSchedule(createCommand.definition()))
        .build();
  }

  private void putRequestBodyIfExists(JobDataMap jobDataMap, Map<String, Object> requestBody) {
    if (isNotEmpty(requestBody)) {
      jobDataMap.put(HttpJobExecutor.REQUEST_BODY, requestBody);
    }
  }

  private void putRequestHeadersIfExists(JobDataMap jobDataMap,
                                         Map<String, String> requestHeaders) {
    if (isNotEmpty(requestHeaders)) {
      MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
      requestHeaders.forEach((key, value) -> headers.put(key, singletonList(value)));

      jobDataMap.put(HttpJobExecutor.REQUEST_HEADERS, headers);
    }
  }
}
