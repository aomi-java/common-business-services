//package tech.aomi.common.batch.repository;
//
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.launch.NoSuchJobException;
//import org.springframework.batch.core.repository.dao.JobInstanceDao;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.lang.Nullable;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import tech.aomi.common.batch.entity.BatchJobInstance;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author Sean Create At 2020/3/31
// */
//public class MongoJobInstanceDao implements JobInstanceDao {
//
//    private MongoTemplate mongoTemplate;
//
//    private MongodbIdRepository idRepository;
//
//    private JobKeyGenerator<JobParameters> jobKeyGenerator = new DefaultJobKeyGenerator();
//
//    public MongoJobInstanceDao(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//        this.idRepository = new MongodbIdRepository(mongoTemplate);
//    }
//
//    @Override
//    public JobInstance createJobInstance(String jobName, JobParameters jobParameters) {
//        Assert.notNull(jobName, "Job name must not be null.");
//        Assert.notNull(jobParameters, "JobParameters must not be null.");
//
//        Assert.state(getJobInstance(jobName, jobParameters) == null, "JobInstance must not already exist");
//
//        Long jobId = idRepository.getJobInstanceId();
//        String jobKey = jobKeyGenerator.generateKey(jobParameters);
//
//        BatchJobInstance batchJobInstance = new BatchJobInstance();
//        batchJobInstance.setId(jobId);
//        batchJobInstance.incrementVersion();
//        batchJobInstance.setName(jobName);
//        batchJobInstance.setKey(jobKey);
//        mongoTemplate.save(batchJobInstance);
//
//        return createJobInstance(batchJobInstance);
//    }
//
//    @Override
//    @Nullable
//    public JobInstance getJobInstance(String jobName, JobParameters jobParameters) {
//        Assert.notNull(jobName, "Job name must not be null.");
//        Assert.notNull(jobParameters, "JobParameters must not be null.");
//
//        String jobKey = jobKeyGenerator.generateKey(jobParameters);
//        BatchJobInstance batchJobInstance = getBatchJobInstance(jobName, jobKey);
//        return createJobInstance(batchJobInstance);
//    }
//
//    @Override
//    public JobInstance getJobInstance(Long instanceId) {
//        return createJobInstance(mongoTemplate.findById(instanceId, BatchJobInstance.class));
//    }
//
//    @Override
//    public JobInstance getJobInstance(JobExecution jobExecution) {
//        return createJobInstance(mongoTemplate.findById(jobExecution.getJobId(), BatchJobInstance.class));
//    }
//
//    @Override
//    public List<JobInstance> getJobInstances(String jobName, int start, int count) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("name").is(jobName))
//                .skip(start)
//                .limit(count);
//
//        List<BatchJobInstance> batchJobInstances = mongoTemplate.find(query, BatchJobInstance.class);
//        return createJobInstance(batchJobInstances);
//    }
//
//
//    @Override
//    public List<String> getJobNames() {
//        return mongoTemplate.findDistinct("name", BatchJobInstance.class, String.class);
//    }
//
//    @Override
//    public List<JobInstance> findJobInstancesByName(String jobName, int start, int count) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("name").regex(".*" + jobName + ".*"))
//                .skip(start)
//                .limit(count);
//
//        List<BatchJobInstance> batchJobInstances = mongoTemplate.find(query, BatchJobInstance.class);
//        return createJobInstance(batchJobInstances);
//    }
//
//    @Override
//    public int getJobInstanceCount(String jobName) throws NoSuchJobException {
//        return (int) mongoTemplate.count(new Query().addCriteria(Criteria.where("name").is(jobName)), BatchJobInstance.class);
//    }
//
//    private BatchJobInstance getBatchJobInstance(String jobName, String jobKey) {
//        BatchJobInstance batchJobInstance;
//        Query query = new Query();
//        if (StringUtils.hasLength(jobKey)) {
//            query.addCriteria(Criteria.where("name").is(jobName).and("key").is(jobKey));
//            batchJobInstance = mongoTemplate.findOne(query, BatchJobInstance.class);
//        } else {
//            query.addCriteria(Criteria.where("name").is(jobName));
//            batchJobInstance = mongoTemplate.findOne(query, BatchJobInstance.class);
//        }
//        return batchJobInstance;
//    }
//
//    private List<JobInstance> createJobInstance(List<BatchJobInstance> batchJobInstances) {
//        return batchJobInstances.stream().map(this::createJobInstance).collect(Collectors.toList());
//    }
//
//    private JobInstance createJobInstance(BatchJobInstance batchJobInstance) {
//        if (null == batchJobInstance)
//            return null;
//        JobInstance jobInstance = new JobInstance(batchJobInstance.getId(), batchJobInstance.getName());
//        jobInstance.setVersion(batchJobInstance.getVersion());
//        return jobInstance;
//    }
//}
