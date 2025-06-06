package com.genc.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
//each lesson associate with course
@Entity
public class Lesson {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	    
	 private String title;
	 
	 @Column(columnDefinition = "LONGTEXT")
	 private String content;

	 @ManyToOne ///1couse many lesson fetch all lesson , same for quiz
	 @JoinColumn(name = "course_id", nullable = false)//courseid foreign key map with particular course
	 private Course course;
	 public int getId() {
		return id;
	}

	public void setId(int lessonId) {
		this.id = lessonId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
}
