package com.hedza06.saasscheduler.job.adapter.in;

import com.hedza06.saasscheduler.job.adapter.out.HttpJobExecutorAdapter;
import com.hedza06.saasscheduler.job.application.port.in.CreateJobUseCase;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.hedza06.saasscheduler.job.application.JobUtils.URL;
import static com.hedza06.saasscheduler.job.application.JobUtils.putRequestBodyIfExists;
import static com.hedza06.saasscheduler.job.application.JobUtils.putRequestHeadersIfExists;

@Service
@RequiredArgsConstructor
class HttpCreateJobAdapter implements CreateJobUseCase {

  private final Scheduler scheduler;


  @Override
  public String create(CreateJobCommand createCommand) throws SchedulerException {
    var jobDetail = createJobDetail(createCommand);
    var jobTrigger = createJobTrigger(createCommand, jobDetail);

    scheduler.scheduleJob(jobDetail, jobTrigger);

    return jobDetail.getKey().getName();
  }

  private JobDetail createJobDetail(CreateJobCommand createCommand) {
    var jobData = new JobDataMap();
    jobData.put(URL, createCommand.url());
    putRequestBodyIfExists(jobData, createCommand.requestBody());
    putRequestHeadersIfExists(jobData, createCommand.requestHeaders());

    return JobBuilder.newJob(HttpJobExecutorAdapter.class)
        .withIdentity(
            UUID.randomUUID().toString(),
            SecurityContextHolder.getContext().getAuthentication().getName()
        )
        .withDescription(createCommand.description() + "_JOB_DETAIL")
        .setJobData(jobData)
        .storeDurably()
        .build();
  }

  private Trigger createJobTrigger(CreateJobCommand createCommand, JobDetail jobDetail) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(
            jobDetail.getKey().getName(),
            SecurityContextHolder.getContext().getAuthentication().getName()
        )
        .withDescription(createCommand.description() + "_CRON_TRIGGER")
        .startAt(Date.from(Instant.now()))
        .withSchedule(CronScheduleBuilder.cronSchedule(createCommand.definition()))
        .build();
  }
}
