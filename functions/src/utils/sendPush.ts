import * as admin from "firebase-admin";

export async function sendPushNotification(token: string, title: string, body: string, data: any = {}) {
  const payload: admin.messaging.Message = {
    token: token,
    notification: {
      title: title,
      body: body,
    },
    data: data,
    android: {
      priority: "high",
      notification: {
        sound: "default",
        clickAction: "FLUTTER_NOTIFICATION_CLICK",
      },
    },
  };
  try {
    await admin.messaging().send(payload);
    console.log("Notification sent successfully with high priority");
  } catch (error) {
    console.error("Error sending notification:", error);
  }
}