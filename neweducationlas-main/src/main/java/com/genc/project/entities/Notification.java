package com.genc.project.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long NotificationId;

    @Column(name = "user_id")
    private int userid;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String Message;

    @Column(name = "type", length = 50) // e.g., "Assignment Due", "New Course", "Announcement"
    private String Type;

    @Column(name = "related_id") // or call it courseId if you prefer
    private Integer relatedId;

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Long getNotificationId() {
        return NotificationId;
    }

    public int getUserid() {
        return userid;
    }

    public String getMessage() {
        return Message;
    }

    public void setNotificationId(Long notificationId) {
        NotificationId = notificationId;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getType() {
        return Type;
    }

    public Notification(String message, String type, int id,int courseId) {
        this.Message=message;
        this.Type=type;
        this.userid = id;
        this.relatedId=courseId;
    }

    public Notification(){

    }
}
