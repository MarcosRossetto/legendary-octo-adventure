package org.acme.services.mappers;

import org.acme.dtos.input.PostInputDTO;
import org.acme.dtos.output.PostOutputDTO;
import org.acme.entities.Post;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import io.quarkus.logging.Log;

public class PostMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<PostInputDTO, Post>() {
            @Override
            protected void configure() {
                skip(destination.getUser());
            }
        });
        
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public static PostOutputDTO toDTO(Post post) {
        Log.info("Post para DTO: " + post.toString());
        return modelMapper.map(post, PostOutputDTO.class);
    }

    public static Post toEntity(PostInputDTO postInputDTO) {
        return modelMapper.map(postInputDTO, Post.class);
    }

        public static void updateEntityFromEntity(Post source, Post target) {
        modelMapper.map(source, target);
    }
}
