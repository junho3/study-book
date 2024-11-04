package com.example.study.jvm;

/**
 * 가상 머신은 기본적으로 자바 스레드를 운영 체제의 커널 스레드와 매핑시키므로 스레드를 무한정 만들면 운영 체제에 엄청난 압박을 준다.
 * 스레드를 너무 많이 만들어서 운영 체제가 멈춰 버릴 수도 있다.
 */
public class JavaVMStackOOM {
    private void dontStop() {
        while (true) {
            //
        }
    }
    
    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            
            thread.start();
        }
    }
    
    public static void main(String[] args) throws Throwable {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
