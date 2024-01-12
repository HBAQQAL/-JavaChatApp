package com.client.utils;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

/**
 * FXRouter allows to manage scenes switching on JavaFX Application with an easy
 * API
 * Inspired by Angular JS $routeProvider
 *
 * @author Marco Trombino
 * @version 1.0.0
 */
public final class FXRouter {
    private static final String WINDOW_TITLE = "";
    private static final Double WINDOW_WIDTH = 800.0;
    private static final Double WINDOW_HEIGHT = 600.0;
    private static final Double FADE_ANIMATION_DURATION = 800.0;

    // FXRouter Singleton
    private static FXRouter router;
    // FXRouter Main Class reference to get main package
    private static Object mainRef;
    // FXRouter Application Stage reference to set scenes
    private static Stage window;

    // Application Stage title
    private static String windowTitle;
    // Application Stage size
    private static Double windowWidth;
    private static Double windowHeight;

    // routes switching animation
    private static String animationType;
    private static Double animationDuration;

    // FXRouter routes map
    private static AbstractMap<String, RouteScene> routes = new HashMap<>();
    // FXRouter current route
    private static RouteScene currentRoute;

    /**
     * FXRouter Inner Class used into routes map
     */
    private static class RouteScene {
        // route .fxml Scene path
        private String scenePath;
        // Scene styleSheet path
        private String styleSheetPath;
        // Scene (Stage) title
        private String windowTitle;
        private double sceneWidth;
        private double sceneHeight;
        // route data passed from goTo()
        private Object data;

        private RouteScene(String scenePath, String styleSheetPath) {
            this(scenePath, getWindowTitle(), getWindowWidth(), getWindowHeight(), styleSheetPath);
        }

        private RouteScene(String scenePath, String windowTitle, String styleSheetPath) {
            this(scenePath, windowTitle, getWindowWidth(), getWindowHeight(), styleSheetPath);
        }

        private RouteScene(String scenePath, double sceneWidth, double sceneHeight, String styleSheetPath) {
            this(scenePath, getWindowTitle(), sceneWidth, sceneHeight, styleSheetPath);
        }

        /**
         * Route scene constructor
         *
         * @param scenePath:      .FXML scene file that is relative Main Class package
         *                        path (e.g. "loginView/login.fxml" loginView is a
         *                        folder in the ressources folder)
         * @param styleSheetPath: .css scene file that is relative Main Class package
         *                        path (e.g. "loginView/login-styles.css" loginView is a
         *                        folder in the ressources folder)
         * @param windowTitle:    Scene (Stage) title
         * @param sceneWidth:     Scene Width
         * @param sceneHeight:    Scene Height
         */
        private RouteScene(String scenePath, String windowTitle, double sceneWidth, double sceneHeight,
                String styleSheetPath) {
            this.scenePath = scenePath;
            this.styleSheetPath = styleSheetPath;
            this.windowTitle = windowTitle;
            this.sceneWidth = sceneWidth;
            this.sceneHeight = sceneHeight;
        }

        private static String getWindowTitle() {
            return FXRouter.windowTitle != null ? FXRouter.windowTitle : WINDOW_TITLE;
        }

        private static double getWindowWidth() {
            return FXRouter.windowWidth != null ? FXRouter.windowWidth : WINDOW_WIDTH;
        }

        private static double getWindowHeight() {
            return FXRouter.windowHeight != null ? FXRouter.windowHeight : WINDOW_HEIGHT;
        }
    }

    /**
     * FXRouter constructor kept private to apply Singleton pattern
     */
    private FXRouter() {
    }

    public static void bind(Object ref, Stage win) {
        checkInstances(ref, win);
    }

    public static void bind(Object ref, Stage win, String winTitle) {
        checkInstances(ref, win);
        windowTitle = winTitle;
    }

    public static void bind(Object ref, Stage win, double winWidth, double winHeight) {
        checkInstances(ref, win);
        windowWidth = winWidth;
        windowHeight = winHeight;
    }

    /**
     * FXRouter binder with Application Stage and main package
     *
     * @param ref:       Main Class reference
     * @param win:       Application Stage
     * @param winTitle:  Application Stage title
     * @param winWidth:  Application Stage width
     * @param winHeight: Application Stage height
     */
    public static void bind(Object ref, Stage win, String winTitle, double winWidth, double winHeight) {
        checkInstances(ref, win);
        windowTitle = winTitle;
        windowWidth = winWidth;
        windowHeight = winHeight;
    }

    /**
     * set FXRouter references only if they are not set yet
     *
     * @param ref: Main Class reference
     * @param win: Application Stage
     */
    private static void checkInstances(Object ref, Stage win) {
        if (mainRef == null)
            mainRef = ref;
        if (router == null)
            router = new FXRouter();
        if (window == null)
            window = win;
    }

    public static void when(String routeLabel, String scenePath, String styleSeetPath) {
        RouteScene routeScene = new RouteScene(scenePath, styleSeetPath);
        routes.put(routeLabel, routeScene);
    }

    public static void when(String routeLabel, String scenePath, String winTitle, String styleSheetPath) {
        RouteScene routeScene = new RouteScene(scenePath, winTitle, styleSheetPath);
        routes.put(routeLabel, routeScene);
    }

    public static void when(String routeLabel, String scenePath, double sceneWidth, double sceneHeight,
            String styleSheetPath) {
        RouteScene routeScene = new RouteScene(scenePath, sceneWidth, sceneHeight, styleSheetPath);
        routes.put(routeLabel, routeScene);
    }

    /**
     * Define a FXRouter route
     *
     * @param routeLabel:  Route label identifier
     * @param scenePath:   .FXML scene file
     * @param winTitle:    Scene (Stage) title
     * @param sceneWidth:  Scene Width
     * @param sceneHeight: Scene Height
     */
    public static void when(String routeLabel, String scenePath, String winTitle, double sceneWidth,
            double sceneHeight, String styleSheetPath) {
        RouteScene routeScene = new RouteScene(scenePath, winTitle, sceneWidth, sceneHeight, styleSheetPath);
        routes.put(routeLabel, routeScene);
    }

    public static void goTo(String routeLabel) throws IOException {
        // get corresponding route
        RouteScene route = routes.get(routeLabel);
        loadNewRoute(route);
    }

    /**
     * Switch between FXRouter route and show corresponding scenes
     *
     * @param routeLabel: Route label identifier
     * @param data:       Data passed to route
     * @throws Exception: throw FXMLLoader exception if file is not loaded correctly
     */
    public static void goTo(String routeLabel, Object data) throws IOException {
        // get corresponding route
        RouteScene route = routes.get(routeLabel);
        // set route data
        route.data = data;
        loadNewRoute(route);
    }

    /**
     * Helper method of goTo() which load and show new scene
     *
     * @throws Exception: throw FXMLLoader exception if file is not loaded correctly
     */
    private static void loadNewRoute(RouteScene route) throws IOException {
        // set FXRouter current route reference
        currentRoute = route;

        String scenePath = "/" + route.scenePath;
        String sceneStyleSheetPath = "/" + route.styleSheetPath;

        File fxmlFile = new File("src/main/resources" + scenePath);
        File styleSheetFile = new File("src/main/resources" + sceneStyleSheetPath);

        Parent resource = null;
        try {
            resource = FXMLLoader.load(fxmlFile.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("there was an exception ");
        }

        window.setTitle(route.windowTitle);

        window.setScene(new Scene(resource, route.sceneWidth, route.sceneHeight));
        window.getScene().getStylesheets().add(styleSheetFile.toURI().toURL().toExternalForm());
        window.show();

        // set scene animation
        routeAnimation(resource);
    }

    /* Syntactic sugar for goTo() method when FXRouter get set */
    public static void startFrom(String routeLabel) throws Exception {
        goTo(routeLabel);
    }

    public static void startFrom(String routeLabel, Object data) throws Exception {
        goTo(routeLabel, data);
    }

    /**
     * set FXRouter switching animation
     *
     * @param anType: Animation type
     */
    public static void setAnimationType(String anType) {
        animationType = anType;
    }

    /**
     * set FXRouter switching animation
     *
     * @param anType:     Animation type
     * @param anDuration: Animation duration
     */
    public static void setAnimationType(String anType, double anDuration) {
        animationType = anType;
        animationDuration = anDuration;
    }

    /**
     * Animate routes switching based on animation type
     *
     * // * @param resource: .FXML scene file to animate
     */
    private static void routeAnimation(Parent node) {
        String anType = animationType != null ? animationType.toLowerCase() : "";
        switch (anType) {
            case "fade":
                Double fd = animationDuration != null ? animationDuration : FADE_ANIMATION_DURATION;
                FadeTransition ftCurrent = new FadeTransition(Duration.millis(fd), node);
                ftCurrent.setFromValue(0.0);
                ftCurrent.setToValue(1.0);
                ftCurrent.play();
                break;
            default:
                break;
        }
    }

    /**
     * Get current route data
     */
    public static Object getData() {
        return currentRoute.data;
    }

}