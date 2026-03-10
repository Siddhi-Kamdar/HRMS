
package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="expenses_review")
@Getter
@Setter
public class ExpensesReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="expense_review_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="expense_id")
    private ExpensesDetail expense;

    @ManyToOne
    @JoinColumn(name="reviewed_by_id")
    private Employee reviewedBy;

    @ManyToOne
    @JoinColumn(name="review_status_id")
    private ApprovalStatus status;

    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @Column(name="is_deleted")
    private Boolean isDeleted=false;

}