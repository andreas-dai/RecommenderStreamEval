package controllers;

import models.Team;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Signup extends Controller{
	static Team team = new Team();
	final static play.data.Form<Team> signupForm = Form.form(Team.class);

	public static Result index() {
		// signupForm = signupForm.fill(new Team("testTeam","test@gmail.com"))
		return ok(views.html.signup.render(signupForm));
	}

	public static Result submit() {
		Form<Team> filledForm = signupForm.bindFromRequest();
		if (filledForm.field("teamName").valueOr("").isEmpty()) {
			filledForm.reject("teamName", "team's name can't be null");
		}
		if (filledForm.field("password").valueOr("").isEmpty()) {
			filledForm.reject("password", "Password can't be null");
		}
		// Check repeated password
		if (!filledForm.field("password").valueOr("").isEmpty()) {
			if (!filledForm.field("password").valueOr("")
					.equals(filledForm.field("repeatPassword").value())) {
				filledForm.reject("repeatPassword", "Password don't match");
			}
		}

		if (!filledForm.hasErrors()) {
			if (team.isEmailExists(filledForm.field("email").value())) {
				filledForm.reject("email", "This email is already taken");
			}
		}

		if (filledForm.hasErrors()) {
			System.out.println("error");
			return badRequest(views.html.signup.render(filledForm));
		} else {
			Team created = filledForm.get();
			String teamId = Team.create(created.teamName, created.email,
					created.password);
			System.out.println("teamId: " + teamId);
			return ok(views.html.signupSuccess.render(teamId));
		}
	}
}
