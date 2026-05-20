package com.yiyang.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private PageInfo page;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(int code, String message, T data, PageInfo page) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.page = page;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> page(T data, long total, int pageNum, int pageSize) {
        PageInfo pageInfo = new PageInfo(total, pageNum, pageSize);
        return new ApiResponse<>(200, "查询成功", data, pageInfo);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    @Data
    public static class PageInfo {
        private long total;
        private int pageNum;
        private int pageSize;

        public PageInfo(long total, int pageNum, int pageSize) {
            this.total = total;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
        }
    }
}
