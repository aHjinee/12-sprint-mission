package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.common.FileUtils;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class FileChannelService implements ChannelService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelService(){
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "my_dir", "Channels");
        FileUtils.init(DIRECTORY);
    }

    private Path makePath(UUID id){
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }


    @Override
    public void save(Channel Channel) {
        Path path = makePath(Channel.getId());
        boolean result = FileUtils.saveObject(path, Channel);
        if(!result){
            throw new IllegalStateException("채널 저장에 실패했습니다.");
        }
    }

    @Override
    public Channel findById(UUID id) {
        return (Channel)FileUtils.loadObject(makePath(id));
    }

    @Override
    public List<Channel> findAll() {
        return FileUtils.load(DIRECTORY);
    }


    @Override
    public Channel delete(UUID id) {
        Channel channelToDelete = findById(id);
        Path path = makePath(id);
        if(!Files.exists(path)){
            throw new RuntimeException("삭제 실패: 해당 " + id + "의 파일을 찾을 수 없습니다.");
        }
        try {
            Files.delete(path);
            return channelToDelete;
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
    public Channel update(ChannelUpdateDto dto) {
        //save를 할지.. UserService에서 뺄지..?
        return null;
    }
}
