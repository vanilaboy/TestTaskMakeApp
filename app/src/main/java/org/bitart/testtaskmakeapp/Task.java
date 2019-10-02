package org.bitart.testtaskmakeapp;

import java.util.UUID;

public class Task {

    public static final int LOW_PRIOROTY = 0;
    public static final int MEDIUM_PRIOROTY = 1;
    public static final int HIGH_PRIOROTY = 2;

    private UUID mId;
    private String mHeader;
    private String mBody;
    private int mPriority = LOW_PRIOROTY;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        mId = id;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
