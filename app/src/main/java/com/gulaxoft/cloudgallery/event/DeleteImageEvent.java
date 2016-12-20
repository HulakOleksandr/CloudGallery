package com.gulaxoft.cloudgallery.event;

import com.gulaxoft.cloudgallery.entity.Image;

import lombok.Getter;

/**
 * Created by gos on 20.12.16.
 */

public class DeleteImageEvent {

    @Getter private Image image;

    public DeleteImageEvent(Image image) {
        this.image = image;
    }
}
