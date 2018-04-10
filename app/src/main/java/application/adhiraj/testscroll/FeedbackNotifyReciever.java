package application.adhiraj.testscroll;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class FeedbackNotifyReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int ID = intent.getIntExtra("id", 0);

        notifyUser(context, ID);;
    }

    private void notifyUser (Context context, int ID){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.final_icon)
                        .setContentTitle("We would love to hear from you")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.final_icon))
                        .setContentText("Tap here to give feedback. We are always looking to improve.");
        Intent resultIntent = new Intent(context, RateAppointmentActivity.class);
        resultIntent.putExtra("id", ID);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AboutUsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        ID+7,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID+7, mBuilder.build());
    }
}
