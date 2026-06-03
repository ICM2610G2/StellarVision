import { onValueCreated } from "firebase-functions/v2/database";
import * as admin from "firebase-admin";

admin.initializeApp();

export const enviarPushNotification = onValueCreated(
  {
    ref: "/notifications/{idReceptor}/{idNotificacion}",
    region: "us-central1"
  },
  async (event) => {
    const idReceptor = event.params.idReceptor;
    const data = event.data.val();

    if (!data) return;

    const emisor = data.fromUsername || "Un astrónomo";
    const tipo = data.type;
    const postId = data.postId || "";

    let titulo = "StellarVision";
    let cuerpo = "Tienes una nueva interacción en las estrellas.";

    if (tipo === "like") {
      titulo = "✨ ¡Les gustó tu post!";
      cuerpo = `${emisor} le dio me gusta a tu publicación.`;
    } else if (tipo === "comment") {
      titulo = "💬 Nuevo comentario";
      cuerpo = `${emisor} comentó tu publicación.`;
    } else if (tipo === "chat") {
      titulo = "🌌 Mensaje nuevo";
      cuerpo = `${emisor} te envió un mensaje.`;
    }

    try {
      const userSnapshot = await admin.database().ref(`/users/${idReceptor}`).once("value");
      if (!userSnapshot.exists()) {
        console.log(`El usuario ${idReceptor} no existe en /users.`);
        return;
      }

      const tokenFCM = userSnapshot.child("fcmToken").val();
      if (!tokenFCM) {
        console.log(`El usuario ${idReceptor} no tiene un fcmToken registrado.`);
        return;
      }

      const mensajePush: admin.messaging.Message = {
        token: tokenFCM,
        notification: {
          title: titulo,
          body: cuerpo,
        },
        data: {
          postId: postId,
          type: tipo,
        },
        android: {
          priority: "high",
          notification: {
            sound: "default",
            clickAction: "FLUTTER_NOTIFICATION_CLICK",
          },
        },
      };

      const response = await admin.messaging().send(mensajePush);
      console.log(`Push v2 enviada con éxito para ${tipo}:`, response);

    } catch (error) {
      console.error("Error ejecutando la Cloud Function v2:", error);
    }
  }
);
