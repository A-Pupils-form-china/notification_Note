package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyService extends Service {
    String[] data={"clean up after sb","species","await","emancipation","tedium","household","harbor",
                   "work out to","upward of","justification","lavish","in return","investment","out there",
                   "back up","Rx","prompt","multitude","couch","every now and then","neurochemical","incredibly",
                    "comforting","slobbery","ultimate","antidote","stressful","spouse","recovery","encounter","reduction",
                    "presence","distract","cortisol","depression","elevate","oxytocin","furry","aquarium","relaxant","proven",
                    "meditative","companionship","coin","gossipy","loyal","nonjudgmental","rank","turn to","company","veterinary",
                    "respondent","confide","soulful","reveal","integral","extend","strike up","wheelchair","quality","magnet","infirm",
                    "maintain","stimulation","caretaker","AIDS","succumb","dolphin"};
    MyBroadcastReceiver my;
    int index;
    NotificationManagerCompat notificationManager;
    Intent previousInt;
    Intent nextInt;
    Intent exitInt;
    PendingIntent previous;
    PendingIntent next;
    PendingIntent exit;
    int offset = 8;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "被杀");
        unregisterReceiver(my);
        notificationManager.cancel(1);

    }
    public int onStartCommand(Intent intent, int flags, int startId){
        index = 0;
        Log.d("TAG", "服务启动");
        my = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.myapplication.COMMAND");
        registerReceiver(my, filter);
        previousInt = new Intent("com.example.myapplication.COMMAND");
        nextInt = new Intent("com.example.myapplication.COMMAND");
        exitInt = new Intent("com.example.myapplication.COMMAND");
        previousInt.putExtra("command", "previous");
        nextInt.putExtra("command", "next");
        exitInt.putExtra("command", "exit");
        previous = PendingIntent.getBroadcast(this, 1, previousInt, PendingIntent.FLAG_CANCEL_CURRENT);
        next = PendingIntent.getBroadcast(this, 2, nextInt, PendingIntent.FLAG_CANCEL_CURRENT);
        exit = PendingIntent.getBroadcast(this, 3, exitInt, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationManager = NotificationManagerCompat.from(this);
        String temp = data[index]+"\t\t\t\t\t"+data[index+1]+'\n'+data[index+2]+"\t\t\t\t\t"+data[index+3]+
                '\n'+data[index+4]+"\t\t\t\t\t"+data[index+5]+"\n"+data[index+6]+"\t\t\t\t\t"+data[index+7];
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "emm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.previous,"上一个", previous)
                .addAction(R.drawable.next, "下一个", next)
                .addAction(R.drawable.makefg, "退出", exit)
                .setContentTitle(String.valueOf(index))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(temp))
                .setSmallIcon(R.drawable.makefg);
        notificationManager.notify(1, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }
    public void send(String command){
        switch(command){
            case "previous":
                if(index-offset<0)
                    index=offset;
                index-=offset;
                break;
            case "next":
                if(index+offset<64)
                    index+=offset;
                break;
            case "exit":
                stopService(new Intent(this, MyService.class));
        }
        String temp = data[index]+"\t\t\t\t\t"+data[index+1]+'\n'+data[index+2]+"\t\t\t\t\t"+data[index+3]+
                '\n'+data[index+4]+"\t\t\t\t\t"+data[index+5]+"\n"+data[index+6]+"\t\t\t\t\t"+data[index+7];
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "emm")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.previous,"上一个", previous)
                .addAction(R.drawable.next, "下一个", next)
                .addAction(R.drawable.makefg, "退出", exit)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(temp))
                .setContentTitle(String.valueOf(index))
                .setSmallIcon(R.drawable.makefg);
        notificationManager.notify(1, builder.build());

    }
    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            send(intent.getStringExtra("command"));
        }
    }
}