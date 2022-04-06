package io.github.eziomou.pm.model;

import java.util.Objects;

abstract class BaseEntity<ID> {

    public abstract ID getId();

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        BaseEntity<?> other = (BaseEntity<?>) object;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
