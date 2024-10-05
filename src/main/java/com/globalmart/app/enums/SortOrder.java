package com.globalmart.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortOrder {

    ASC("ASC"),
    DESC("DESC");

    private final String order;

}
