<template>
    <div class="create-category">
        <el-upload
            class="avatar-uploader"
            action="/api/upload_image"
            name="image"
            :show-file-list="false"
            :headers="{Authorization: getToken()}"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
            <img v-if="avatar" :src="avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-text>板块名称</el-text><el-input v-model="name"></el-input>
        <el-text>简介</el-text><el-input v-model="info" type="textarea"></el-input>
        <br>
        <el-button @click="submitForm" type="primary">提交</el-button>
    </div>
</template>

<script setup>
import { createCategory } from '@/js/api/category';
import { getToken } from '@/js/utils/auth';
import { isEmpty } from 'lodash';
import { useRouter } from 'vue-router';
const name = ref('')
const avatar = ref('')
const info = ref('')
const router = useRouter()
const emit = defineEmits(['finish-load'])

emit('finish-load')

function handleAvatarSuccess(resp, file) {
    avatar.value = resp
}

function beforeAvatarUpload(file) {
    return true
}

async function submitForm() {
    try {
        let id = await createCategory(name.value, avatar.value, info.value)
        ElMessage({message: `恭喜！已完成 ${name.value} 板块的创建`, type: 'success'})
        router.push({name: 'category_index', params: {id: id}})
    } catch (err) {
        ElMessage({message: err, type: 'error'})
    }
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