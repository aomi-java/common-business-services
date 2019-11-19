package tech.aomi.common.bs.review;

import software.sitb.common.entity.review.Review;
import software.sitb.common.entity.review.ReviewHistory;
import software.sitb.common.entity.system.Operator;

/**
 * 审核服务
 *
 * @author Sean(sean.snow @ live.com) Create At 2019-11-19
 */
public interface ReviewServices {

    /**
     * 审核
     *
     * @param clazz         审核信息真实对象class
     * @param reviewId      审核内容ID
     * @param operator      审核操作员
     * @param reviewHistory 审核结果
     */
    <T extends Review> T review(Class<T> clazz, String reviewId, Operator<?> operator, ReviewHistory reviewHistory);

}
