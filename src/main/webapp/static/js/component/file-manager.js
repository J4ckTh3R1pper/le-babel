import axios from "axios";

export default {
    template:`
        <h2>文件管理</h2>
        <div>
          <label>
            <input type="text" class="text-box full-len" id="searchBar" ref="searchBar" v-model="searchText">
          </label>
        </div>
        <button id="search" @click="loadFileList()">搜索</button>
        <table id="list-files">
            <tr>
                <th>文件名</th>
                <th>上传日期</th>
                <th>上传者ID</th>
                <th>操作</th>
            </tr>
            <template v-for="file in filePage.list" :key="file.fileId">
                <tr>
                    <td>{{file.fileName + '.' + file.extension}}</td>
                    <td>{{file.uploadDate}}</td>
                    <td>{{file.userId}}</td>
                    <td>
                        <button @click="download(file.fileId)">下载</button>
                        <button v-if="permission > 0 || file.userId == userId" @click="deleteFile(file.fileId, file.fileName)">删除</button>
                    </td>
                </tr>
            </template>
        </table>
        <div class="bottom">共{{filePage.total}}条结果
            <button id="prevPage" @click="loadFileList(filePage.prePage)">上一页</button>
            <button id="nextPage" @click="loadFileList(filePage.nextPage)">下一页</button>
            <button id="jumpPage" @click="loadFileList(toPage)">跳转至</button>
            第<input class="text-box" id="goToPage" type="number" v-model="toPage">/{{filePage.pages}}页
        </div>
        <input class="text-box" type="file" ref="input">
        <button @click="upload">上传新文件</button>
    `,
    data() {
        return {
            userId: 0,
            serverId: 0,
            filePage: {list:[]},
            toPage: 1,
            permission: -1,
            searchText: ""
        }
    },
    methods: {
        download(fileId) {
            window.open("/download?fileId=" + fileId);
        },
        async upload() {
            if (this.$refs['input'].files.length < 1)
                return;
            const data = new FormData();
            data.append("file", this.$refs['input'].files[0]);
            data.append("serverId", this.serverId);
            let resp;
            try {
                resp = await axios.post("/upload_file", data, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    },
                })
            } catch (e) {
                console.log(e);
            }
            finally {
                console.log(resp);
                this.loadFileList();
            }
        },
        loadFileList(page) {
            axios.get("/list_files", {
                params: {
                    serverId: this.serverId,
                    page: page ? page : this.filePage['pageNum'],
                    keyword: this.searchText
                }
            }).then(resp => {
                console.log(resp)
                this.filePage = resp.data;
                this.toPage = resp.data['pageNum'];
            })
        },
        deleteFile(id, fileName) {
			if (confirm("确定要删除文件" + fileName +"吗？"))
				axios.get("/delete_file", {
					params: {
						fileId: id
					}
				}).then(resp => {
					this.loadFileList();
            })
        }
    }
}
