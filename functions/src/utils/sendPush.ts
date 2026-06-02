import * as admin from "firebase-admin";

export async function sendPushNotification(token: string, title: string, body: string, data: any = {}) {
  const payload = {
    token: token,
    notification: {
      title: title,
      body: body,
    },
    data: data,
  };

  try {
    await admin.messaging().send(payload);
    console.log("Notification sent successfully");
  } catch (error) {
    console.error("Error sending notification:", error);
  }
}