package com.pmi.kysp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductsNotificationManager {
    Context context;

    public ProductsNotificationManager(Context context){
        this.context = context;
    }

    public void setNotifications() {
        List<Product> products = getListOfProducts();
        for (Product product : products) {
            setNotification(product);
        }
    }

    public void disableNotifications(){
        List<Product> products = getListOfProducts();
        for (Product product : products){
            disableNotification(product);
        }
    }

    public void updateNotifications(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SettingsNotification", context.MODE_PRIVATE);
        boolean switchNotificationsValue = sharedPreferences.getBoolean(SettingsActivity.SWITCH_NOTIFICATIONS, true);
        disableNotifications();
        if (switchNotificationsValue)
            setNotifications();
    }

    public void setNotification(Product product){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar notificationTime = getCurrentTimeNotification(false);
        Calendar notificationTimeNextDay = getCurrentTimeNotification(true);
        Intent alertIntent = new Intent(context, ProductsNotification.class);
        int requesCode = convertBarcodeToInt(product.getBarcode());
        alertIntent.putExtra("productName", product.getProductName());
        alertIntent.putExtra("barcode", product.getBarcode());
        alertIntent.putExtra("isExpired", true);
        alertIntent.putExtra("requestCode", -requesCode);
        Long alertTime = calculatingTimeForExpiredProduct(product);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alertTime, PendingIntent.getBroadcast(context,-requesCode , alertIntent, PendingIntent.FLAG_UPDATE_CURRENT)), PendingIntent.getBroadcast(context,-requesCode , alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        if (!product.isExpired()){
            alertIntent = new Intent(context, ProductsNotification.class);
            alertIntent.putExtra("productName", product.getProductName());
            alertIntent.putExtra("barcode", product.getBarcode());
            alertIntent.putExtra("isExpired", false);
            alertIntent.putExtra("requestCode", requesCode);
            alertTime = calculatingTimeBeforeNotification(product);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alertTime, PendingIntent.getBroadcast(context,requesCode , alertIntent, PendingIntent.FLAG_UPDATE_CURRENT)), PendingIntent.getBroadcast(context,requesCode , alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }

    }

    public void disableNotification(Product product){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alertIntent = new Intent(context, ProductsNotification.class);
        int requestCode = convertBarcodeToInt(product.getBarcode());
        alarmManager.cancel(PendingIntent.getBroadcast(context,requestCode,alertIntent,0));
        alarmManager.cancel(PendingIntent.getBroadcast(context,-requestCode,alertIntent,0));
    }

    private int convertBarcodeToInt(String barcode){
        String new_barcode = "";
        for (int i = 0; i < barcode.length(); ++i)
            if (barcode.length() - i < 10 && i != barcode.length() - 1)
                new_barcode += barcode.charAt(i);
        return Integer.valueOf(new_barcode);
    }

    private List<Product> getListOfProducts(){
        LocalDBManager localDBManager = new LocalDBManager(context);
        List<String> barcodes = localDBManager.getBarcodes();
        List<Product> products = ProductsApi.getProducts(barcodes);
        products.forEach(p -> {
            String barcode = p.getBarcode();
            LocalDate manufactureDate = null;
            try {
                manufactureDate = localDBManager.getManufactureDate(barcode);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.updateExpDate(manufactureDate);
        });
        return products;
    }

    private Calendar getCurrentTimeNotification(boolean nextDay){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SettingsNotification", context.MODE_PRIVATE);
        String timeNotification = sharedPreferences.getString(SettingsActivity.TIME_TEXT, "17:00");
        int timeNotificationHours = Integer.valueOf(Character.toString(timeNotification.charAt(0)) + Character.toString(timeNotification.charAt(1)));
        int timeNotificationMinutes = Integer.valueOf(Character.toString(timeNotification.charAt(3)) + Character.toString(timeNotification.charAt(4)));

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, timeNotificationHours);
        notificationTime.set(Calendar.MINUTE, timeNotificationMinutes);
        notificationTime.set(Calendar.SECOND, 0);

        if (nextDay)
            notificationTime.add(Calendar.DAY_OF_MONTH, 1);

        return notificationTime;
    }

    private Long calculatingTimeBeforeNotification(Product product){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SettingsNotification", context.MODE_PRIVATE);
        int daysNotification = sharedPreferences.getInt(SettingsActivity.NUMBER_PICKER_DAYS, 1);
        Calendar currentTimeNotification = getCurrentTimeNotification(false);

        if (product.getExpiringDate() > daysNotification){
            currentTimeNotification.add(Calendar.DAY_OF_MONTH, product.getExpiringDate() - daysNotification);
        }else{
            Calendar currentTime = Calendar.getInstance();
            if (currentTime.getTimeInMillis() > currentTimeNotification.getTimeInMillis())
                currentTimeNotification = getCurrentTimeNotification(true);
        }
        return currentTimeNotification.getTimeInMillis();
    }

    private Long calculatingTimeForExpiredProduct(Product product){
        Calendar currentTime = Calendar.getInstance();
        Calendar currentTimeNotification = getCurrentTimeNotification(false);
        Calendar notificationTimeNextDay = getCurrentTimeNotification(true);
        if (product.isExpired()) {
            if (currentTime.getTimeInMillis() > currentTimeNotification.getTimeInMillis())
                return notificationTimeNextDay.getTimeInMillis();
            return currentTimeNotification.getTimeInMillis();
        }
        currentTimeNotification.add(Calendar.DAY_OF_MONTH, product.getExpiringDate());
        return currentTimeNotification.getTimeInMillis();
    }

}
