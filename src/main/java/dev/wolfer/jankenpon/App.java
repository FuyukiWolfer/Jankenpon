// Package Name

package dev.wolfer.jankenpon;

// Java Libraries

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// External Libraries

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

// App Class

public class App {
  public static void main(String[] args) {
    Javalin server = Javalin.create(config -> {
      config.staticFiles.add("src/resources", Location.EXTERNAL);
    });

    Map<Long, User> userList = new HashMap<Long, User>();

    User compUser = new User("Computer");

    server.get("/", ctx -> {
      byte[] bytes = Files.readAllBytes(Paths.get("src/pages/app.html"));
      ctx.html(new String(bytes));
    });

    server.get("/api/users", ctx -> {
      ctx.json(userList.values().toArray());
    });

    server.post("/api/users", ctx -> {
      User playerUser = ctx.bodyAsClass(User.class);
      userList.put(playerUser.getId(), playerUser);
      ctx.cookie("user", playerUser.getId().toString(), 86400);
      ctx.status(201);
      ctx.json(playerUser);
    });

    server.get("/api/users/{id}", ctx -> {
      Long userId = Long.parseLong(ctx.pathParam("id"));
      if (userList.get(userId) != null) {
        ctx.json(userList.get(userId));
      } else {
        ctx.status(404);
      }
    });

    server.delete("/api/users/{id}", ctx -> {
      Long userId = Long.parseLong(ctx.pathParam("id"));
      if (userList.get(userId) != null) {
        userList.remove(userId);
        ctx.status(204);
      } else {
        ctx.status(404);
      }
    });

    server.get("/api/computer", ctx -> {
      ctx.json(compUser);
    });

    server.post("/api/challenge", ctx -> {
      Long userId = Long.parseLong(ctx.cookie("user") != null ? ctx.cookie("user") : "0");
      if (userList.get(userId) != null) {
        User playerUser = userList.get(userId);
        Move playerMove = ctx.bodyAsClass(Move.class);
        Move compMove = new Move(new Random().nextInt(3));
        String result = new Challenge(playerUser, compUser, playerMove, compMove).getResult();
        ctx.json(result);
      } else {
        ctx.status(403);
      }
    });

    server.start(3000);
  }
}
