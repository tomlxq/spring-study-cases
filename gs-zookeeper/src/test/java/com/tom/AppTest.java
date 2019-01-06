package com.tom;

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);

        SortedSet<String> sortedSet = new TreeSet<String>();
        sortedSet.add("/LOCKS/000000007");
        sortedSet.add("/LOCKS/000000009");
        sortedSet.add("/LOCKS/000000008");
        sortedSet.add("/LOCKS/000000004");
        sortedSet.add("/LOCKS/000000006");
        sortedSet.add("/LOCKS/000000001");
        sortedSet.add("/LOCKS/000000002");
        sortedSet.add("/LOCKS/000000005");
        sortedSet.add("/LOCKS/000000003");

        System.out.println(sortedSet.first());
        sortedSet.remove("/LOCKS/000000001");
        System.out.println(sortedSet);
        SortedSet<String> lessThanLockId = sortedSet.headSet("/LOCKS/000000008");
        if (!lessThanLockId.isEmpty()) {
            //拿到比当前节点更小的上一个节点
            String preLockId = lessThanLockId.last();
            System.out.println(preLockId);
        }

        SortedSet<String> lessThanLockId1 = sortedSet.headSet("/LOCKS/000000004");
        if (!lessThanLockId1.isEmpty()) {
            //拿到比当前节点更小的上一个节点
            String preLockId = lessThanLockId1.last();
            System.out.println(preLockId);
        }
    }
}
