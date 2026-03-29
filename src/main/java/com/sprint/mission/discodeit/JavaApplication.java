package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("---------------------------User 테스트 시작--------------------------");
        UserService userService = new JCFUserService();
        List<User> users;

        ChannelService channelservice = new JCFChannelService();
        List<Channel> channels;

        MessageService messageService = new JCFMessageService();
        List<Message> messages;

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

        UUID userId = userinfo.getId();
        UserUpdateDto UserUpdateDto = new UserUpdateDto(userId, "홍중동", "yungyang@codeit.com", "1234", "yungyang");

        User userUpdateInfo = userService.update(UserUpdateDto);
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
        UUID channel1InfoId = channel1Info.getId();
        ChannelUpdateDto ChannelUpdateDto
                = new ChannelUpdateDto(channel1InfoId, "채널3", "채널 1을 3으로 변경", ChannelType.FORUM, true);
        Channel channel1Update = channelservice.update(ChannelUpdateDto);
        System.out.println(channel1Update);

        System.out.println("\n------------------------ [ Channel 삭제 ] -----------------------");

        Channel channel1del = channelservice.delete(channel1.getId());
        System.out.println("삭제된 채널 id : " + channel1del.getId() + ", 채널명 : " + channel1del.getChannelName());
        channelservice.findAll().forEach(System.out::println);

        System.out.println("\n--------------------------- Channel 테스트 끝 --------------------------");
        System.out.println("\n--------------------------- Message 테스트 시작 -------------------------");
        System.out.println("\n--------------------- [ Message 등록 및 전체 조회 ] ----------------------");
        User sender1 = new User("사람1", "person1@codeit.com", "1234", "person1");
        User sender2 = new User("사람2", "person2@codeit.com", "1234", "person2");
        User sender3 = new User("사람3", "person3@codeit.com", "1234", "person3");

        userService.save(sender1);
        userService.save(sender2);

        UUID roomId1 = UUID.randomUUID();
        UUID roomId2 = UUID.randomUUID();

        Message message1 = new Message(sender1.getId(), roomId1, "사람1이 roomId1에서 보낸 첫 번째 메세지");
        Message message2 = new Message(sender1.getId(), roomId2, "사람1이 roomId2에서 보낸 첫 번째 메세지");
        Message message3 = new Message(sender1.getId(), roomId1, "사람1이 roomId1에서 보낸 두 번째 메세지");
        Message message4 = new Message(sender1.getId(), roomId2, "사람1이 roomId2에서 보낸 두 번째 메세지");
        Message message5 = new Message(sender2.getId(), roomId2, "사람2가 roomId2에서 보낸 첫 번째 메세지");
        Message message6 = new Message(sender2.getId(), roomId2, "사람2가 roomId2에서 보낸 두 번째 메세지");

        messageService.save(message1);
        messageService.save(message2);
        messageService.save(message3);
        messageService.save(message4);
        messageService.save(message5);
        messageService.save(message6);


        messageService.findAll().forEach(System.out::println);

        System.out.println("\n------------------------ [ senderId와 roomId로(한 사람이 특정 채팅방에서 보낸 메시지) 조회 ] ------------------------");

        messageService.findBySenderIdAndRoomId(sender1.getId(), roomId1).forEach(System.out::println);
        System.out.println("\n------------------------ [ MessageId로 조회 ] ------------------------");
        System.out.println(messageService.findById(message1.getId()));
        System.out.println("\n------------------------ [ Message 내용 수정 ] ------------------------");
        MessageUpdateDto dto = new MessageUpdateDto(message1.getId(), message1.getRoomId(), message1.getSenderId(), "메시지 수정");
        Message updateMsg = messageService.update(dto);
        System.out.println(updateMsg);

        System.out.println("\n------------------------ [ Message 삭제 ] ------------------------");
        Message deleteMsg = messageService.delete(message1.getId());
        System.out.println("삭제 된 msg : " + deleteMsg);
        System.out.println(messageService.findById(message1.getId()));

    }
}
