package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.document;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    public static Result about(){
		return ok(about.render("About"));
	}
	public static Result document(){
		return ok(document.render("Document"));
	}

}
