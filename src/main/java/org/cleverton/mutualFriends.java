package org.cleverton;

public class mutualFriends {

    private friends friends;
    private boolean id;
    private boolean name;

    public mutualFriends(friends friends) {
        this.friends = friends;
    }

    public mutualFriends selectId() {
        id = true;
        return this;
    }

    public mutualFriends ignoreId() {
        id = false;
        return this;
    }

    public mutualFriends selectName() {
        name = true;
        return this;
    }

    public mutualFriends ignoreName() {
        name = false;
        return this;
    }

    public mutualFriends selectAllFields() {
        id = true;
        name = true;
        return this;
    }

    public friends endMutualFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "mutualFriends{" +
                "friends=" + friends +
                ", id=" + id +
                ", name=" + name +
                '}';
    }
}