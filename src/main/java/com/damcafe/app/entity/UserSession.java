/*
 * @ (#) UserSession.java   1.0 4/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.damcafe.app.entity;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 4/27/2025
 * version: 1.0
 */
public class UserSession {
    private static String username;

    // Phương thức để lưu tên đăng nhập
    public static void setUsername(String user) {
        username = user;
    }

    // Phương thức để lấy tên đăng nhập
    public static String getUsername() {
        return username;
    }
}
