
package vaevictis.friends;


import java.io.Serializable;
import java.util.Objects;

public class FriendsId implements Serializable {

    private String origin;
    private String destiny;

    public FriendsId() {
    }

    public FriendsId(String origin, String destiny) {
        this.origin = origin;
        this.destiny = destiny;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsId friendsId = (FriendsId) o;
        return friendsId.equals(friendsId.origin) &&
        friendsId.equals(friendsId.destiny);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny);
    }
}