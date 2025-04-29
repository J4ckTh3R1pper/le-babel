<template>
    <div>
        <h1>编写评论</h1>
        <MarkdownEditor v-model="content"/>
        <el-button @click="createComment" type="primary">评论</el-button>
    </div>
</template>

<script setup>
import { postComment } from '@/js/api/comment';
import MarkdownEditor from '../Markdown/MarkdownEditor.vue';

const {postId} = defineProps({
    postId: Number,
})

const emit = defineEmits(['comment-created'])

const content = ref('')

async function createComment() {
    let form = {
        postId: postId,
        commentBody: content.value,
        parentCommentId: null
    }
    try {
        let newCommentId = await postComment(form);
        emit('comment-created')
        content.value = ''
    } catch (err) {
        console.log(err)
    } finally {
    }
}

</script>

<style lang="scss" scoped>

</style>