/**
 * 
 */
package com.z.gateway.service.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.z.gateway.common.util.NetworkUtil;
import com.z.gateway.service.IdService;

/**
 * @author sunff
 * 
 */
public class StringSnowFlakeIdServiceImpl implements IdService {

    @Override
    public String genInnerRequestId() {
        // TODO Auto-generated method stub
        return nextId();
    }

    private final long workerId;
    private final long datacenterId;
    private final long idepoch;

    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    private static final long sequenceBits = 13L;
    // private static final long workerIdShift = sequenceBits;
    // private static final long datacenterIdShift = sequenceBits +
    // workerIdBits;
    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;
    private long sequence;

    // private static final Random r = new Random();

    /*
     * public StringIdServiceImpl() { int workerId =
     * Math.abs(NetworkUtil.getLocalMac().hashCode()) % 31; int datacenterId =
     * Math.abs(NetworkUtil.getLocalHost().hashCode()) % 31;
     * this(Long.valueOf(workerId+""),Long.valueOf( datacenterId+""), 0L,
     * 1344322705519L); }
     */

    //
    public StringSnowFlakeIdServiceImpl() {
        this.workerId = Long.valueOf(Math.abs(NetworkUtil.getLocalMac().hashCode()) % 7);
        this.datacenterId = Long.valueOf(Math.abs(NetworkUtil.getLocalHost().hashCode()) % 7);
        this.sequence = 0L;
        this.idepoch = 1344322705519L;
        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("workerId is illegal: " + workerId);
        }
        if (datacenterId < 0 || datacenterId > maxDatacenterId) {
            throw new IllegalArgumentException("datacenterId is illegal: " + workerId);
        }
        if (idepoch >= System.currentTimeMillis()) {
            throw new IllegalArgumentException("idepoch is illegal: " + idepoch);
        }
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    private static final DateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private synchronized String nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.");
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        StringBuilder sb = new StringBuilder(f.format(new Date(timestamp)));
        sb.append(datacenterId);
        sb.append(workerId);
        sb.append(String.format("%04d", sequence));
        return sb.toString();
    }

    /**
     * get the timestamp (millis second) of id
     * 
     * @param id
     *            the nextId
     * @return the timestamp of id
     */
    public long getIdTimestamp(long id) {
        return idepoch + (id >> timestampLeftShift);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
