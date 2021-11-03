package com.app.helpdesk.util.listener;

import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

@Component
public class AttachmentListener {

    private static HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        AttachmentListener.historyService = historyService;
    }

    @PostPersist
    void afterPersist(Attachment attachment){
        historyService.saveHistoryAfterCreateAttachment(attachment);
    }

    @PostRemove
    void afterRemove(Attachment attachment){
        historyService.saveHistoryAfterRemoveAttachment(attachment);
    }
}
