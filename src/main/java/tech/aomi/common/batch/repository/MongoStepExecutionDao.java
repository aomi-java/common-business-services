//package tech.aomi.common.batch.repository;
//
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.repository.dao.StepExecutionDao;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//import software.sitb.common.entity.batch.BatchStepExecution;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author Sean Create At 2020/4/1
// */
//public class MongoStepExecutionDao implements StepExecutionDao {
//
//    private MongoTemplate mongoTemplate;
//
//    private MongodbIdRepository idRepository;
//
//    public MongoStepExecutionDao(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//        this.idRepository = new MongodbIdRepository(mongoTemplate);
//    }
//
//    @Override
//    public void saveStepExecution(StepExecution stepExecution) {
//        validateStepExecution(stepExecution);
//        stepExecution.setId(idRepository.nextStepExecutionId());
//        stepExecution.incrementVersion(); //Should be 0
//
//        mongoTemplate.save(createStepExecution(stepExecution));
//    }
//
//    @Override
//    public void saveStepExecutions(Collection<StepExecution> stepExecutions) {
//        if (!CollectionUtils.isEmpty(stepExecutions)) {
//            stepExecutions.forEach(this::saveStepExecution);
//        }
//    }
//
//    @Override
//    public void updateStepExecution(StepExecution stepExecution) {
//        mongoTemplate.save(createStepExecution(stepExecution));
//    }
//
//    @Override
//    public StepExecution getStepExecution(JobExecution jobExecution, Long stepExecutionId) {
//        BatchStepExecution batchStepExecution = mongoTemplate.findById(stepExecutionId, BatchStepExecution.class);
//        return createStepExecution(jobExecution, batchStepExecution);
//    }
//
//    @Override
//    public void addStepExecutions(JobExecution jobExecution) {
//        List<BatchStepExecution> batchStepExecutions = mongoTemplate.find(new Query().addCriteria(Criteria.where("jobExecutionId").is(jobExecution.getId())), BatchStepExecution.class);
//        jobExecution.addStepExecutions(createStepExecutions(jobExecution, batchStepExecutions));
//    }
//
//
//    /**
//     * Validate StepExecution. At a minimum, JobId, StartTime, and Status cannot
//     * be null. EndTime can be null for an unfinished job.
//     */
//    private void validateStepExecution(StepExecution stepExecution) {
//        Assert.notNull(stepExecution, "stepExecution is required");
//        Assert.notNull(stepExecution.getStepName(), "StepExecution step name cannot be null.");
//        Assert.notNull(stepExecution.getStartTime(), "StepExecution start time cannot be null.");
//        Assert.notNull(stepExecution.getStatus(), "StepExecution status cannot be null.");
//    }
//
//    private BatchStepExecution createStepExecution(StepExecution stepExecution) {
//        BatchStepExecution batchStepExecution = new BatchStepExecution();
//        batchStepExecution.setId(stepExecution.getId());
//        batchStepExecution.setVersion(stepExecution.getVersion());
//
//        batchStepExecution.setName(stepExecution.getStepName());
//        batchStepExecution.setJobExecutionId(stepExecution.getJobExecutionId());
//        batchStepExecution.setStartAt(stepExecution.getStartTime());
//        batchStepExecution.setEndAt(stepExecution.getEndTime());
//        batchStepExecution.setStatus(stepExecution.getStatus().toString());
//        batchStepExecution.setCommitCount(stepExecution.getCommitCount());
//        batchStepExecution.setReadCount(stepExecution.getReadCount());
//        batchStepExecution.setWriteCount(stepExecution.getWriteCount());
//        batchStepExecution.setFilterCount(stepExecution.getFilterCount());
//        batchStepExecution.setRollbackCount(stepExecution.getRollbackCount());
//        batchStepExecution.setReadSkipCount(stepExecution.getReadSkipCount());
//        batchStepExecution.setProcessSkipCount(stepExecution.getProcessSkipCount());
//        batchStepExecution.setWriteSkipCount(stepExecution.getWriteSkipCount());
//        batchStepExecution.setExitCode(stepExecution.getExitStatus().getExitCode());
//        batchStepExecution.setExitMessage(stepExecution.getExitStatus().getExitDescription());
//        batchStepExecution.setUpdateAt(stepExecution.getLastUpdated());
//
//        return batchStepExecution;
//    }
//
//    private List<StepExecution> createStepExecutions(JobExecution jobExecution, List<BatchStepExecution> batchStepExecutions) {
//        return batchStepExecutions.stream().map(item -> createStepExecution(jobExecution, item)).collect(Collectors.toList());
//    }
//
//    private StepExecution createStepExecution(JobExecution jobExecution, BatchStepExecution batchStepExecution) {
//        if (null == batchStepExecution)
//            return null;
//        StepExecution stepExecution = new StepExecution(batchStepExecution.getName(), jobExecution);
//        stepExecution.setId(batchStepExecution.getId());
//        stepExecution.setVersion(batchStepExecution.getVersion());
//        stepExecution.setStartTime(batchStepExecution.getStartAt());
//        stepExecution.setEndTime(batchStepExecution.getEndAt());
//        stepExecution.setStatus(BatchStatus.valueOf(batchStepExecution.getStatus()));
//        stepExecution.setCommitCount(batchStepExecution.getCommitCount());
//        stepExecution.setReadCount(batchStepExecution.getReadCount());
//        stepExecution.setWriteCount(batchStepExecution.getWriteCount());
//        stepExecution.setFilterCount(batchStepExecution.getFilterCount());
//        stepExecution.setRollbackCount(batchStepExecution.getRollbackCount());
//        stepExecution.setReadSkipCount(batchStepExecution.getReadSkipCount());
//        stepExecution.setProcessSkipCount(batchStepExecution.getProcessSkipCount());
//        stepExecution.setWriteSkipCount(batchStepExecution.getWriteSkipCount());
//        stepExecution.setExitStatus(new ExitStatus(batchStepExecution.getExitCode(), batchStepExecution.getExitMessage()));
//        stepExecution.setLastUpdated(batchStepExecution.getUpdateAt());
//
//        return stepExecution;
//    }
//
//}
