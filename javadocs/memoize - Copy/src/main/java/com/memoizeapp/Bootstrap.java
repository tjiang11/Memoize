//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//


package com.memoizeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

import static spark.Spark.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bootstrap {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8081;

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {
        //Check if the database file exists in the current directory. Abort if not

//        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
//                .buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        AppUser user = new AppUser("firstuser");
//        session.save(user);
//
//        session.getTransaction().commit();
//        session.close();

        DataSource dataSource = configureDataSource();
        if (dataSource == null) {
            System.out.printf("Could not find todo.db in the current directory (%s). Terminating\n",
                    Paths.get(".").toAbsolutePath().normalize());
            System.exit(1);
        }

        //Specify the IP address and Port at which the server should be run
        ipAddress(IP_ADDRESS);
        port(PORT);

        //Specify the sub-directory from which to serve static resources (like html and css)
        staticFileLocation("/public");

        //Create the model instance and then configure and start the web service
        try {
            MemoizeService model = new MemoizeService(dataSource);
            new MemoizeController(model);
        } catch (MemoizeService.TodoServiceException ex) {
            logger.error("Failed to create a TodoService instance. Aborting");
        }
    }

    /**
     * Check if the database file exists in the current directory. If it does
     * create a DataSource instance for the file and return it.
     * @return javax.sql.DataSource corresponding to the todo database
     */
    private static DataSource configureDataSource() {
        Path todoPath = Paths.get(".", "todo.db");
        if ( !(Files.exists(todoPath) )) {
            try { Files.createFile(todoPath); }
            catch (java.io.IOException ex) {
                logger.error("Failed to create toto.db file in current directory. Aborting");
            }
        }

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:todo.db");
        return dataSource;

    }
}
