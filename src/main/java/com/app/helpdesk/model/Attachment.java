package com.app.helpdesk.model;

import com.app.helpdesk.util.listener.AttachmentListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AttachmentListener.class)
public class Attachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Lob
    @Column(name = "blob")
    private byte[] blob;

    @Column(name = "content_type")
    private String contentType;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "name")
    private String name;
}
