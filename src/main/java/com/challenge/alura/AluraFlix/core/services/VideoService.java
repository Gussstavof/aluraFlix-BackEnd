package com.challenge.alura.AluraFlix.core.services;

import com.challenge.alura.AluraFlix.core.entities.Mapper;
import com.challenge.alura.AluraFlix.core.entities.videos.VideoDto;
import com.challenge.alura.AluraFlix.core.entities.videos.Video;
import com.challenge.alura.AluraFlix.core.exception.ExceptionNotFound;
import com.challenge.alura.AluraFlix.core.repositories.CategoryRepository;
import com.challenge.alura.AluraFlix.core.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Mapper mapper;

    public VideoDto saveVideo(VideoDto videoDto){
        searchCategory(videoDto);
        videoRepository.save(mapper.toVideo(videoDto));
        return videoDto;
    }

    public Page<VideoDto> getAllVideos(Pageable pageable){
        var page = videoRepository.findAll(pageable);
        return mapper.toVideoDto(page.getContent());
    }

    public VideoDto getByIdVideo(String id){
        return mapper.toVideoDto(getIdOrThrow(id));
    }

    public VideoDto updateVideo(String id, VideoDto videoDto) {
      return videoRepository.findById(id).map(videoUpdate -> {
          videoUpdate.setTitle(videoDto.getTitle());
          videoUpdate.setDescription(videoDto.getDescription());
          videoUpdate.setUrl(videoDto.getUrl());
          return mapper.toVideoDto(videoRepository.save(videoUpdate));
      }).orElseThrow(() -> new ExceptionNotFound("Id not found"));
    }

    public void deleteVideo(String id){
        videoRepository.deleteById(getIdOrThrow(id).getId());
    }


    public Page<VideoDto> getByTitleVideo(String title, Pageable pageable) {
        var page = videoRepository.findByTitleContains(title, pageable);
        return mapper.toVideoDto(page.getContent());
    }

    private Video getIdOrThrow(String id){
        return videoRepository.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Id not found"));
    }

    private void searchCategory(VideoDto videoDto){
        videoDto.setCategory(
                categoryRepository.findById(videoDto.getCategory().getId())
                        .orElseThrow(() -> new ExceptionNotFound("Id not found")));
    }
}
