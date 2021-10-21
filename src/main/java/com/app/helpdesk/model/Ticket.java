package com.app.helpdesk.model;

import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.model.enums.Urgency;
import com.app.helpdesk.util.listener.TicketListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(TicketListener.class)
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDate createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDate desiredResolutionDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owned;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "state_id")
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment attachment;

    @Column(name = "urgency_id")
    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @OneToMany(mappedBy = "ticket")
    private List<Feedback> feedback = new ArrayList<>();

    public void addFeedback(Feedback feedback) {
        this.feedback.add(feedback);
    }

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<History> history = new ArrayList<>();

    public void addHistory(History history) {
        this.history.add(history);
    }

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Transient
    private Ticket previousStateHolder;

    @PostLoad
    private void storeState() {
        previousStateHolder = new Ticket();
        previousStateHolder.setState(this.getState());
    }

}
