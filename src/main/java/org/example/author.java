package org.example;

public class author {

    private final comments comments;
    private boolean id;
    private boolean name;
    private boolean email;
    private boolean profilePicture;
    private boolean joinedDate;
    private final friends friends;

    public author(comments comments) {
        this.comments = comments;
        friends = new friends(this);
    }

    public author selectId() {
        id = true;
        return this;
    }

    public author ignoreId() {
        id = false;
        return this;
    }

    public author selectName() {
        name = true;
        return this;
    }

    public author ignoreName() {
        name = false;
        return this;
    }

    public author selectEmail() {
        email = true;
        return this;
    }

    public author ignoreEmail() {
        email = false;
        return this;
    }

    public author selectProfilePicture() {
        profilePicture = true;
        return this;
    }

    public author ignoreProfilePicture() {
        profilePicture = false;
        return this;
    }

    public author selectJoinedDate() {
        joinedDate = true;
        return this;
    }

    public author ignoreJoinedDate() {
        joinedDate = false;
        return this;
    }

    public author selectAllFields() {
        id = true;
        name = true;
        email = true;
        profilePicture = true;
        joinedDate = true;
        friends.selectAllFields();
        return this;
    }

    public friends fromFriends() {
        return friends;
    }

    public comments endAuthor() {
        return comments;
    }

    @Override
    public String toString() {
        return "author{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", profilePicture=" + profilePicture +
                ", joinedDate=" + joinedDate +
                ", friends=" + friends +
                '}';
    }
}