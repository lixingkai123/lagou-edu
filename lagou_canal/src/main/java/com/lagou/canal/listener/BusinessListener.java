package com.lagou.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 监控tb_ad表的数据变动
 * @author 元敬
 * @Version 1.0
 */
@CanalEventListener
public class BusinessListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ListenPoint(schema = "lagou_business",table = {"tb_ad"})
    public void adUpdate(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
        System.out.println("tb_ad表中的数据发生变化");
        //将修改后的数据发送的MQ中,只需要将position
        //tb_ad表存储了网站首页所有的缓存信息，不仅仅是首页广告。
        //只是首页广告的lua脚本，家电区写编写缓存更新和缓存加载的lua脚本。
        for(CanalEntry.Column column : rowData.getAfterColumnsList()){
            if(column.getName().equals("position")){
                System.out.println("发送消息到mq  ad_update_queue"+column.getValue());
                //发送position的值到MQ
                //将发送到mq，不管客户端有没有消费，只要是监听到变动就发送
                rabbitTemplate.convertAndSend("","ad_update_queue",column.getValue());
                //只要确定消费者已经接收到了消息，才会发送下一条消息
                //rabbitTemplate.convertSendAndReceive()
            }
        }
    }

}
