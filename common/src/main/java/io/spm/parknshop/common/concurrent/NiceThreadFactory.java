package io.spm.parknshop.common.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wrapped thread factory for better use.
 *
 * @author Eric Zhao
 * @date 2017/12/01
 */
public class NiceThreadFactory implements ThreadFactory {

  private final ThreadGroup group;
  private final AtomicInteger threadNumber = new AtomicInteger(1);

  private final String namePrefix;

  public NiceThreadFactory(String namePrefix) {
    SecurityManager s = System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup() :
      Thread.currentThread().getThreadGroup();
    this.namePrefix = namePrefix;
  }

  public Thread newThread(Runnable r) {
    Thread t = new Thread(group, r,
      namePrefix + "-" + threadNumber.getAndIncrement(),
      0);
    if (t.isDaemon())
      t.setDaemon(false);
    if (t.getPriority() != Thread.NORM_PRIORITY)
      t.setPriority(Thread.NORM_PRIORITY);
    return t;
  }
}
