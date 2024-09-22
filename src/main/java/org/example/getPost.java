package org.example;

public class getPost {

    private boolean id;
    private boolean title;
    private boolean text;
    private boolean datePublished;
    private boolean views;
    private boolean likes;
    private comments comments;

    public getPost() {
        comments = new comments(this);
    }

    public getPost selectId() {
        id = true;
        return this;
    }

    public getPost ignoreId() {
        id = false;
        return this;
    }

    public getPost selectTitle() {
        title = true;
        return this;
    }

    public getPost ignoreTitle() {
        title = false;
        return this;
    }

    public getPost selectText() {
        text = true;
        return this;
    }

    public getPost ignoreText() {
        text = false;
        return this;
    }

    public getPost selectDatePublished() {
        datePublished = true;
        return this;
    }

    public getPost ignoreDatePublished() {
        datePublished = false;
        return this;
    }

    public getPost selectViews() {
        views = true;
        return this;
    }

    public getPost ignoreViews() {
        views = false;
        return this;
    }

    public getPost selectLikes() {
        likes = true;
        return this;
    }

    public getPost ignoreLikes() {
        likes = false;
        return this;
    }

    public getPost selectAllFields() {
        id = true;
        title = true;
        text = true;
        datePublished = true;
        views = true;
        likes = true;
        comments.selectAllFields();
        return this;
    }

    public comments fromComments() {
        return comments;
    }

    public getPost endGetPost() {
        return this;
    }

    @Override
    public String toString() {
        return "getPost{" +
                "id=" + id +
                ", title=" + title +
                ", text=" + text +
                ", datePublished=" + datePublished +
                ", views=" + views +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}