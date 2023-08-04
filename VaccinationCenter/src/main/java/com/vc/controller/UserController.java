package com.vc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vc.config.LoginForm;
import com.vc.entity.User;
import com.vc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

	private UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String viewDashboardPage() {
		return "dashboard";
	}

	@RequestMapping("/header")
	public String dashboard(Model model, HttpSession session) {
		// Get the authenticated user's username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUsername = authentication.getName();

		// Store the username in a session attribute
		session.setAttribute("loggedInUsername", loggedInUsername);

		// Your other dashboard logic here

		return "header"; // Return the dashboard page
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		// Add the logout message to the model
		model.addAttribute("logoutMessage", "Logout successful");

		// Redirect the user to the login page
		return "redirect:/login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());

		return "registration";
	}

	@PostMapping("/login")
	public String processLogin(@ModelAttribute LoginForm loginForm, HttpSession session) {
		// Perform custom authentication logic here
		// For simplicity, we assume the username and password are valid
		String username = loginForm.getUsername();
		String password = loginForm.getPassword();

		// Fetch the user from the database based on the provided username
		User user = userRepository.findByUsername(username);

		if (user != null) {
			// Assuming you have previously stored passwords using BCryptPasswordEncoder
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if (passwordEncoder.matches(password, user.getPassword())) {
				// Password matches, user authenticated
				// Set the authenticated user in the session (you might use Spring Security for
				// this in real-world applications)
				session.setAttribute("user", user);
				return "redirect:/header"; // Replace "/dashboard" with your desired landing page after login
			}
		}

		// Invalid credentials, redirect back to the login page with an error message
		return "redirect:/login?error";
	}

	@PostMapping("/register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return "registerSuccess";
	}
}
