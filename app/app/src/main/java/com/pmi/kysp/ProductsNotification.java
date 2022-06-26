package com.pmi.kysp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ProductsNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String product_name = intent.getExtras().getString("productName");
        String barcode = intent.getExtras().getString("barcode");
        boolean isExpiredProduct = intent.getExtras().getBoolean("isExpired");
        int requestCode = intent.getExtras().getInt("requestCode");

        Intent productIntent = new Intent(context, ProductActivity.class);
        productIntent.putExtra("barcode", barcode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode * 1000000000, productIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ProductsNotification");

        if (isExpiredProduct) {
            builder.setContentTitle("Просроченный продукт");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("Продукт '%s' просрочен",product_name)));
        }else{
            builder.setContentTitle("Продукт скоро испортится");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("Продукт '%s' скоро испортится",product_name)));
        }
        builder.setSmallIcon(R.mipmap.ic_notification);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(requestCode, builder.build());
    }

}