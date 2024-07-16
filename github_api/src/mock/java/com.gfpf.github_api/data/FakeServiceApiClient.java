package com.gfpf.github_api.data;

import com.gfpf.github_api.domain.user.GHCommit;
import com.gfpf.github_api.domain.user.GHRepository;
import com.gfpf.github_api.domain.user.GHTag;
import com.gfpf.github_api.domain.user.GHUser;

import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.List;

public class FakeServiceApiClient {

    private final static ArrayMap<Integer, GHUser> FAKE_GH_USER_SERVICE_DATA;
    private final static ArrayMap<Integer, List<GHRepository>> FAKE_GH_REPOS_SERVICE_DATA;
    private final static ArrayMap<Integer, List<GHTag>> FAKE_GH_TAGS_SERVICE_DATA;

    static {
        FAKE_GH_USER_SERVICE_DATA = new ArrayMap<>(21);
        FAKE_GH_REPOS_SERVICE_DATA = new ArrayMap<>(21);
        FAKE_GH_TAGS_SERVICE_DATA = new ArrayMap<>(21);

        // Adding fake users
        addGHUser(1, "Fake GFPF", "Name 1", "https://avatars0.githubusercontent.com/u/2482739?v=4", "https://github.com/gfpf");
        addGHUser(2, "Fake Nitay", "Name 2", "https://avatars0.githubusercontent.com/u/18?v=4", "https://github.com/nitay");
        addGHUser(3, "Fake kevwil", "Name 3", "https://avatars3.githubusercontent.com/u/26?v=4", "https://github.com/kevwil");
        addGHUser(4, "wycats", "Yehuda Katz", "https://avatars.githubusercontent.com/u/4?v=4", "https://github.com/wycats");
        addGHUser(5, "ezmobius", "Ezra Zygmuntowicz", "https://avatars.githubusercontent.com/u/5?v=4", "https://github.com/ezmobius");
        addGHUser(6, "ivey", "Michael D. Ivey", "https://avatars.githubusercontent.com/u/6?v=4", "https://github.com/ivey");
        addGHUser(7, "evanphx", "Evan Phoenix", "https://avatars.githubusercontent.com/u/7?v=4", "https://github.com/evanphx");
        addGHUser(8, "vanpelt", "Paul Campbell", "https://avatars.githubusercontent.com/u/8?v=4", "https://github.com/vanpelt");
        addGHUser(9, "wayneeseguin", "Wayne E. Seguin", "https://avatars.githubusercontent.com/u/9?v=4", "https://github.com/wayneeseguin");
        addGHUser(10, "brynary", "Bryan Helmkamp", "https://avatars.githubusercontent.com/u/10?v=4", "https://github.com/brynary");
        addGHUser(11, "kevinclark", "Kevin Clark", "https://avatars.githubusercontent.com/u/11?v=4", "https://github.com/kevinclark");
        addGHUser(12, "technoweenie", "Rick Olson", "https://avatars.githubusercontent.com/u/12?v=4", "https://github.com/technoweenie");
        addGHUser(13, "macournoyer", "Matt Aimonetti", "https://avatars.githubusercontent.com/u/13?v=4", "https://github.com/macournoyer");
        addGHUser(14, "takeo", "Takeo Igarashi", "https://avatars.githubusercontent.com/u/14?v=4", "https://github.com/takeo");
        addGHUser(15, "caged", "Ben Lovell", "https://avatars.githubusercontent.com/u/15?v=4", "https://github.com/caged");
        addGHUser(16, "topfunky", "Geoffrey Grosenbach", "https://avatars.githubusercontent.com/u/16?v=4", "https://github.com/topfunky");
        addGHUser(17, "anotherjesse", "Jesse Newland", "https://avatars.githubusercontent.com/u/17?v=4", "https://github.com/anotherjesse");
        addGHUser(18, "roland", "Roland Studer", "https://avatars.githubusercontent.com/u/18?v=4", "https://github.com/roland");
        addGHUser(19, "lukas", "Lukas Biewald", "https://avatars.githubusercontent.com/u/19?v=4", "https://github.com/lukas");
        addGHUser(20, "fanvsfan", "Anders Fajerson", "https://avatars.githubusercontent.com/u/20?v=4", "https://github.com/fanvsfan");
        addGHUser(21, "eileencodes", "Eileen M. Uchitelle", "https://avatars.githubusercontent.com/u/21?v=4", "https://github.com/eileencodes");

        // Adding fake repositories and tags for each user
        for (int userId = 1; userId <= 10; userId++) {
            List<GHRepository> repositories = new ArrayList<>();
            List<GHTag> tags = new ArrayList<>();

            for (int repoId = 1; repoId <= 10; repoId++) {
                String repoName = "Repo " + repoId + " for User " + userId;
                String fullName = "Full Name " + repoId + " for User " + userId;
                String desc = "Description " + repoId + " for User " + userId;
                String repoUrl = "https://github.com/user" + userId + "/repo" + repoId;
                GHRepository repository = new GHRepository(repoId, repoName, fullName, desc, repoUrl);
                repositories.add(repository);
            }

            for (int tagId = 1; tagId <= (int) (Math.random() * 10); tagId++) {
                String tagName = "Tag " + tagId + " for User " + userId;
                GHTag tag = new GHTag("nodeId" + tagId, tagName, "zipballUrl" + tagId, "tarballUrl" + tagId, new GHCommit("sha" + tagId, "url" + tagId));
                tags.add(tag);
            }
            FAKE_GH_REPOS_SERVICE_DATA.put(userId, repositories);
            FAKE_GH_TAGS_SERVICE_DATA.put(userId, tags);
        }
    }

    private static void addGHUser(int id, String login, String name, String avatarUrl, String ghUrl) {
        GHUser user = new GHUser(id, login, name, avatarUrl, ghUrl);
        FAKE_GH_USER_SERVICE_DATA.put(user.getId(), user);
    }

    public static ArrayMap<Integer, GHUser> loadAllGHUsers() {
        return FAKE_GH_USER_SERVICE_DATA;
    }

    public static ArrayMap<Integer, List<GHRepository>> loadAllGHRepositories() {
        return FAKE_GH_REPOS_SERVICE_DATA;
    }

    public static ArrayMap<Integer, List<GHTag>> loadAllGHTags() {
        return FAKE_GH_TAGS_SERVICE_DATA;
    }
}
