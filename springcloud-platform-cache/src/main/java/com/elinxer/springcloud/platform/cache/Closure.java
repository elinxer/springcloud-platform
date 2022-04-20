package com.elinxer.springcloud.platform.cache;

public interface Closure<O, I> {
    public O execute(I input);
}