package org.example;

public class comments {

    private final getPost getPost;
    private boolean id;
    private boolean text;
    private boolean datePublished;
    private author author;

    public comments(getPost getPost) {
        this.getPost = getPost;
        author = new author(this);
    }

    public comments selectId() {
        id = true;
        return this;
    }

    public comments ignoreId() {
        id = false;
        return this;
    }

    public comments selectText() {
        text = true;
        return this;
    }

    public comments ignoreText() {
        text = false;
        return this;
    }

    public comments selectDatePublished() {
        datePublished = true;
        return this;
    }

    public comments ignoreDatePublished() {
        datePublished = false;
        return this;
    }

    public comments selectAllFields() {
        id = true;
        text = true;
        datePublished = true;
        author.selectAllFields();
        return this;
    }

    public author fromAuthor() {
        return author;
    }

    public getPost endComments() {
        return getPost;
    }

    @Override
    public String toString() {
        return "comments{" +
                "id=" + id +
                ", text=" + text +
                ", datePublished=" + datePublished +
                ", author=" + author +
                '}';
    }
}