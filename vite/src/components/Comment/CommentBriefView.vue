<template>
    <div class="comment-brief-view">
        <UserBriefView :category-id="categoryId" :user-id="userId"/>
        <Markdown :md-text="commentBody" class="comment-body"/>
        <CommentData @toggle-reply-input="toggleReplyInput" :comment-id="commentId" :comment-count="childCount" :liked="liked" :like-count="likeCount"/>
        <span v-show="showReplyInput">
            <el-input v-model="replyText" placeholder="输入回复内容"/><el-button type="primary" @click="replyComment">回复</el-button>
        </span>
        <details class="child-comment" v-if="!isEmpty(children)" open>
            <summary>展开评论</summary>
            <CommentBriefView ref="childrenRef"
                @toggle-reply-input="closeAllReplyInput"
                v-for="i in children"
                :category-id="i.categoryId"
                :children="i.children"
                :comment-id="i.id"
                :comment-body="i.commentBody"
                :post-id="i.postId"
                :create-time="i.createTime"
                :child-count="i.childCount"
                :like-count="i.likeCount"
                :user-id="i.userId"
                :liked="i.liked"
            />
        </details>
    </div>
</template>

<script setup>
import UserBriefView from '../User/UserBriefView.vue';
import Markdown from '@/components/Markdown/index.vue'
import CommentData from './CommentData.vue';
import { isEmpty } from 'lodash';
import { postComment } from '@/js/api/comment';
import { useTemplateRef } from 'vue';

const {commentId, postId, userId, categoryId, commentBody, createTime, likeCount, liked, children = [], childCount = 0} = defineProps({
    commentId: {
        type: Number,
        required: true
    },
    postId: Number,
    userId: Number,
    categoryId: Number,
    commentBody: String,
    createTime: Number,
    likeCount: Number,
    liked: Boolean,
    children: Array,
    childCount: Number,
})

const replyText = ref(null)
const emit = defineEmits(['comment-created', 'toggle-reply-input'])
const showReplyInput = ref(false)

const childrenRef = useTemplateRef('childrenRef')

async function replyComment() {
    if (isEmpty(replyText.value)) return
    let form = {
        postId: postId,
        commentBody: replyText.value,
        parentCommentId: commentId
    }
    try {
        let newCommentId = await postComment(form);
        emit('comment-created')
        replyText.value = null
    } catch (err) {
        console.log(err)
    } finally {
    }
}

function openReplyInput() {
    showReplyInput.value = true
}
function closeReplyInput() {
    showReplyInput.value = false
}
function toggleReplyInput() {
    if (!showReplyInput.value) {
        emit('toggle-reply-input')
        closeAllReplyInput()
        openReplyInput()
    } else closeReplyInput()
}
function closeAllReplyInput() {
    if (childrenRef.value)
        childrenRef.value.forEach(i => {
            i.closeReplyInput()
        })
        closeReplyInput()
}

defineExpose({
    closeReplyInput
})
</script>

<style lang="scss" scoped>
.comment-brief-view {
    &:last-child {
        background-size: 1px 50px !important;
    }
    & > * {
        padding: 5px 0;
    }
    .el-input {
        display: inline;
        margin-right: 5px;
    }
    .child-comment {
        .comment-brief-view {
            background: linear-gradient(#999, #999) 3px 0px/1px 100% no-repeat;
            & > * {
                padding-left: 2em;
            }
    
        }
        .comment-body {
            background: linear-gradient(#999,#999) 3px 50%/20px 1px no-repeat;
        }
    }
}
</style>