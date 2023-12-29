package com.hedza06.saasscheduler.job.adapter.out;

import com.hedza06.saasscheduler.job.application.port.out.ForceRunJobUseCase;
import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.hedza06.saasscheduler.job.application.JobUtils.URL;
import static com.hedza06.saasscheduler.job.application.JobUtils.putRequestBodyIfExists;
import static com.hedza06.saasscheduler.job.application.JobUtils.putRequestHeadersIfExists;
import static org.quartz.JobKey.jobKey;

@Service
@RequiredArgsConstructor
class ForceRunJobAdapter implements ForceRunJobUseCase {

  private final Scheduler scheduler;


  @Override
  public String httpForceRun(String id, ForceRunCommand forceRunCommand) throws SchedulerException {
    var jobGroupName = SecurityContextHolder.getContext().getAuthentication().getName();
    var jobKey = jobKey(UUID.randomUUID().toString(), jobGroupName);

    var jobData = new JobDataMap();
    jobData.put(URL, forceRunCommand.url());
    putRequestBodyIfExists(jobData, forceRunCommand.requestBody());
    putRequestHeadersIfExists(jobData, forceRunCommand.requestHeaders());

    var jobDetail = JobBuilder.newJob(HttpJobExecutorAdapter.class)
        .withIdentity(jobKey)
        .withDescription("force-run: [" + id + "]")
        .setJobData(jobData)
        .storeDurably()
        .build();

    scheduler.addJob(jobDetail, true);
    scheduler.triggerJob(jobKey);

    return jobDetail.getKey().getName();
  }
}
