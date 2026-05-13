package com.example;

import static spark.Spark.*;

import com.google.gson.Gson;

import java.sql.*;
import java.util.*;

public class Main {

    static Gson gson = new Gson();

    static final String DB_HOST = System.getenv("DB_HOST");
    static final String DB_PORT = System.getenv("DB_PORT");
    static final String DB_NAME = System.getenv("DB_NAME");
    static final String DB_USER = System.getenv("DB_USER");
    static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    static final String URL =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    public static void main(String[] args) {

        port(8080);

        get("/students", (req, res) -> {

            List<Map<String, Object>> students = new ArrayList<>();

            try (
                    Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM students")
            ) {

                while (rs.next()) {

                    Map<String, Object> student = new HashMap<>();

                    student.put("id", rs.getInt("id"));
                    student.put("name", rs.getString("name"));
                    student.put("age", rs.getInt("age"));
                    student.put("career", rs.getString("career"));

                    students.add(student);
                }

            }

            res.type("application/json");
            return gson.toJson(students);
        });

        post("/students", (req, res) -> {

            Map<String, Object> data = gson.fromJson(req.body(), Map.class);

            String name = data.get("name").toString();
            int age = ((Double) data.get("age")).intValue();
            String career = data.get("career").toString();

            try (
                    Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD)
            ) {

                String sql = "INSERT INTO students(name, age, career) VALUES (?, ?, ?)";

                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setString(3, career);

                stmt.executeUpdate();
            }

            res.type("application/json");

            return "{\"message\":\"Student created\"}";
        });
    }
}