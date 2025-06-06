package com.genc.project.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.genc.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genc.project.entities.Course;
import com.genc.project.entities.Lesson;
import com.genc.project.entities.Quiz;
import com.genc.project.entities.User;

@Controller
public class InstructorController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private QuizService quizService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private EnrollmentService enrollmentService;

	
	@GetMapping("/addCourse")
	public String showAddCourseForm(Model model, Authentication auth) {
		int id = ((UserDetailsImpl) auth.getPrincipal()).getId();
	    User instructor = userService.findById(id);
	    model.addAttribute("user", instructor);
	    return "createCourse"; 
	}
	//
	
	@PostMapping("/addCourse")
	public String addCourse(@ModelAttribute Course course,  
	        @RequestParam("lessonsTitle[]") List<String> titles,
	        @RequestParam("lessonsContent[]") List<String> contents,
	        @RequestParam("quizQuestion[]") List<String> questions,
	        @RequestParam("quizOptions[]") List<List<String>> options,
	        @RequestParam("quizCorrectAnswer[]") List<String> answer,
	        Authentication auth) throws JsonProcessingException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Lesson> lessonList = new ArrayList<>();
	    for (int i = 0; i < titles.size(); i++) {
	        Lesson lesson = new Lesson();
	        lesson.setTitle(titles.get(i));
	        lesson.setContent(contents.get(i));
	        lesson.setCourse(course);
	        lessonList.add(lesson);
	    }
	    course.setLessons(lessonList);
	    List<Quiz> quizList = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Quiz quiz = new Quiz();
            
            quiz.setQuestion(questions.get(i));
            List<String> ops = options.get(i);
            String jsonOptions = String.join(", ", ops); 
            String correctOptionsFormatted = jsonOptions.replaceAll("[\\[\\]\"]", "").trim(); 
            quiz.setAnswerOptions(objectMapper.writeValueAsString(correctOptionsFormatted));
            String correctAnswerFormatted = answer.get(i).replaceAll("[\\[\\]\"]", "").trim();
            quiz.setCorrectAnswers(objectMapper.writeValueAsString(correctAnswerFormatted));
            quiz.setCourse(course);
            quizList.add(quiz);
	        quiz.setCourse(course);
	        quizList.add(quiz);
	    }
	    course.setQuizzes(quizList);

	    int id = ((UserDetailsImpl) auth.getPrincipal()).getId();
	    User instructor = userService.findById(id);
	    
	    course.setInstructor(instructor);
	    courseService.saveCourse(course);

		List<User> alluser = userService.findByRole();
		for (User user : alluser) {
			notificationService.sendNotification(
					user.getId(),
					"New Course has been Added. Enroll Now!!",
					"New Course",
					course.getId()
			);
		}
	    
	    return "redirect:/home"; 
	}

	
	@GetMapping("/updateCourse/{id}")
    public String showEditForm(@PathVariable int id, Model model, Authentication auth) {
		int uid = ((UserDetailsImpl) auth.getPrincipal()).getId();
	    User instructor = userService.findById(uid);
	    model.addAttribute("user", instructor);
        Course course = courseService.getCourse(id);
        model.addAttribute("course", course);
        model.addAttribute("user",instructor);
        return "updateCourse"; 
    }
	



	
	@PostMapping("/updateCourse/{id}")
	public String updateCourse(@PathVariable int id, @ModelAttribute Course course, 
	                           @RequestParam(value = "lessonsTitle[]", required = false) List<String> titles,
	                           @RequestParam(value = "lessonsContent[]", required = false) List<String> contents,
	                           @RequestParam(value = "quizQuestion[]", required = false) List<String> questions,
	                           @RequestParam(value = "quizOptions[]", required = false) List<List<String>> options, 
	                           @RequestParam(value = "quizCorrectAnswer[]", required = false) List<String> answer) throws JsonProcessingException 
	{
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Lesson> lessons = new ArrayList<>();
	    
	    if (titles != null) {
	        for (int i = 0; i < titles.size(); i++) {
	            Lesson lesson = new Lesson();
	            lesson.setTitle(titles.get(i));
	            lesson.setContent(contents.get(i));
	            lesson.setCourse(course);
	            lessons.add(lesson);
	        }
	        lessonService.saveAll(lessons);
	        course.setLessons(lessons);
	    }

	    if (questions != null) {
	        List<Quiz> quizList = new ArrayList<>();
	        for (int i = 0; i < questions.size(); i++) {
	            Quiz quiz = new Quiz();
	            
	            quiz.setQuestion(questions.get(i));
	            List<String> ops = options.get(i);
	            String jsonOptions = String.join(", ", ops); 
	            String correctOptionsFormatted = jsonOptions.replaceAll("[\\[\\]\"]", "").trim(); 
	            quiz.setAnswerOptions(objectMapper.writeValueAsString(correctOptionsFormatted));
	            String correctAnswerFormatted = answer.get(i).replaceAll("[\\[\\]\"]", "").trim();
	            quiz.setCorrectAnswers(objectMapper.writeValueAsString(correctAnswerFormatted));
	            quiz.setCourse(course);
	            quizList.add(quiz);
	        }
	        quizService.saveAll(quizList);
	        course.setQuizzes(quizList);
	    }

	    courseService.updateCourse(course.getId(), course);
		List<User> specuser = enrollmentService.findAllUsers(course.getId());
		String Message = "The Course \"" + course.getTitle() + "\" you have Enrolled is Updated Now with New Lessons and Quizzes!!";
		for (User user : specuser) {
			notificationService.sendNotification(
					user.getId(),
					Message,
					"Course Updation",
					course.getId()
			);
		}

	    return "redirect:/home";
	}


    
    @GetMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable int id) {
		Course course=courseService.getCourse(id);
		List<User> specuser = enrollmentService.findAllUsers(course.getId());
		String Message = "Heads up! The course \"" + course.getTitle() + "\" you were enrolled in has been taken down by the instructor.";
		for (User user : specuser) {
			notificationService.sendNotification(
					user.getId(),
					Message,
					"Course Deletion",
					course.getId()
			);
		}
		courseService.deleteCourse(id);
        return "redirect:/home"; 
    }
    
}

