package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Team;

public class Signin extends Controller {
	final static play.data.Form<Team> signinForm = Form.form(Team.class);

	public static Result index() {
		return ok(views.html.signin.render(signinForm));
	}

	public static Result submit() {
		Form<Team> filledForm = signinForm.bindFromRequest();
		String teamId = Team.signinTeam(filledForm.field("email").value(),
				filledForm.field("password").value());
		if (teamId != null) {
			return ok(views.html.signupSuccess.render(teamId));
		}
		return badRequest(views.html.signin.render(filledForm));
	}
}
