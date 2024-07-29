import axios from "axios";
export default {
    template: `
            <h2>群成员管理</h2></br>
            <label>
                <input type="text" class="text-box full-len" id="searchBar" ref="searchBar" v-model="searchText">
            </label>
            <button id="search" @click="loadUserList()">搜索</button>
            <table id="list_users">
                <tr>
                    <th>用户ID</th>
                    <th>用户名</th>
                    <th>身份</th>
                    <th>群内昵称</th>
                    <th>操作</th>
                </tr>
                <tr v-for="user in userPage.list" class="search_result">
                    <td>{{user.userId}}</td>
                    <td>{{user.username}}</td>
                    <td>{{user.identity == 2 ? "群主" : user.identity == 1 ? "管理员" : "普通用户"}}</td>
                    <td>{{user.nickname ? user.nickname : "无"}}</td>
                    <td>
                        <button v-if="0 < permission && ( permission < 2 || userId != user.userId )" @click="banishUser(user.userId, user.username)">踢出</button>
                        <button v-if="permission > 1 && user.identity == 0 " @click="setAdmin(user.userId, user.username)">设为管理员</button>
                        <button v-if="permission > 1 && user.identity == 1" @click="unSetAdmin(user.userId, user.username)">取消管理员</button>
                        <button v-if="permission > 0 || user.userId == userId"
                                @click="setupDialog(user.userId, user.username)"
                        >
                            更改群昵称
                        </button>
                    </td>
                </tr>
            </table>
            <div class="bottom">共{{userPage.total}}条结果
                <button id="prevPage" @click="loadUserList(userPage.prePage)">上一页</button>
                <button id="nextPage" @click="loadUserList(userPage.nextPage)">下一页</button>
                <button id="jumpPage" @click="loadUserList(toPage)">跳转至</button>
                第<input class="text-box" id="goToPage" type="number" v-model="toPage">/{{userPage.pages}}页
            </div>
            <div>
                <label>
                    邀请新成员：<input class="text-box full-len" id="userIdToAdd" type="text">
                </label>
                <button id="addUser">添加</button>
            </div>
            <dialog id="nicknameDialog" ref="dialog">
                <label for="nicknameInput">设置用户
                    <span id="nicknameUser">{{editingUsername}}</span>的群内昵称：
                    <input class="text-box" id="nicknameInput" type="text" ref="nicknameInput">
                </label>
                <button id="submitNickname" @click="setNickname()">设置</button>
                <button @click="closeDialog()">取消</button>
            </dialog>
    `,
    data() {
        return {
            permission: -1,
            userPage: {list:[]},
            serverInfo: {},
            editingUsername: null,
            editingUserId: null,
            searchText: "",
            userId: 0,
            toPage: 1
        }
    },
    methods: {
        inviteUser(userId) {
            axios.get("/invite_user", {
                params: {
                    serverId: this.serverInfo['id'],
                    userId: userId
                }
            }).then(resp => {
                console.log(resp.data.message);
                this.loadUserList();
            })
        },
        banishUser(userId, username) {
            if (confirm("确定要踢出用户" + username +"吗？"))
                 axios.get("/banish_user", {
					params: {
						serverId: this.serverInfo['id'],
						userId: userId
					}
                 }).then(resp => {
                 console.log(resp.data);
                 this.loadUserList();
            })
        },
        setNickname() {
            axios.get("/set_nickname", {
                params: {
                    serverId: this.serverInfo['id'],
                    userId: this.editingUserId,
                    nickname: this.$refs['nicknameInput'].value
                }
            }).then(resp => {
                console.log(resp.data.message);
                this.loadUserList();
            })
            this.closeDialog();
        },
        setAdmin(userId, username) {
            if (confirm("确定要设置" + username + "为管理员吗？" ))
				axios.get("/set_admin", {
					params: {
						serverId: this.serverInfo['id'],
						userId: userId,
					}
				}).then(resp => {
					console.log(resp.data.message);
					this.loadUserList();
            })
        },
        unSetAdmin(userId, username) {
            if (confirm("确定要移除" + username + "的管理员权限吗？" ))
				axios.get("/unset_admin", {
					params: {
						serverId: this.serverInfo['id'],
						userId: userId,
					}
				}).then(resp => {
					console.log(resp.data.message);
					this.loadUserList();
            })
        },
        search() {
        },
        setupDialog(userId, username) {
            this.editingUsername = username;
            this.editingUserId = userId;
            this.$refs['dialog'].showModal();
        },
        closeDialog() {
            this.$refs['nicknameInput'].value = "";
            this.$refs['dialog'].close();
        },
        loadUserList(page) {
            axios.get("/list_users", {
                params: {
                    serverId: this.serverInfo.id,
                    page: page ? page : this.userPage['pageNum'],
                    searchText: this.searchText
                }
            }).then(resp => {
                this.userPage = resp.data;
                this.toPage = resp.data['pageNum'];
            })
        }
    }
}
