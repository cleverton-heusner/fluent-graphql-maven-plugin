package org.example;

public class friends {

    private final author author;
    private boolean id;
    private boolean name;
    private boolean profilePicture;
    private final mutualFriends mutualFriends;

    public friends(author author) {
        this.author = author;
        mutualFriends = new mutualFriends(this);
    }

    public friends selectId() {
        id = true;
        return this;
    }

    public friends ignoreId() {
        id = false;
        return this;
    }

    public friends selectName() {
        name = true;
        return this;
    }

    public friends ignoreName() {
        name = false;
        return this;
    }

    public friends selectProfilePicture() {
        profilePicture = true;
        return this;
    }

    public friends ignoreProfilePicture() {
        profilePicture = false;
        return this;
    }

    public friends selectAllFields() {
        id = true;
        name = true;
        profilePicture = true;
        mutualFriends.selectAllFields();
        return this;
    }

    public mutualFriends fromMutualFriends() {
        return mutualFriends;
    }

    public author endFriends() {
        return author;
    }

    @Override
    public String toString() {
        return "friends{" +
                "id=" + id +
                ", name=" + name +
                ", profilePicture=" + profilePicture +
                ", mutualFriends=" + mutualFriends +
                '}';
    }
}