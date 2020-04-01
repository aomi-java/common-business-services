//package tech.aomi.common.batch.repository;
//
//import com.mongodb.client.result.UpdateResult;
//import lombok.Getter;
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.repository.dao.JobExecutionDao;
//import org.springframework.batch.core.repository.dao.NoSuchObjectException;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.util.Assert;
//import tech.aomi.common.batch.entity.BatchJobExecution;
//import tech.aomi.common.batch.entity.BatchJobInstance;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * @author Sean Create At 2020/3/31
// */
//@Getter
//public class MongoJobExecutionDao implements JobExecutionDao {
//
//    private MongoTemplate mongoTemplate;
//
//    private MongodbIdRepository idRepository;
//
//    public MongoJobExecutionDao(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//        this.idRepository = new MongodbIdRepository(mongoTemplate);
//    }
//
//    @Override
//    public void saveJobExecution(JobExecution jobExecution) {
//        validateJobExecution(jobExecution);
//        jobExecution.incrementVersion();
//        jobExecution.setId(idRepository.getJobExecutionId());
//
////        BatchJobExecution batchJobExecution = new BatchJobExecution();
////        batchJobExecution.setId(jobExecution.getId());
////        batchJobExecution.setVersion(jobExecution.getVersion());
////
////        batchJobExecution.setInstanceId(jobExecution.getJobId());
////
////        batchJobExecution.setStartAt(jobExecution.getStartTime());
////        batchJobExecution.setEndAt(jobExecution.getEndTime());
////
////        batchJobExecution.setCreateAt(jobExecution.getCreateTime());
////        batchJobExecution.setUpdateAt(jobExecution.getLastUpdated());
////
////        batchJobExecution.setStatus(jobExecution.getStatus().name());
////        batchJobExecution.setExitCode(jobExecution.getExitStatus().getExitCode());
////        batchJobExecution.setExitMessage(jobExecution.getExitStatus().getExitDescription());
////
////        batchJobExecution.setConfigurationName(jobExecution.getJobConfigurationName());
////        batchJobExecution.setJobParameters(jobExecution.getJobParameters());
//
//        mongoTemplate.save(jobExecution);
//    }
//
//    @Override
//    public void updateJobExecution(JobExecution jobExecution) {
//        validateJobExecution(jobExecution);
//        Assert.notNull(jobExecution.getId(),
//                "JobExecution ID cannot be null. JobExecution must be saved before it can be updated");
//
//        Assert.notNull(jobExecution.getVersion(),
//                "JobExecution version cannot be null. JobExecution must be saved before it can be updated");
//
//        Integer version = jobExecution.getVersion();
//
//        boolean exist = mongoTemplate.exists(new Query().addCriteria(Criteria.where("id").is(jobExecution.getId())), BatchJobExecution.class);
//        if (!exist) {
//            throw new NoSuchObjectException("Invalid JobExecution, ID " + jobExecution.getId() + " not found.");
//        }
//        UpdateResult updateResult = mongoTemplate.updateFirst(
//                new Query().addCriteria(Criteria.where("id").is(jobExecution.getId()).and("version").is(jobExecution.getVersion())),
//                new Update()
//                        .set("startTime", jobExecution.getStartTime())
//                        .set("endTime", jobExecution.getEndTime())
//                        .set("status", jobExecution.getStatus())
//                        .set("exitStatus", jobExecution.getExitStatus())
//                        .set("createTime", jobExecution.getCreateTime())
//                        .set("lastUpdated", jobExecution.getLastUpdated())
//                        .inc("value", 1L)
//                ,
//                JobExecution.class
//        );
//        if (updateResult.getModifiedCount() == 0) {
//            throw new OptimisticLockingFailureException("Attempt to update job execution id="
//                    + jobExecution.getId() + ", where current version is " + version);
//        }
//        jobExecution.incrementVersion();
//    }
//
//    @Override
//    public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
//        Assert.notNull(jobInstance, "Job cannot be null.");
//        Assert.notNull(jobInstance.getId(), "Job Id cannot be null.");
//
//        return mongoTemplate.find(new Query().addCriteria(Criteria.where("jobInstance.id").is(jobInstance.getId())), JobExecution.class);
//    }
//
//
//    @Override
//    public JobExecution getLastJobExecution(JobInstance jobInstance) {
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("jobInstance.id").is(jobInstance.getId()));
//        PageRequest page = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
//        query.with(page);
//        return mongoTemplate.findOne(query, JobExecution.class);
//    }
//
//    @Override
//    public Set<JobExecution> findRunningJobExecutions(String jobName) {
//        JobInstance batchJobInstance = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("jobName").is(jobName)), JobInstance.class);
//        if (null == batchJobInstance)
//            return new HashSet<>();
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("startTime").exists(true).and("endTime").exists(true));
//        query.addCriteria(Criteria.where("jobInstance.id").is(batchJobInstance.getId()));
//        query.with(Sort.by(Sort.Direction.DESC, "id"));
//
//        List<JobExecution> batchJobExecutions = mongoTemplate.find(query, JobExecution.class);
//        return new HashSet<>(createJobExecutions(batchJobExecutions));
//    }
//
//    @Override
//    public JobExecution getJobExecution(Long executionId) {
//        return createJobExecution(mongoTemplate.findById(executionId, BatchJobExecution.class));
//    }
//
//    @Override
//    public void synchronizeStatus(JobExecution jobExecution) {
//        BatchJobExecution batchJobExecution = mongoTemplate.findById(jobExecution.getId(), BatchJobExecution.class);
//        if (null == batchJobExecution)
//            return;
//
//        int currentVersion = batchJobExecution.getVersion();
//
//        if (currentVersion != jobExecution.getVersion()) {
//            String status = batchJobExecution.getStatus();
//            jobExecution.upgradeStatus(BatchStatus.valueOf(status));
//            jobExecution.setVersion(currentVersion);
//        }
//    }
//
//    /**
//     * Validate JobExecution. At a minimum, JobId, StartTime, EndTime, and
//     * Status cannot be null.
//     *
//     * @param jobExecution jobExecution
//     */
//    private void validateJobExecution(JobExecution jobExecution) {
//
//        Assert.notNull(jobExecution, "jobExecution cannot be null");
//        Assert.notNull(jobExecution.getJobId(), "JobExecution Job-Id cannot be null.");
//        Assert.notNull(jobExecution.getStatus(), "JobExecution status cannot be null.");
//        Assert.notNull(jobExecution.getCreateTime(), "JobExecution create time cannot be null");
//    }
//
//    private List<JobExecution> createJobExecutions(List<BatchJobExecution> batchJobExecutions) {
//        return batchJobExecutions.stream().map(this::createJobExecution).collect(Collectors.toList());
//    }
//
//    private JobExecution createJobExecution(BatchJobExecution batchJobExecution) {
//        if (null == batchJobExecution)
//            return null;
//        JobExecution jobExecution = new JobExecution(
//                batchJobExecution.getId(),
//                (JobParameters) batchJobExecution.getJobParameters(),
//                batchJobExecution.getConfigurationName()
//        );
//
//        jobExecution.setStartTime(batchJobExecution.getStartAt());
//        jobExecution.setEndTime(batchJobExecution.getEndAt());
//        jobExecution.setStatus(BatchStatus.valueOf(batchJobExecution.getStatus()));
//        jobExecution.setExitStatus(new ExitStatus(batchJobExecution.getExitCode(), batchJobExecution.getExitMessage()));
//        jobExecution.setCreateTime(batchJobExecution.getCreateAt());
//        jobExecution.setLastUpdated(batchJobExecution.getUpdateAt());
//        jobExecution.setVersion(batchJobExecution.getVersion());
//        return jobExecution;
//    }
//}
