package com.example.study.jvm;

import java.util.ArrayList;
import java.util.List;

public class MemoryMonitoringTest {
    
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }
    
    public static void fillHeap(int num) {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            list.add(new OOMObject());
        }
        
        System.gc();
    }
    
    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        while (true) {
            System.out.println("대기 시작");
            Thread.sleep(1000);
        }
    }
}
