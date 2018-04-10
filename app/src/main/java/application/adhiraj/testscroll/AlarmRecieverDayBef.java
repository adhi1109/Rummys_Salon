package application.adhiraj.testscroll;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Adhiraj on 12/06/17.
 */

public class AlarmRecieverDayBef extends BroadcastReceiver {
    private int Flag;

    public AlarmRecieverDayBef() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Flag = intent.getFlags();

        ArrayList<String> data = new ArrayList<String>();
        data = intent.getStringArrayListExtra("details");

        String details = data.get(0)+" at "+data.get(1);

        if (Flag == 21){
            notifyUser(context, Integer.parseInt(data.get(2))+5, details);
        }else if (Flag ==20){
            notifyUser(context,Integer.parseInt(data.get(2))+2, details+" LT");
        }else {
            Log.e("Error", "Flag is: "+Flag);
        }

        //attachDatabase(context, userEmail);
    }


    private void notifyUser (Context context, int ID, String date){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.final_icon)
                        .setContentTitle("We are waiting for you!")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.final_icon))
                        .setContentText(date);
        Intent resultIntent = new Intent(context, AboutUsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AboutUsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        ID,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID, mBuilder.build());
    }

}
