package com.neevek.lib.message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Message {
    public final static int PRIORITY_NORMAL = 1;
    public final static int PRIORITY_HIGH = 2;
    public final static int PRIORITY_EXTREMELY_HIGH = 3;

    private static ConcurrentLinkedQueue<Message> mCachedMessagePool = new ConcurrentLinkedQueue<Message>();
    private final static int MAX_CACHED_MESSAGE_OBJ = 15;

    public static enum Type {
    	
        NONE,
        // this message is used to destroy the message pump,
        // we use the "Poison Pill Shutdown" approach, see: http://stackoverflow.com/a/812362/668963
        DESTROY_MESSAGE_PUMP,
        
        UPLOAD_LOGO_SUCCESS,
        UPDATE_USER_IFNO_SUCCESS,

        ADD_CUSTOMER_SUCCESS,//添加新客户成功

        NETWORK_UNAVAILABLE,
        LOG_OUT,
        UNKNOWN,
        UPDATE_FETCH_BRAND_COUNT,//更新未取货数量
        OTHER_1,
        OTHER_2,
        OTHER_3,

    }

    public Message(Type type, Object data, int priority, Object sender) {
        this.type = type;
        this.data = data;
        this.priority = priority;
        this.sender = sender;
    }

    public Message(Type type, Object data, int priority) {
        this(type, data, priority, null);
    }

    public Message(Type type, Object data) {
        this(type, data, PRIORITY_NORMAL, null);
    }

    public Message(Type type, int priority) {
        this(type, null, priority);
    }

    public void reset() {
        type = Type.NONE;
        data = null;
        priority = PRIORITY_NORMAL;
        sender = null;
    }

    public void recycle() {
        if (mCachedMessagePool.size() < MAX_CACHED_MESSAGE_OBJ) {
            reset();
            mCachedMessagePool.add(this);
        }
    }

    public static Message obtainMessage(Type messageType, Object data, int priority, Object sender) {
        Message message = mCachedMessagePool.poll();

        if (message != null) {
            message.type = messageType;
            message.data = data;
            message.priority = priority;
            message.sender = sender;

        } else {
            message = new Message(messageType, data, priority, sender);
        }

        return message;
    }

    public Type type;
    public Object data;
    public int priority;
    public Object sender;

    public int referenceCount;
}
