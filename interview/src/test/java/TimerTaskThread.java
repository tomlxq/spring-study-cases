/**
 *
 * @ClassName: TimerTaskThread 线程资源必须通过线程池提供，不允许在应用中自行显式创建线程
 * @Description:【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯
 * @Author: tomluo
 * @Date: 2022/12/17 15:57
 **/
public class TimerTaskThread extends Thread {
    public TimerTaskThread() {
        super.setName("TimerTaskThread");
        // ...
    }
}