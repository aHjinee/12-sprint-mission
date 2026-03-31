package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.common.FileUtils;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMessageService implements MessageService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageService(){
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "my_dir", "Messages");
        FileUtils.init(DIRECTORY);
    }

    private Path makePath(UUID id){
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }


    @Override
    public void save(Message message) {
        Path path = makePath(message.getId());
        boolean result = FileUtils.saveObject(path, message);
        if(!result){
            throw new IllegalStateException("message 저장에 실패했습니다.");
        }
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(FileUtils.loadObject(makePath(id)))
                .filter(obj -> obj instanceof Message)
                .map(obj -> (Message) obj);
    }

    @Override
    public List<Message> findBySenderIdAndRoomId(UUID senderId, UUID roomId) {
        //load쓰지 말고 loadObject로 가져와서 스트림으로 ...?
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return FileUtils.load(DIRECTORY);
    }


    @Override
    public Message delete(UUID id) {
        Message MessageToDelete = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));
        Path path = makePath(id);
        if(!Files.exists(path)){
            throw new RuntimeException("삭제 실패: 해당 " + id + "의 파일을 찾을 수 없습니다.");
        }
        try {
            Files.delete(path);
            return MessageToDelete;
        } catch (IOException e) {
            throw new RuntimeException("파일을 삭제할 수 없습니다.");
        }
    }

    public void deleteAll() {
        try(Stream<Path> stream = Files.list(DIRECTORY)) {
            stream.filter(path ->  path.getFileName().toString().endsWith(EXTENSION))
                    .forEach(path ->{
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message update(MessageUpdateDto dto) {
        //save를 할지.. UserService에서 뺄지..?
        return null;
    }
}
