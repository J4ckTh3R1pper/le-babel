<template>
    <div class="comment-brief-view">
        <UserBriefView :category-id="categoryId" :user-id="userId"/>
        <Markdown :md-text="commentBody" class="comment-body"/>
        <CommentData 
            @toggle-reply-input="toggleReplyInput"
            ref="dataRef"
            :comment-id="commentId"
            :comment-count="childCount"
            :liked="liked"
            :like-count="likeCount"
        />
        <span v-show="showReplyInput">
            <el-input v-model="replyText" placeholder="输入回复内容"/><el-button type="primary" @click="replyComment">回复</el-button>
        </span>
        <details class="child-comment" v-if="!isEmpty(data.children)" open>
            <summary>展开评论</summary>
            <CommentBriefView ref="childrenRef"
                @toggle-reply-input="closeAllReplyInput"
                @comment-created="refreshData"
                v-for="i in children"
                :key="i.id"
                :data="i"
            />
        </details>
        <el-link
            v-if="!detailed && childCount > 5 && !isNumber(parentCommentId)"
            @click="goToDetails"
        >查看所有回复</el-link>
    </div>
</template>

<script setup>
import UserBriefView from '../User/UserBriefView.vue';
import Markdown from '@/components/Markdown/index.vue'
import CommentData from './CommentData.vue';
import { isEmpty, isNumber } from 'lodash';
import { getSingleComment, postComment } from '@/js/api/comment';
import { nextTick, useTemplateRef } from 'vue';
import { useRouter } from 'vue-router';
import useLoginUserStore from '@/js/module/login_user';
import { storeToRefs } from 'pinia';

const {data, detailed} = defineProps({
    data: Object,
    detailed: Boolean
})
const router = useRouter()

const commentId = data.id
const postId = data.postId
const userId = data.userId
const categoryId = data.categoryId
const commentBody = ref(data.commentBody)
const createTime = data.createTime
const likeCount = data.likeCount
const liked = data.liked
const parentCommentId = data.parentCommentId
const childCount = data.childCount
const children = ref(data.children)
const loginUserStore = useLoginUserStore()

const {isLoggedIn} = storeToRefs(loginUserStore)

const replyText = ref(null)
const emit = defineEmits(['comment-created', 'toggle-reply-input'])
const showReplyInput = ref(false)

const childrenRef = useTemplateRef('childrenRef')
const dataRef = useTemplateRef('dataRef')

async function refreshData() {
    await dataRef.value.refresh()
}

function goToDetails() {
    router.push({name: 'comment_detail', params: {id: commentId}})
}

async function refresh() {
    let newData = await getSingleComment(data.id)
    children.value = []
    newData.children.forEach(i => {
        children.value.push(i)
    })
    console.log(newData.children)
    console.log(children.value)
}

async function replyComment() {
    if (isEmpty(replyText.value)) return
    if (!isLoggedIn.value) {
        ElMessage({message: '请先登录！', type: 'error'})
        return
    }
    let form = {
        postId: data.postId,
        commentBody: replyText.value,
        parentCommentId: data.id
    }
    try {
        let newCommentId = await postComment(form);
        emit('comment-created')
        refresh()
        refreshData()
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