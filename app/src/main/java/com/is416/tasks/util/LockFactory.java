package com.is416.tasks.util;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LockFactory is intended to be used to create & access locks across threads.
 * The locks are named (via a String) and accessed by that name.
 * Internally, there is a map from lockName to lock object.
 *
 * All locks are ReadWriteLocks.
 */
public class LockFactory
{
    /*
        Note that we expect concurrent access to the LockFactory itself!  This means that
        adding new locks and acquiring existing locks must be ... locked!  We do this
        by using the JDK's ConcurrentHashMap which guarantees thread-safe access to the
        underlying map.
     */
    private static final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();


    public static ReadWriteLock getLock(String lockName)
    {
        return getReadWriteLock(lockName);
    }

    public static ReadWriteLock getReadWriteLock(String lockName)
    {

        ReadWriteLock lock = (ReadWriteLock) map.putIfAbsent(lockName, new ReentrantReadWriteLock());
        if (lock == null)
            lock = (ReadWriteLock) map.get(lockName);

        return lock;

    }

    public static void main(String[] args)
    {
        //Test the Factory
        Lock reentrantLock = LockFactory.getLock("RL a").writeLock();
        assert(reentrantLock != null);

        Lock lock = LockFactory.getLock("RL b").writeLock();
        assert(lock != null);

        ReadWriteLock rwLock = LockFactory.getReadWriteLock("RWL a");
        assert(rwLock != null);

    }
}
