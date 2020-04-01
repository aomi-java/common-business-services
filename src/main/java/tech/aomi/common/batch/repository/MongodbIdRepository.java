//package tech.aomi.common.batch.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import tech.aomi.common.batch.entity.BatchJobExecution;
//import tech.aomi.common.batch.entity.BatchJobInstance;
//import tech.aomi.common.batch.entity.BatchStepExecution;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Sean(sean.snow @ live.com) createAt 17-12-26.
// */
//@Slf4j
//public class MongodbIdRepository {
//
//    private static final String SEQUENCE_COLLECTION = "DBSequence";
//
//    private MongoTemplate mongoTemplate;
//
//    public MongodbIdRepository(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    public Long getJobInstanceId() {
//        return generateNoSqlSequence(BatchJobInstance.class.getSimpleName());
//    }
//
//    public Long getJobExecutionId() {
//        return generateNoSqlSequence(BatchJobExecution.class.getSimpleName());
//    }
//
//    public Long nextStepExecutionId() {
//        return generateNoSqlSequence(BatchStepExecution.class.getSimpleName());
//    }
//
//    public void createSequence(String sequenceName) {
//        boolean exists = mongoTemplate.exists(
//                new Query(Criteria.where("id")
//                        .is(sequenceName)),
//                String.class,
//                SEQUENCE_COLLECTION
//        );
//        if (!exists) {
//            Map<String, Object> sequence = new HashMap<>();
//            sequence.put("_id", sequenceName);
//            sequence.put("value", 1L);
//            mongoTemplate.insert(sequence, SEQUENCE_COLLECTION);
//        }
//    }
//
//    private Long generateNoSqlSequence(String sequenceName) {
//        HashMap seq = mongoTemplate.findAndModify(
//                new Query(Criteria.where("_id")
//                        .is(sequenceName)),
//                new Update().inc("value", 1L),
//                HashMap.class,
//                SEQUENCE_COLLECTION
//        );
//        if (null == seq) {
//            LOGGER.info("序列{}不存在,自动创建", sequenceName);
//            createSequence(sequenceName);
//            return generateNoSqlSequence(sequenceName);
//        }
//        return (Long) seq.get("value");
//    }
//}
