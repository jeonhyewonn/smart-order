package com.example.smartorder.item.controller.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse {
    private final List<Response> results;
    private final int totalPage;

    public PageResponse(List<Response> results, int totalPage) {
        this.results = results;
        this.totalPage = totalPage;
    }
}
