package com.tarp.farmcare.data.model;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.Objects;

public class Recognition implements Comparator<Recognition> {
    String id = "";
    String title = "";
    float confidence = 0F;

    public Recognition(String id, String title, float confidence) {
        this.id = id;
        this.title = title;
        this.confidence = confidence;
    }

    public Recognition() {

    }

    public float getConfidence() {
        return confidence;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title = " + title + ", Confidence = " + confidence;
    }

    @Override
    public int compare(Recognition r1, Recognition r) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recognition)) return false;
        Recognition recognition = (Recognition) o;
        return getId().equals(recognition.getId()) &&
                Objects.equals(getTitle(), recognition.getTitle()) &&
                Objects.equals(getConfidence(), recognition.getConfidence());
    }
}
