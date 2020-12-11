package club.nsuer.nsuer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class ReminderBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        SessionManager sessionManager = new SessionManager(context);

        if (sessionManager.isLoggedIn()) {

            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

                ScheduleDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                        ScheduleDatabase.class, "schedule").fallbackToDestructiveMigration().allowMainThreadQueries().build();

                List<ScheduleEntity> list = db.scheduleDao().getAll();

                for (int i = 0; i < list.size(); i++) {

                    int id = list.get(i).getId();
                    String title = list.get(i).getTitle();
                    String type = list.get(i).getType();
                    String note = list.get(i).getExtraNote();
                    long date = list.get(i).getDate();
                    long reminderDate = list.get(i).getReminderDate();
                    int color = list.get(i).getColor();
                    boolean doRemind = list.get(i).isDoReminder();

                    boolean isPassed = false;
                    long currentTime = Calendar.getInstance().getTimeInMillis();

                    if (currentTime > (reminderDate * 1000L))
                        isPassed = true;
                    String reminderText = title;
                    if (!type.equals(""))
                        reminderText += " - " + type;

                    if (doRemind && !isPassed)
                        setReminder(id, reminderText, reminderDate, context);

                }

                if (list.size() == 0) {

                    Utils.syncSchedule(sessionManager.getMemberID(), context);

                }


            }
        }


    }


    public void setReminder(int idd, String reminderText, long time, Context context) {
        int timeInSec = 10;

        Intent intent = new Intent(context, ReminderBroadcast.class);

        intent.putExtra("text", reminderText);
        intent.putExtra("id", idd);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), idd, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time * 1000L, pendingIntent);
    }


}