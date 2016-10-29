//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.memoizeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class MemoizeController {

    private static final String API_CONTEXT = "/api/v1";

    private final MemoizeService memoizeService;

    private final Logger logger = LoggerFactory.getLogger(MemoizeController.class);

    public MemoizeController(MemoizeService todoService) {
        this.memoizeService = todoService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        get(API_CONTEXT + "/hello", (req, res) -> {
            res.status(200);
            return "Hello, world.";
        }, new JsonTransformer());
    }
}
