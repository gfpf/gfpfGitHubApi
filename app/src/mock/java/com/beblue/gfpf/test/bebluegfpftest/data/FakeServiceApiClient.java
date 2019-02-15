package com.beblue.gfpf.test.bebluegfpftest.data;

import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;

import androidx.collection.ArrayMap;

public class FakeServiceApiClient {

    private final static ArrayMap FAKE_GH_USER_SERVICE_DATA;

    static {
        FAKE_GH_USER_SERVICE_DATA = new ArrayMap(3);
        addGHUser(1, "Fake Login 1", "Name 1", "https://avatars0.githubusercontent.com/u/2482739?v=4", "https://github.com/gfpf");
        addGHUser(2, "Fake Login 2", "Name 2", "https://avatars0.githubusercontent.com/u/18?v=4", "https://github.com/nitay");
        addGHUser(3, "Fake Login 3", "Name 3", "https://avatars3.githubusercontent.com/u/26?v=4", "https://github.com/kevwil");
    }

    private static void addGHUser(int id, String login, String name, String avatarUrl, String ghUrl) {
        GHUser user = new GHUser(id, login, name, avatarUrl, ghUrl);
        FAKE_GH_USER_SERVICE_DATA.put(user.getId(), user);
    }


    public static ArrayMap<Integer, GHUser> loadAllGHUsers() {
        return FAKE_GH_USER_SERVICE_DATA;
    }

}