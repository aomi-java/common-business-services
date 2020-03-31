package tech.aomi.common.netty;

import io.netty.channel.ChannelPipeline;

/**
 * netty网络服务接口定义
 *
 * @author 田尘殇Sean Create At 2018/10/15 10:59
 */
public interface NettyNetwork {

    String getHost();

    int getPort();

    /**
     * 添加额外的消息编码器
     *
     * @param pipeline 管道
     */
    void addHandler(ChannelPipeline pipeline);

}
