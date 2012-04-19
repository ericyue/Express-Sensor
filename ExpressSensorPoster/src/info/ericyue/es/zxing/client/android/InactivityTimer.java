package info.ericyue.es.zxing.client.android;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Finishes an activity after a period of inactivity if the device is on battery power.
 */
final class InactivityTimer {

  private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

  private final ScheduledExecutorService inactivityTimer =
      Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
  private final Activity activity;
  private ScheduledFuture<?> inactivityFuture = null;
  private final PowerStatusReceiver powerStatusReceiver = new PowerStatusReceiver();

  InactivityTimer(Activity activity) {
    this.activity = activity;
    onActivity();
  }

  void onActivity() {
    cancel();
    if (!inactivityTimer.isShutdown()) {
      try {
        inactivityFuture = inactivityTimer.schedule(new FinishListener(activity),
            INACTIVITY_DELAY_SECONDS,
            TimeUnit.SECONDS);
      } catch (RejectedExecutionException ree) {
        // surprising, but could be normal if for some reason the implementation just doesn't
        // think it can shcedule again. Since this time-out is non-essential, just forget it
      }
    }
  }

  public void onPause(){
    activity.unregisterReceiver(powerStatusReceiver);
  }

  public void onResume(){
    activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  }

  private void cancel() {
    if (inactivityFuture != null) {
      inactivityFuture.cancel(true);
      inactivityFuture = null;
    }
  }

  void shutdown() {
    cancel();
    inactivityTimer.shutdown();
  }

  private static final class DaemonThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setDaemon(true);
      return thread;
    }
  }

  private final class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
      if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        // 0 indicates that we're on battery
        // In Android 2.0+, use BatteryManager.EXTRA_PLUGGED
        if (intent.getIntExtra("plugged", -1) == 0) {
          InactivityTimer.this.onActivity();
        } else {
          InactivityTimer.this.cancel();
        }
      }
    }
  }

}
