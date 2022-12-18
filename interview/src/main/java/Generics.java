/*
 * Copyright (c) TomLuo, Inc. 2022-2022. All rights reserved.
 */

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @ClassName: Generics
 * @Description: Generics
 * @author: tomluo
 * @Date: 2022/12/18 15:57
 **/
public class Generics {
    public <T> List<T> fromArrayToList(T[] a) {
        return Arrays.stream(a).collect(Collectors.toList());
    }

    public static <T, G> List<G> fromArrayToList(T[] a, Function<T, G> mapperFunction) {
        return Arrays.stream(a).map(mapperFunction).collect(Collectors.toList());
    }
}
