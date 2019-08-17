package com.city.sprinbboot.database2code.com.farsunset.cim.sdk.server.model;

import java.io.Serializable;


/**
 * 消息对象
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     */
    private String mid;

    /**
     * 消息类型，用户自定义消息类别
     */
    private String action;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息类容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     */
    private String content;

    /**
     * 消息发送者账号
     */
    private String sender;
    /**
     * 消息发送者接收者</br>
     * 如果消息只发送给一个人，这里存放的是发送者的用户id</br>
     * 如果消息发送的是一群人，这里存放的是一群的人公共id,并且把所有需要发送的用户id存入到receivers</br>
     * 如果消息是发送一群人， 其中一个人接收就可以了，这里存放一群的人公共id,并且receivers是null</br>
     * 如果是发送给所有人，这里存放"all",并且把所有需要发送的用户id存入到receivers</br>
     */
    private String receiver;

    /**
     * content 内容格式
     */
    private String format;

    /**
     * 附加内容 内容
     */
    private String extra;

    /**
     * 群消息接收的所有用户id
     */
    private String receivers;

    /**
     * 是否要用户返回确认信息
     */
    private boolean isCallback;
    /**
     * 客户端点击的地址
     */
    private String linkurl;
    private long timestamp;

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public boolean getIsCallback() {
        return isCallback;
    }

    public void setIsCallback(boolean isCallback) {
        this.isCallback = isCallback;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public Message() {
        timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("#Message#").append("\n");
        buffer.append("mid:").append(mid).append("\n");
        buffer.append("action:").append(action).append("\n");
        buffer.append("title:").append(title).append("\n");
        buffer.append("content:").append(content).append("\n");
        buffer.append("extra:").append(extra).append("\n");
        buffer.append("sender:").append(sender).append("\n");
        buffer.append("receiver:").append(receiver).append("\n");
        buffer.append("format:").append(format).append("\n");
        buffer.append("timestamp:").append(timestamp).append("\n");
        buffer.append("isCallback:").append(isCallback).append("\n");
        buffer.append("receivers:").append(receivers).append("\n");
        buffer.append("linkurl:").append(linkurl).append("\n");
        return buffer.toString();
    }

}
