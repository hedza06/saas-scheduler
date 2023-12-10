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

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class JobCreatorAdapter implements JobCreatorUseCase {

  private final Scheduler scheduler;

  @Override
  public String create(JobCreateCommand createCommand) throws SchedulerException {
    var jobDetail = createJobDetail(createCommand);
    var jobTrigger = createJobTrigger(createCommand, jobDetail);

    scheduler.scheduleJob(jobTrigger);

    return jobDetail.getKey().getName();
  }

  private JobDetail createJobDetail(JobCreateCommand createCommand) {
    var jobData = new JobDataMap();
    jobData.put("url", createCommand.url());
    jobData.put("requestBody", createCommand.requestPayload());

    return JobBuilder.newJob(HttpJobExecutor.class)
        .withIdentity(UUID.randomUUID().toString(), "app-name-here+job")
        .withDescription(createCommand.description() + "_JOB_DETAIL")
        .setJobData(jobData)
        .storeDurably()
        .build();
  }

  private Trigger createJobTrigger(JobCreateCommand createCommand, JobDetail jobDetail) {
    return TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity(jobDetail.getKey().getName(), "app-name-here+trigger")
        .withDescription(createCommand.description() + "_CRON_TRIGGER")
        .startAt(Date.from(Instant.now()))
        .withSchedule(CronScheduleBuilder.cronSchedule(createCommand.definition()))
        .build();
  }
}
