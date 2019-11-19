package tech.aomi.common.bs.review;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import software.sitb.common.entity.review.Review;
import software.sitb.common.entity.review.ReviewHistory;
import software.sitb.common.entity.review.ReviewResult;
import software.sitb.common.entity.review.ReviewUser;
import software.sitb.common.entity.system.Operator;
import tech.aomi.common.exception.PermissionException;
import tech.aomi.common.exception.ResourceNonExistException;
import tech.aomi.common.exception.ResourceStatusException;
import tech.aomi.common.exception.ServiceException;

import java.util.Date;
import java.util.List;

import static software.sitb.common.entity.review.ReviewStatus.FINISH;

/**
 * 审核默认实现
 *
 * @author Sean(sean.snow @ live.com) Create At 2019-11-19
 */
@Slf4j
@Service
public class ReviewServicesImpl implements ReviewServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public <T extends Review> T review(Class<T> clazz, String reviewId, Operator<?> operator, ReviewHistory reviewHistory) {
        Assert.hasLength(reviewId, "审核流程ID不能为空");
        T review = mongoTemplate.findById(reviewId, clazz);
        if (null == review) {
            LOGGER.error("审核流程不存在: {}, {}", clazz.getName(), reviewId);
            throw new ResourceNonExistException("审核流程不存在" + reviewId);
        }
        if (review.getStatus() == FINISH) {
            LOGGER.error("流程已经审核完毕: {}", reviewId);
            throw new ResourceStatusException("流程已经审核完毕，不能重复审核");
        }

        ReviewUser reviewUser;
        try {
            reviewUser = review.getReviewProcess().getChain().get(review.getCurrentReviewUserIndex());
        } catch (Exception e) {
            LOGGER.error("无法从审核链中获取当前审核用户信息:currentReviewUserIndex = {}", review.getCurrentReviewUserIndex());
            throw new ServiceException("无法从审核链中获取当前审核用户信息");
        }

        boolean isReview = false;
        if (StringUtils.isNotEmpty(reviewUser.getRoleId())) {
            isReview = operator.getRoles().stream().anyMatch(item -> item.getId().equals(reviewUser.getRoleId()));
            if (isReview) {
                LOGGER.debug("用户角色在可审核角色列表中");
            }
        } else if (StringUtils.isNotEmpty(reviewUser.getUserId()) && operator.getId().equals(reviewUser.getUserId())) {
            LOGGER.debug("用户在属于指定的审核员");
            isReview = true;
        }
        if (!isReview) {
            throw new PermissionException("当前操作员不能审核");
        }

        reviewHistory.setReviewAt(new Date());
        review.getHistories().add(reviewHistory);
        if (reviewHistory.getResult() == ReviewResult.REJECTED) {
            LOGGER.debug("用户{}选择拒绝申请，审核流程结束", operator.getId());
            review.setStatus(FINISH);
            review.setResult(ReviewResult.REJECTED);
            review.setResultDescribe(reviewHistory.getDescribe());
            return mongoTemplate.save(review);
        }

        List<ReviewUser> chain = review.getReviewProcess().getChain();
        if (review.getNextReviewUserIndex() == chain.size()) {
            LOGGER.debug("审核流程完成,审核通过");
            review.setStatus(FINISH);
            review.setResult(ReviewResult.RESOLVE);
            review.setResultDescribe(reviewHistory.getDescribe());
            return mongoTemplate.save(review);
        }
        review.setCurrentReviewUserIndex(review.getNextReviewUserIndex());
        review.setNextReviewUserIndex(review.getNextReviewUserIndex() + 1);
        return mongoTemplate.save(review);
    }

}
