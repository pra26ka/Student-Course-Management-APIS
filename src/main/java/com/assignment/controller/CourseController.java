package com.assignment.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.beans.Course;
import com.assignment.exceptions.CourseException;
import com.assignment.services.CourseService;

@RestController
@RequestMapping("/api")
@Api(value="Course Management")
public class CourseController {
	
	@Autowired
	private CourseService cService;

	@ApiOperation(value="API used to register new course", response = Course.class)
	@PostMapping("/admin/register-course")
	public ResponseEntity<Course> registerNewCourse(@RequestBody Course course) throws CourseException
	{
		Course course1 = cService.registerNewCourse(course);
		
		return new ResponseEntity<Course>(course1, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="API used to update the course", response = Course.class)
	@PutMapping("/admin/update-course")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course) throws CourseException
	{
		Course course1 = cService.updateCourse(course);
		
		return new ResponseEntity<Course>(course1, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="API used to delete course", response = String.class)
	@DeleteMapping("/admin/delete-course/{courseId}")
	public ResponseEntity<String> deleteCourse(@PathVariable("courseId") Integer courseId) throws CourseException
	{
		cService.deleteCourse(courseId);
		
		return new ResponseEntity<String>("Course deleted...", HttpStatus.OK);
	}

	@ApiOperation(value="API used to view all course", response = Course.class)
	@GetMapping("/all-courses")
	public ResponseEntity<List<Course>> viewAllCourses() throws CourseException
	{
		List<Course> courses = cService.viewAllCourses();
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.FOUND);
	}
}
