package com.stc.assessment.controller;

import com.stc.assessment.model.User;
import com.stc.assessment.model.UsersPosts;
import com.stc.assessment.service.UserService;
import com.stc.assessment.service.UsersPostsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class AssessmentEndPointsController {

    @Autowired
    private UserService userService;
    @Autowired
    private UsersPostsService usersPostsService;

    @GetMapping("/")
    public String index(){ return "Greetings from Spring Boot!"; }

    @GetMapping("/allPosts")
    public ResponseEntity<UsersPosts[]> getAllPostsSortedById(){
        // Get all the posts and return them in the response sorted by id with HTTP status code of 200.

        String url = "https://gorest.co.in/public/v2/posts"; // Not good practice, we should put it in config.file
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity(url, UsersPosts[].class);
    }

    @GetMapping("/allUsers")
    public void getAllUsers(){
        // get all users and store only the users with gender male to your DB (MySQL, PostgreSQL ... etc.).
        // Return 200 HTTP code in case of success.

        String url = "https://gorest.co.in/public/v2/users"; // Not good practice, we should put it in config.file
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(url, User[].class);
        User[] users = responseEntity.getBody();

        assert users != null;
        for (User user : users) {
            if (user.getGender().equals("male")){
                userService.createUsers(user);
            }
        }
    }

    @GetMapping("/getUsersPosts")
    public void getUsersPosts(){
        // Get user’s posts by user id for all the users that you have previously saved. And store the response to your DB.
        // Return 200 HTTP code in case of success.

        UsersPosts[] usersPosts = getAllPostsSortedById().getBody();
        Map<Long, UsersPosts> usersPostsMap = new HashMap<>();
        for (UsersPosts usersPost : usersPosts) {
            Long userId = usersPost.getUser_id();
            usersPostsMap.put(userId, usersPost);
        }

        List<User> userList = userService.findAll();
        for (User user : userList) {
            Long id = user.getId();
            if (usersPostsMap.containsKey(id)) {
                UsersPosts posts = usersPostsMap.get(id);
                usersPostsService.createUsersPosts(posts);
            }
        }
    }

    @GetMapping("/getAllComments")
    public void getAllComments(){
        // Get all the comments for the saved posts in the previous step and store the response to your DB.
        // Return 200 HTTP code in case of success.
    }

    @GetMapping("/user/{userId}")
    public void getUserInfo(){
        // Takes user id and returns user info, user posts, and post comments.
    }

    @PostMapping("/updateUserInfo")
    public void updateUserInfo(){
        // To update the name of the user by taking user-id and updated-name in the body of the request.
        // 200 HTTP code in case of success.
    }
}
