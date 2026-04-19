package com.bawnorton.dcfixes.collection;

import java.util.function.Function;

public class StandardLambdaMap<K, V> extends LambdaMap<K, V> {
    public StandardLambdaMap(Function<K, V> elementMapper) {
        super(elementMapper);
    }
}
