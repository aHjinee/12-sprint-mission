package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("---------------------------User 테스트 시작--------------------------");
        UserService userService = new JCFUserService();
        List<User> users;

        ChannelService channelservice = new JCFChannelService();
        List<Channel> channels;

        System.out.println("\n--------------------- [ user 등록 및 전체 조회 ] --------------------");
        User user1 = new User("홍길동", "dongdong@codeit.com", "1234", "dongdong");
        User user2 = new User("홍영동", "yeongdong@codeit.com", "1234", "yeongdong");

        userService.save(user1);
        userService.save(user2);

        users = userService.findAll();
        for (User u : users) {
            System.out.println(u);
        }

        System.out.println("\n------------------------ [ userId로 조회 ] ------------------------");

        User userinfo = userService.findById(user1.getId());
        System.out.println("user1 : " + userinfo);

        System.out.println("\n------------------------ [ user 정보 수정 ] -----------------------");
        System.out.println("user1 수정 전 : " + userinfo);

        User userUpdateInfo = userService.update(userinfo);
        System.out.println("user1 수정 후 : " + userUpdateInfo);

        System.out.println("\n------------------------ [ user 삭제 ] ---------------------------");
        User userDelete = userService.delete(user1.getId());
        System.out.println("삭제 된 userId : " + userDelete.getId() + ", userName : " + userDelete.getUsername());

        System.out.println("삭제 후 전체 조회");
        userService.findAll().forEach(System.out::println);
        System.out.println("\n--------------------------- User 테스트 끝 --------------------------");

        System.out.println("\n------------------------- Channel 테스트 시작 -------------------------");
        System.out.println("\n--------------------- [ Channel 등록 및 전체 조회 ] --------------------");
        Channel channel1 = new Channel("채널1", "첫 번째 채널", ChannelType.TEXT, false);
        Channel channel2 = new Channel("채널2", "두 번째 채널", ChannelType.VOICE, true);

        channelservice.save(channel1);
        channelservice.save(channel2);

        channelservice.findAll().forEach(System.out::println);

        System.out.println("\n------------------------ [ channelId로 조회 ] ------------------------");

        Channel channel1Info = channelservice.findById(channel1.getId());
        System.out.println(channel1Info);

        System.out.println("\n------------------------ [ Channel 정보 수정 ] -----------------------");

        Channel channel1Update = channelservice.update(channel1Info);
        System.out.println(channel1Update);

        System.out.println("\n------------------------ [ Channel 삭제 ] -----------------------");

        Channel channel1del = channelservice.delete(channel1.getId());
        System.out.println("삭제된 채널 id : " + channel1del.getId() + ", 채널명 : " + channel1del.getChannelName());
        channelservice.findAll().forEach(System.out::println);

        System.out.println("\n--------------------------- Channel 테스트 끝 --------------------------");
    }
}
