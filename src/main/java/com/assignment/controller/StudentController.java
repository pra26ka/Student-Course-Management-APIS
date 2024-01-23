package com.assignment.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.beans.Student;
import com.assignment.exceptions.CourseException;
import com.assignment.exceptions.StudentException;
import com.assignment.payload.request.LoginRequest;
import com.assignment.payload.request.StudentRegisterReq;
import com.assignment.security.jwt.JWTUtils;
import com.assignment.security.services.UserDetailsServiceImpl;
import com.assignment.services.StudentService;

@RestController
@RequestMapping("/api")
@Api(value="Student Management")
@Slf4j
public class StudentController {
	
	@Autowired
	private StudentService sService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTUtils jwtUtils;

	@ApiOperation(value="API used to register new students.", response = String.class)
	@PostMapping("/student/register-student")
	public ResponseEntity<String> registerNewStudent(@RequestBody StudentRegisterReq studentReq) throws StudentException {
		String message = sService.registerNewStudent(studentReq);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="API used to login with existing students.")
	@PostMapping("/student/login-student")
	public ResponseEntity<?> loginStudent(@RequestBody LoginRequest loginDetails) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getEmailId(), loginDetails.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginDetails.getEmailId());
		
		if(userDetails != null)
			return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
		return new ResponseEntity<>("Invalid user log in details..!", HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value="API used to update courses for student", response = Student.class)
	@PutMapping("/student/{roll}/courses")
	public ResponseEntity<Student> updateCoursesForStudent(@PathVariable("roll") Integer roll, @RequestBody List<Integer> courseIds) throws StudentException {
		Student student1 = sService.updateCoursesForStudent(roll, courseIds);
		log.info("updated Student is "+student1);
		return new ResponseEntity<Student>(student1, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="API used to delete one student", response = String.class)
	@DeleteMapping("/admin/delete-student/{roll}")
	public ResponseEntity<String> deleteStudent(@PathVariable("roll") Integer roll) throws StudentException {
		sService.deleteStudent(roll);
		log.info("Student is successfully deleted");
		return new ResponseEntity<String>("Student deleted...", HttpStatus.OK);
	}

	@ApiOperation(value="API used to get List of all Students for selected course", response = List.class)
	@GetMapping("/admin/all-students/{courseName}")
	public ResponseEntity<List<Student>> viewAllStudentsForEachCourse(@PathVariable("courseName") String courseName) throws CourseException {
		List<Student> students = sService.viewAllStudentsForEachCourse(courseName);
		return new ResponseEntity<List<Student>>(students, HttpStatus.FOUND);
	}

	@ApiOperation(value="API used to allocate courses to student", response = String.class)
	@PostMapping("/admin/add-student-to-course/{courseId}/{roll}")
	public ResponseEntity<String> addStudentToCourse(@PathVariable("courseId") Integer courseId, @PathVariable("roll") Integer roll) throws StudentException, CourseException {
		String message = sService.addStudentToCourse(courseId, roll);
		log.info("Student is successfully allocated the courses with info "+ message);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
	}
}
