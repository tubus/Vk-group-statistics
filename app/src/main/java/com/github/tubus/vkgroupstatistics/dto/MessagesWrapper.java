package com.github.tubus.vkgroupstatistics.dto;

import java.util.ArrayList;
import java.util.List;

public class MessagesWrapper {

    private List<byte[]> messages;

    public MessagesWrapper() {
        messages = new ArrayList<>();
    }

    public List<byte[]> getMessages() {
        return messages;
    }

    public void setMessages(List<byte[]> messages) {
        this.messages = messages;
    }
}
