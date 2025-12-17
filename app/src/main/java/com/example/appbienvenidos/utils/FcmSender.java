package com.example.appbienvenidos.utils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FcmSender {
    private static final String SERVER_KEY = "AAAA.... (Ta longue clé ici)";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static void sendNotification(String userToken, String title, String message) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                // Le format JSON attendu par Firebase
                JSONObject json = new JSONObject();
                json.put("to", userToken);

                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);

                json.put("notification", notification);

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(FCM_URL)
                        .addHeader("Authorization", "key=" + SERVER_KEY)
                        .post(body)
                        .build();

                client.newCall(request).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
