package by.jnetworks.roadcamera.entity;

import java.io.Serializable;

public interface BaseEntity<PK extends Serializable> {

    PK getId();
}
