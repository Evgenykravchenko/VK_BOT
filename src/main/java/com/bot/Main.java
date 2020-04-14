package com.bot;

import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;

public class Main {

    public static void main(String[] args) {
        Group group = new Group(194194997, Prop.getDataFromProp("vk_key"));

        group.onSimpleTextMessage(message ->
                new Message()
                        .from(group)
                        .to(message.authorId())
                        .text("Ведутся работы.")
                        .send()
        );

        group.onPhotoMessage(message ->
                new Message()
                        .from(group)
                        .to(message.authorId())
                        .text("Работы же говорю ведутся")
                        .photo("src/main/resources/pictures/notWork.png")
                        .send()
        );
    }
}
