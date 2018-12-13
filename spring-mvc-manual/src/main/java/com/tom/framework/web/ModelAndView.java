package com.tom.framework.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ModelAndView {
    String viewName;
    Map<String, Object> model;

}
