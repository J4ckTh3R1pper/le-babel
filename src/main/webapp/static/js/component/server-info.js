import axios from "axios"
export default {
    template: `
        <label xmlns="http://www.w3.org/1999/html">
            群号: {{serverInfo.id}}
        </label><br>
        <label>
            群名
            <input type="text" class="text-box full-len" ref="serverName" v-model="serverInfo.name" :readonly="permission < 2 ? 'readonly' : false">
        </label><br>
        <label>
            群简介<textarea ref="serverDesc" class="text-box full-len" v-model="serverInfo.description" :readonly="permission < 2 ? 'readonly' : false" ></textarea>
        </label><br>
        <label for="avatar">
            群头像
            <img :src="serverInfo.avatar"><br>
            <template v-if="permission > 1">
                <input type="file" id="avatar" ref="avatar" class="text-box full-len"
                   accept="image/png, image/jpeg">
                <button @click="updateAvatar">更改群头像</button>
            </template>
        </label><br>
        <label for="banner">
            群横幅
            <img :src="serverInfo.banner"><br>
            <template v-if="permission > 1">
                <input type="file" id="banner" ref="banner" class="text-box full-len"
                   accept="image/png, image/jpeg">
                <button @click="updateBanner">更改群横幅</button>
            </template>
        </label><br>
        <button v-if="permission > 1" @click="updateServer">保存</button>
    `,
    data() {
        return {
            serverInfo: {},
            token: "",
            permission: -1
        }
    },
    methods: {
        updateAvatar() {
            let formData = new FormData();
            formData.append("serverId", this.serverInfo.id)
            formData.append("image", this.$refs['avatar'].files[0]);
            formData.append("token", this.token);
            axios.post("/edit_server/update_avatar", formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
            }).then(e => this.updateInfo())
        },
        updateBanner() {
            let formData = new FormData();
            formData.append("serverId", this.serverInfo.id)
            formData.append("image", this.$refs['banner'].files[0]);
            formData.append("token", this.token);
            axios.post("/edit_server/update_banner", formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
            }).then(e => this.updateInfo())
        },
        updateServer() {
                axios.post("/edit_server/update_server", {
                    serverId: this.serverInfo.id,
                    name: this.$refs['serverName'].value,
                    description: this.$refs['serverDesc'].value,
                    token: this.token
                }, {
                    headers: {
                        'Content-Type': "multipart/form-data"
                    }
                }).then(e => {
                    alert("群聊信息已更新！")
                    this.updateInfo()
                })
        },
        updateInfo() {
            axios.get("server_info?serverId=" + this.serverInfo.id)
                .then(e => {
                    this.serverInfo = e.data;
                })
        }
    }

}