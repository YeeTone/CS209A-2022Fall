import java.util.Objects;

public class Item<K, V> {
        K key;
        V value;

        Item(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item<?, ?> item = (Item<?, ?>) o;
            return Objects.equals(key, item.key) && Objects.equals(value, item.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }