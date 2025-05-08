<template>
    <div class="user-edit">
        <h1>修改个人信息</h1>
        <el-text>上传头像</el-text>
        <el-upload
            class="avatar-uploader"
            action="/api/user/update_avatar"
            name="image"
            :show-file-list="false"
            :headers="{Authorization: getToken()}"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
            <img v-if="headImgUrl" :src="headImgUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-text>昵称</el-text><el-input v-model="nickName"></el-input>
        <el-text>个人简介</el-text><el-input v-model="introduce" type="textarea"></el-input>
        <el-text>性别</el-text><br>
        <el-radio-group v-model="gender">
            <el-radio value="MALE">男</el-radio>
            <el-radio value="FEMALE">女</el-radio>
            <el-radio value="UNKNOWN">保密</el-radio>
        </el-radio-group>
        <br>
        <el-button @click="submitUpdate" type="primary">提交</el-button>
    </div>
</template>

<script setup>
import { getFullInfo, updateUser } from '@/js/api/user';
import useLoginUserStore from '@/js/module/login_user';
import { getToken } from '@/js/utils/auth';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';

const router = useRouter()
const loginUserStore = useLoginUserStore()
const {userId, isLoggedIn} = storeToRefs(loginUserStore)
const emit = defineEmits(['finish-load'])
if (!isLoggedIn.value) {
    ElMessage({message: '请先登录！', type: 'error'})
    router.push({name: 'login'})
}

const data = await getFullInfo(userId.value)
const headImgUrl = ref(data.headImgUrl)
const introduce = ref(data.introduce)
const nickName = ref(data.nickName)
const gender = ref(data.gender)

emit('finish-load')

function handleAvatarSuccess(resp, file) {
    headImgUrl.value = URL.createObjectURL(file.raw)
}

function beforeAvatarUpload(file) {
    return true
}

async function submitUpdate() {
    await updateUser(nickName.value, introduce.value, gender.value)
    ElMessage({message: '用户信息更新成功！', type: 'success'})
}

</script>

<style lang="scss" scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>