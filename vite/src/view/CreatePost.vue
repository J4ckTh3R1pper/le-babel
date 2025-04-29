<template>
    <div class="create-post">
        <BackButton :category-id="id"/>
        <el-form ref="createPostRef" :model="form" :rules="rules">
            <el-form-item prop="title">
                <el-input v-model="form.title" placeholder="请输入标题"/>
            </el-form-item>
            <el-form-item prop="content">
                <el-input style="display: none" v-model="form.content"/>
            </el-form-item>
        </el-form>
        <MarkdownEditor v-model="form.content"/>
        <el-button 
            :loading="loading"
            type="primary"
            @click="onSubmit"
        >提交
        </el-button>
    </div>
</template>

<script setup>
import BackButton from '@/components/Category/BackButton.vue';
import { uploadImage } from '@/js/api/upload';
import { MdEditor } from 'md-editor-v3';
import { useRoute, useRouter } from 'vue-router';
import { createPost } from '@/js/api/post';
import MarkdownEditor from '@/components/Markdown/MarkdownEditor.vue';
const { proxy } = getCurrentInstance();

const rules = {
    title: [
        {required: true, trigger: "blur", message: "标题不可为空！"},
        {required: true, trigger: "change", message: "标题不可为空！"},
    ],
    content: [{required: true, trigger: "blur", message: "内容不可为空！"}]
}

const form = ref({
    title: '',
    content: '',
})

const route = useRoute()
const router = useRouter()
const id = ref(Number(route.params['id']))
const emit = defineEmits(['finish-load'])
const loading = ref(false)

watch(route, async (newRoute) => {
    if ( newRoute.name == 'create_post' ) {
        id.value = Number(newRoute.params['id'])
    }
})

emit('finish-load')

async function onSubmit() {
    proxy.$refs.createPostRef.validate(async (valid) => {
        if (valid) {
            loading.value = true
            let data = {
                title: form.value.title,
                content: form.value.content,
                categoryId: id.value
            }
            try {
                let newPostId = await createPost(data)
                loading.value = false
                router.push({
                    name: "post_detail",
                    params: {
                        id: newPostId
                    }
                })
            } catch (err) {
                console.log(err)
            } finally {
                loading.value = false
            }
        }
    })
}


</script>

<style lang="scss" scoped>

</style>