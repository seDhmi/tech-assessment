package com.stc.assessment.controller;

import com.stc.assessment.model.PostsComments;
import com.stc.assessment.model.User;
import com.stc.assessment.model.UsersPosts;
import com.stc.assessment.service.PostsCommentsService;
import com.stc.assessment.service.UserService;
import com.stc.assessment.service.UsersPostsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RestController
public class AssessmentEndPointsController {

    @Autowired
    private UserService userService;
    @Autowired
    private UsersPostsService usersPostsService;
    @Autowired
    private PostsCommentsService postsCommentsService;

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

        assert usersPosts != null;
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

        String url = "https://gorest.co.in/public/v2/comments"; // Not good practice, we should put it in config.file
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PostsComments[]> responseEntity = restTemplate.getForEntity(url, PostsComments[].class);
        PostsComments[] postsComments = responseEntity.getBody();

        Map<Long, PostsComments> map = new HashMap<>();
        assert postsComments != null;
        for (PostsComments postsComment : postsComments) {
            long postId = postsComment.getPost_id();
            map.put(postId, postsComment);
        }


        List<UsersPosts> usersPostsList = usersPostsService.findAll();
        for (UsersPosts usersPosts : usersPostsList) {
            Long id = usersPosts.getId();
            if (map.containsKey(id))
                postsCommentsService.createPostsComment(map.get(id));
        }

    }

    @GetMapping("/user/{userId}")
    public List<Object> getUserInfo(@PathVariable("userId") long userId) {
        // Takes user id and returns user info, user posts, and post comments.
        // TODO: using async is better than sync.

        String infoUrl = "https://gorest.co.in/public/v2/users";
        String postsUrl = "https://gorest.co.in/public/v2/posts";
        String commentsUrl = "https://gorest.co.in/public/v2/comments";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> userResponseEntity = restTemplate.getForEntity(infoUrl, User[].class);
        ResponseEntity<UsersPosts[]> userPostsResponseEntity = restTemplate.getForEntity(postsUrl, UsersPosts[].class);
        ResponseEntity<PostsComments[]> postsCommentsResponseEntity = restTemplate.getForEntity(commentsUrl, PostsComments[].class);

        User[] users = userResponseEntity.getBody();
        UsersPosts[] usersPosts = userPostsResponseEntity.getBody();
        PostsComments[] postsComments = postsCommentsResponseEntity.getBody();

        Map<Long, User> userMap = new HashMap<>();
        Map<Long, UsersPosts> usersPostsMap = new HashMap<>();
        Map<Long, PostsComments> postsCommentsMap = new HashMap<>();

        assert users != null;
        for (User user : users) {
            Long id = user.getId();
            userMap.put(id, user);
        }

        assert usersPosts != null;
        for (UsersPosts usersPost : usersPosts) {
            Long id = usersPost.getUser_id();
            usersPostsMap.put(id, usersPost);
        }

        assert postsComments != null;
        for (PostsComments postsComment : postsComments) {
            long postId = postsComment.getPost_id();
            postsCommentsMap.put(postId, postsComment);
        }

        List<Object> result = new ArrayList<>();

        boolean isUser = userMap.containsKey(userId);
        boolean hasPost = usersPostsMap.containsKey(userId);
        if (isUser && hasPost){
            Long id = usersPostsMap.get(userId).getId();

            User user = userMap.get(userId);
            UsersPosts post = usersPostsMap.get(userId);
            boolean hasComment = postsCommentsMap.containsKey(id);
            PostsComments comments = hasComment ? postsCommentsMap.get(id) : new PostsComments();

            result.add(user);
            result.add(post);
            result.add(comments);
        }

        return result;
    }

    @PostMapping("/updateUserInfo")
    public void updateUserInfo(@RequestBody User updatedUser){
        // To update the name of the user by taking user-id and updated-name in the body of the request.
        // 200 HTTP code in case of success.

        userService.updateUserName(updatedUser);
    }
}
