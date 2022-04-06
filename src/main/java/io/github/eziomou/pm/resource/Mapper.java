package io.github.eziomou.pm.resource;

public interface Mapper<I, O> {

    O map(I input);
}
