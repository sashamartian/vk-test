package app;

import app.index.IndexController;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

/**
 * Application vk.
 */
public class Application {
    // Declare dependencies
    public static UserDao userDao;

    public static void main(String[] args) {
        // Instantiate your dependencies
        userDao = new UserDao();

        // Configure Spark
        port(8080);
        int maxThreads = 8;
        threadPool(maxThreads);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.INDEX,   IndexController.serveIndexPage);
        get(Path.Web.LOGIN,   LoginController.serveLoginPage);
        post(Path.Web.LOGIN,  LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        get("*",        ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);

    }
}
