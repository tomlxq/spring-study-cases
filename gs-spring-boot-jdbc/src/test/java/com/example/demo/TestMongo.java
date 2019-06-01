package com.example.demo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import org.junit.Test;

import static com.Constants.DB_NAME;
import static com.Constants.HOST;
import static com.Constants.PORT;

public class TestMongo {




    /**
     * JDBC
     * connection(url,port,username,password)
     * statement
     * results
     */
    @Test
    public void  testMongo() {
        Mongo mongo = new Mongo(Constants.HOST, Constants.PORT);

        DB demo = new DB(mongo, Constants.DB_NAME);
        System.out.println(demo.getName());
        DBCollection collection = demo.getCollection("Hotel");
        DBCursor cursor = collection.find();

        System.out.println(cursor);
        cursor.forEach(key->{
            System.out.println(key);
        });
    }
}
