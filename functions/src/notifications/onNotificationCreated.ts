import { onValueCreated } from "firebase-functions/v2/database";
import * as admin from "firebase-admin";
import { sendPushNotification } from "../utils/sendPush";

export const onNotificationCreated = onValueCreated(
  "/notifications/{recipientUid}/{notifId}",
  async (event) => {
    const notificationData = event.data.val();
    const recipientUid = event.params.recipientUid;

    const userSnapshot = await admin.database().ref(`/users/${recipientUid}/fcmToken`).get();

    if (!userSnapshot.exists()) {
      console.log(`No token found for user: ${recipientUid}`);
      return;
    }

    const fcmToken = userSnapshot.val();

    let title = "Nueva notificación";
    let body = "Tienes una nueva actividad en StellarVision";

    if (notificationData.type === "like") {
      title = `A ${notificationData.fromUsername} le gustó tu post`;
      body = "Toca para ver quién reaccionó.";
    } else if (notificationData.type === "comment") {
      title = `${notificationData.fromUsername} comentó tu post`;
      body = "Toca para leer el comentario.";
    } else if (notificationData.type === "chat") {
      title = `Nuevo mensaje de ${notificationData.fromUsername}`;
      body = "Toca para responder.";
    }

    const dataPayload = {
      type: notificationData.type,
      postId: notificationData.postId || "",
    };

    await sendPushNotification(fcmToken, title, body, dataPayload);
  }
);