import axios from "axios"
export default {
    template: `
        <label>
            群号: {{serverInfo.id}}
        </label><br>
        <label>
            群名
            <input type="text" class="text-box full-len" ref="serverName" v-model="serverInfo.name">
        </label><br>
        <label>
            群简介<textarea ref="serverDesc" class="text-box full-len" v-model="serverInfo.description"></textarea>
        </label><br>
        <label for="avatar">
            群头像
            <img :src="serverInfo.avatar"><br>
            <input type="file" id="avatar" ref="avatar" class="text-box full-len"
                   accept="image/png, image/jpeg">
            <button @click="updateAvatar">更改群头像</button>
        </label><br>
        <label for="banner">
            群横幅
            <img :src="serverInfo.banner"><br>
            <input type="file" id="banner" ref="banner" class="text-box full-len"
                   accept="image/png, image/jpeg">
            <button @click="updateBanner">更改群横幅</button>
        </label><br>
        <button @click="updateServer">保存</button>
    `,
    data() {
        return {
            serverInfo: {},
            token: ""
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
                }).then(e => this.updateInfo())
        },
        updateInfo() {
            axios.get("server_info?serverId=" + this.serverInfo.id)
                .then(e => {
                    this.serverInfo = e.data;
                })
        }
    }

}