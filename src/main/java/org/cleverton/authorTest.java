package org.cleverton;

public class authorTest {

    private final comments comments;
    private boolean id;
    private boolean name;
    private boolean email;
    private boolean profilePicture;
    private boolean joinedDate;
    private final friends friends;

    public authorTest(comments comments) {
        this.comments = comments;
        friends = new friends(this);
    }

    public authorTest selectId() {
        id = true;
        return this;
    }

    public authorTest ignoreId() {
        id = false;
        return this;
    }

    public authorTest selectName() {
        name = true;
        return this;
    }

    public authorTest ignoreName() {
        name = false;
        return this;
    }

    public authorTest selectEmail() {
        email = true;
        return this;
    }

    public authorTest ignoreEmail() {
        email = false;
        return this;
    }

    public authorTest selectProfilePicture() {
        profilePicture = true;
        return this;
    }

    public authorTest ignoreProfilePicture() {
        profilePicture = false;
        return this;
    }

    public authorTest selectJoinedDate() {
        joinedDate = true;
        return this;
    }

    public authorTest ignoreJoinedDate() {
        joinedDate = false;
        return this;
    }

    public authorTest selectAllFields() {
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